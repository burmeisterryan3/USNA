/* SI 335 Spring 2015
 * Project 2, RSA program
 * MIDN Burmeister
 */

#include <unistd.h>
#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <stdlib.h>
#include <string.h>

#include "posint.hpp"

using namespace std;

// Prints a message on proper usage and exits with the given code
void usage (const char* progname, int ret);

// Function prototype. You have to fill in the implementation below.
void powmod (PosInt& result, const PosInt& a, const PosInt& b, const PosInt& n);

// Returns a number that is most likely a prime
void genPrime (PosInt& result);

// Returns a small prime number that is coPrime to phi
void genSmallPrime (PosInt& result, PosInt& phi);

// Returns false if prime number is divisible by small prime
bool passQuickCheck (PosInt result);

int main (int argc, char** argv) {
  // Detect if standard in is coming from a terminal
  bool interactive = isatty(fileno(stdin));

  // Seed the random number generator
  srand (time(NULL));

  // Pick the base to use in the PosInt class
  PosInt::setBase (10);

  // These parameters determine the blocking of characters
  PosInt byte(256);
  int blocklen = 10;
  PosInt topByte(1);
  for (int i=0; i<blocklen-1; ++i) topByte.mul(byte);

  if (argc < 2 || argv[1][0] != '-') usage(argv[0],1);
  if (argv[1][1] == 'k') {
    if (argc != 4) usage(argv[0],3);
    ofstream pubout(argv[2]);
    ofstream privout(argv[3]);
    if (! pubout || ! privout) {
      cerr << "Can't open public/private key files for writing" << endl;
      return 4;
    }
    ////////////////////////////////////////////////////////////////////
    //                 KEY GENERATION                                 //
    ////////////////////////////////////////////////////////////////////

    PosInt p, q, e, d, one (1), temp;
    
    do { // Ensure the values of p and q are not the same
      genPrime(p);
      genPrime(q);
    } while (p.compare(q) == 0);

    PosInt p1 = p;
    PosInt q1 = q;
    p1.sub(one); // p1 = p-1
    q1.sub(one); // q1 = q-1

    // Initialize n and totient (phi) 
    PosInt n (1);
    PosInt phi (1);

    // Calculate n and totient 
    n.mul(p); // user fastermul  // Max n is ~2^300; Min n is ~2^200
    n.fastermul(q); // n=p*q fastermul
    phi.mul(p1);
    phi.fastermul(q1);

    genSmallPrime(e, phi); // e should be coprime to phi
    temp.xgcd(d, one, e, phi); // t always positive so equals d=t
    // Print out the keys to their respective files.
    pubout << e << endl << n << endl;
    privout << d << endl << n << endl;
    
    ///////////////// (end of key generation) //////////////////////////
    pubout.close();
    privout.close();
    if (interactive)
      cerr << "Public/private key pair written to " << argv[2]
           << " and " << argv[3] << endl;
  }
  else if (argv[1][1] == 'e') {
    if (argc != 3) usage(argv[0],3);
    ifstream pubin (argv[2]);
    if (! pubin) {
      cerr << "Can't open public key file for reading" << endl;
      return 4;
    }
    if (interactive)
      cerr << "Type your message, followed by EOF (Ctrl-D)" << endl;
    ////////////////////////////////////////////////////////////////////
    //                  ENCRYPTION WITH PUBLIC KEY                    //
    ////////////////////////////////////////////////////////////////////
    // Read public key from pubin file
    PosInt e, n;
    pubin >> e >> n;

    // Read characters from standard in and encrypt them
    int c;
    PosInt M (0); // Initialize M to zero
    PosInt curByte (topByte); //topByte = 256^9
    
    bool keepGoing = true;
    while (keepGoing) {
      c = cin.get();

      if (c < 0) keepGoing = false; // c < 0 means EOF or error.
      else {
        PosInt next (c); // next character, converted to a PosInt
        next.mul(curByte); // next *= curByte
        M.add(next);     // M = M + next
        curByte.div(byte); // byte = 256
      }

      if (curByte.isZero() || (!keepGoing && !M.isZero())) {
        PosInt E (0);
        powmod(E, M, e, n);
        cout << E << endl;

        // Now reset curByte and M and keep going
        curByte.set(topByte);
        M.set(0);
      }
    }
    ////////////////// (end of encryption) /////////////////////////////
    if (interactive)
      cerr << "Message successfully encrypted." << endl;
    pubin.close();
  }
  else if (argv[1][1] == 'd') {
    if (argc != 3) usage(argv[0],3);
    ifstream privin (argv[2]);
    if (! privin) {
      cerr << "Can't open private key file for reading" << endl;
      return 4;
    }
    if (interactive)
      cerr << "Enter encrypted numbers, one at a time, ending with EOF" << endl;
    ////////////////////////////////////////////////////////////////////
    //                 DECRYPTION WITH PRIVATE KEY                    //
    ////////////////////////////////////////////////////////////////////
    // Get private key from file
    PosInt d, n;
    privin >> d >> n;

    // Read numbers from standard in and decrypt them
    PosInt E, q(1), M, r, curByte(topByte);
    int count; 
 
    while (cin >> E) {
      powmod(M, E, d, n);
      while (!curByte.isZero() || !M.isZero()) {
        PosInt::divrem(q, r, M, curByte);
        char letter = q.convert();
        cout << letter;
        M.set(r);
        curByte.div(byte);
        count++;
      }
      count=0;
      curByte.set(topByte);
      M.set(0);
    }
    ////////////////// (end of decryption) /////////////////////////////
    if (interactive)
      cerr << "Message successfully decrypted." << endl;
    privin.close();
  }
  else if (argv[1][1] == 'm') {
    if (argc != 4) usage(argv[0],3);
    PosInt x(argv[2]), y(argv[3]);
    y.fasterMul(x);
    cout << x << endl;    
  }
  else if (argv[1][1] == 'h') usage(argv[0], 0);
  else usage(argv[0],2);
  return 0;
}

