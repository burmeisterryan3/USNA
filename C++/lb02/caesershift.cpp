/****************************************
 ***************************************/

#include <iostream>
using namespace std;

int main()
{
  //Read in key and message from user.
  cout << "Enter a key between 0 and 25: ";
  int key;
  cin >> key;
  cout << "Enter message: ";
  char l1, l2, l3, l4;
  cin >> l1, l2, l3, l4;
  
  //Obtain shift.
  int sh1, sh2, sh3, sh4;
  sh1 = l1 - 'a' + key;
  sh2 = l2 - 'a' + key;
  sh3 = l3 - 'a' + key;
  sh4 = l4 - 'a' + key;

  //Shift message.
  new1 = (sh1 + 32) % 32;
  new2 = (sh2 + 32) % 32;
  new3 = (sh3 + 32) % 32;
  new4 = (sh4 + 32) % 32;


