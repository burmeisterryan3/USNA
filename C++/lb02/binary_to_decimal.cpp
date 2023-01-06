/********************************************
This program will convert a four-digit binary 
number into decimal format (i.e. base 10).
********************************************/

#include <iostream>
using namespace std;

int main()
{
  cout << "Welcome to Midn Burmeister's Lab02..." << endl;

  //Read in the binary number from the user.
  int num1, num2, num3, num4;
  cout << "Enter your four-digit binary number: ";
  cin >> num1 >> num2 >> num3 >> num4;

  //Convert binary number into a base 10 format.
  int dec1, dec2, dec3, decimalnum;
  dec1 = num1%2 * 2*2*2;
  dec2 = num2%2 * 2*2;
  dec3 = num3%2 * 2;
  decimalnum = dec1 + dec2 + dec3 + num4;
  
  //Output the decimal number.
  cout << "The decimal number is: " << decimalnum << endl;

  return 0;
}