////////////////////////////////////////////////////////////
//              MODULAR EXPONENTIATION                    //
////////////////////////////////////////////////////////////

// Computes a^b mod n, and stores the answer in "result".
void powmod (PosInt& result, const PosInt& a, const PosInt& b, const PosInt& n) {
  PosInt two (2);
  // Base case
  if (b.isOne()) { result.set(a); }
  else {
    PosInt r(0), y(1);
    PosInt::divrem(y, r, b, two); // x = q*y + r divrem(q, r, x, y)
    powmod(result, a, y, n);
    result.mul(result);
    result.mod(n);
    if (r.isOne()) {
      result.mul(a);
      result.mod(n);
    }
  } 
}

////////////////////////////////////////////////////////////
//              KARATSUBA MULTIPLICATION                  //
////////////////////////////////////////////////////////////

// Sets "this" PosInt object to "this" times x.
void PosInt::fasterMul (const PosInt& x) {
  // First figure out the larger of the two input sizes
  int n = digits.size();
  if (n < x.digits.size()) n = x.digits.size();
  
  // Now copy the inputs into vectors of that size, with zero-padding
  vector<int> mycopy(digits);
  vector<int> xcopy(x.digits);
  mycopy.resize(n, 0);
  xcopy.resize(n, 0);

  // Set "this" digit vector to a zeroed-vector of size 2n
  digits.assign (2*n, 0);

  // Now call the array version to do the actual multiplication
  fastMulArray (&digits[0], &mycopy[0], &xcopy[0], n);

  // We have to call normalize in case there are leading zeros
  normalize();
}

