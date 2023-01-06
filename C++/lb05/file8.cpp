/* Write a program that reads in a product computation of the 
   following form:

   N0 * N1 * N2 * N3 * â€¦ * Nn =

   and returns the product of the given ints, 
   along with the number of terms in the product. 
   Note that you donâ€™t know how many integer values the user 
   will enter until they enter an equal sign (=). 

   Example:

   Please enter a product to be computed, followed by an = sign.
   : 4 * 56 * 2 * 7 * 4 =

   The product of the 5 numbers entered is 12544

 */

#include <iostream>
using namespace std;

int main()
{
	// initialization
	cout << "Enter product like 2 * 3 * 4 = " << endl << ": ";
	int product=1;
	int numValues = 0;  // counts number of valid numbers entered
	int number;
	char oper = '*';

	while (oper != '=') {
		// read next pair
		cin >> number >> oper;

		product = product * number;
		numValues = numValues + 1;

	}


	cout << "The product of the " << numValues 
	     << " entered is "        << product << endl;

	return 0;
}


