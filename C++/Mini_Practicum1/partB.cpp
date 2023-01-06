#include <iostream>
using namespace std;

int main()
{
  //Output name.
  cout << "Welcome to MIDN R.J. Burmeister's (160786) program." << endl;
  
  //Create necessary variables.
  int length1, length2;
  char dimension;
  double area;

  //Read in necessary information to calculate area.
  cout << "Enter room dimensions: ";
  cin >> dimension >> length1 >> length2;

  //Calculate area if dimension is feet or inches.
  if (dimension == 'I')              //Dimension is inches.
    area = double(length1) * length2 / 144;
  else                               //Dimension is feet.
    area = double(length1) * length2;

  //Output area found.
  cout << "Found a room with area " << area << endl << "Bye!" << endl;
 
  return 0;
}