// This does the real work of Karatsuba's algorithm
void PosInt::fastMulArray (int* dest, const int* x, const int* y, int len) {
  if (len <= 4) {
    mulArray(dest, x, len, y, len); 
  }
  else {
    int halfLen = len/2;
    int mod = len%2;
  
    // Allocate space and copy into X0 and Y0
    int* X0 = (int*)calloc(halfLen, sizeof(int));
    memcpy(X0, x, halfLen*sizeof(int));
    int* Y0 = (int*)calloc(halfLen, sizeof(int));
    memcpy(Y0, y, halfLen*sizeof(int));
    int* X1 = (int*)calloc((halfLen+1), sizeof(int));
    int* Y1 = (int*)calloc((halfLen+1), sizeof(int));

    // Ensure that the correct amount of memory is read from
    memcpy(X1, x+halfLen, (len-halfLen)*sizeof(int));
    memcpy(Y1, y+halfLen, (len-halfLen)*sizeof(int));
    
    // Allocate space for U, and create necessary V and temp
    int* U = (int*)calloc((halfLen+2), sizeof(int));
    int* V = (int*)calloc((halfLen+2), sizeof(int));
    int* P0 = (int*)calloc((halfLen*2), sizeof(int)); //x0*y0
    int* P1 = (int*)calloc((halfLen*2+2), sizeof(int)); // x1*y1
    int* P2 = (int*)calloc(((halfLen+2)*2), sizeof(int)); // U*V

    // Calculate U 
    PosInt::addArray(U, X0, halfLen);
    PosInt::addArray(U, X1, halfLen+1); 

    // Calculate V
    PosInt::addArray(V, Y0, halfLen);
    PosInt::addArray(V, Y1, halfLen+1);

    // Calculate P0, P1, P2
    PosInt::fastMulArray(P0, X0, Y0, halfLen);
    PosInt::fastMulArray(P1, X1, Y1, halfLen+1);
    PosInt::fastMulArray(P2, U, V, halfLen+2);

    // Free memory associated with unneeded values
    free(X0); free(X1); free(Y0); free(Y1);
    free(U); free(V);

    // Allocate memory for A
    int* A = (int*)calloc(2*len+1, sizeof(int));
    
    // Calculate A
    memcpy(A, P0, (halfLen*2)*sizeof(int));
    memcpy(A+halfLen*2, P1, (halfLen*2+2)*sizeof(int));
    PosInt::addArray(A+halfLen, P2, (halfLen+2)*2);
    PosInt::subArray(A+halfLen, P0, halfLen*2);
    PosInt::subArray(A+halfLen, P1, halfLen*2+2);

    free(P0); free(P1); free(P2);

    // Copy A into the dest array
    memcpy(dest, A, 2*len*sizeof(int));
    free(A);
  }
}

// Prints a message on proper usage and exits with the given code
void usage (const char* progname, int ret) {
  cout
    << "Generate a public-private key pair:" << endl
    << "\t" << progname << " -k PUBLIC_KEY_FILE PRIVATE_KEY_FILE" << endl
    << "Encrypt a message with public key:" << endl
    << "\t" << progname << " -e PUBLIC_KEY_FILE" << endl
    << "Decrypt a message with private key:" << endl
    << "\t" << progname << " -d PRIVATE_KEY_FILE" << endl
    << "Note: PUBLIC_KEY_FILE and PRIVATE_KEY_FILE are any filenames you choose."
    << endl
    << "      Encryption and decryption read and write to/from standard in/out."
    << endl
    << "      You have to end the input with EOF (Ctrl-D if on command line)."
    << endl
    << "      You can use normal bash redirection with < and > to read or" << endl
    << "      write the encryption results to a file instead of standard in/out."
    << endl
    << "Multiply with Karatsuba:" << endl
    << "\t" << progname << " -m NUMBER_1 NUMBER_2"
    << endl;
  exit(ret);
}

// Returns a number that is most likely a prime
void genPrime (PosInt& result) {
  char* max = "1157920892373161954235709850086879078532699846656"
              "40564039457584007913129639936"; // 2^256
  
  PosInt maxRand (max);
  result.rand(maxRand);
  
  PosInt primes ("200560490130"); // primes = product of first 11 primes
  PosInt one (1), temp;
  temp.gcd(result, primes);
  if (temp.isOne()) {
    if (result.isEven()) result.add(one); // Must be odd to be prime
    for (int i = 0; i < 10; i++) { // ~1:1000000 chance not prime
      if (!result.MillerRabin()) genPrime(result);
    }
  } else genPrime(result); // Failed small prime test
}

// Returns a small prime number that is relatively prime to phi
void genSmallPrime (PosInt& e, PosInt& phi) {
  PosInt x;
  int primes[42] = {1009, 1031, 1069, 1409, 1621, 1657, 1741,
                    1789, 2063, 2659, 3181, 3187, 3163, 3331,
                    3581, 3881, 4001, 4201, 4391, 4759, 4639,
                    4861, 4903, 5107, 5437, 5791, 5869, 6143,
                    6343, 6563, 6763, 6977, 7193, 7211, 7247,
                    7451, 7489, 7577, 7639, 7691, 7829, 7919};
  do {
    e.set(primes[rand()%42]);  
    x.gcd(e, phi);
  } while (!x.isOne());
}
