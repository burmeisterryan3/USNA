/****************************************
This program will convert a binary number
with a maximum of 6 digits into a decimal
number.
*****************************************/

#include <iostream>
using namespace std;

int main()
{
  cout << "Welcome to Midn Burmeister's lab02..." << endl;
  
  //Read in input decimal number from user.
  int decnum, MAX6;
  MAX6 = 63;
  cout << "Enter in decimal number no greater than: " << MAX6 << endl;
  cin >> decnum;

  //Establish 6-digit number.
  int binum1, binum2, binum3, binum4, binum5, binum6;

  binum1 = decnum % 2;
  decnum = decnum / 2;
  binum2 = decnum % 2;
  decnum = decnum / 2;
  binum3 = decnum % 2;
  decnum = decnum / 2;
  binum4 = decnum % 2;
  decnum = decnum / 2;
  binum5 = decnum % 2;
  decnum = decnum / 2;
  binum6 = decnum % 2;

  //Output 6 digit binary number.
  cout << "The binary number is" 
    << binum6 << binum5 << binum4 << binum3 << binum2 << binum1 << endl;

  return 0;
}
