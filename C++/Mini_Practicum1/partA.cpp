#include <iostream>
using namespace std;

int main()
{
  //Output name.
  cout << "Welcome to MIDN R.J. Burmeister's (160786) program." << endl;
  
  //Create necessary variables.
  int length1, length2, area;
  char dimension;
  
  //Read in necessary information to calculate area.
  cout << "Enter room dimensions: ";
  cin >> dimension >> length1 >> length2;

  //Caluclate and output area.
  area = length1 * length2;
  cout << "Found a room with area " << area << endl << "Bye!" << endl;
 
  return 0;
}
