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

  //Read in first dimension before entering loop.
  cout << "Enter room dimensions: ";
  cin >> dimension;

  //Read in dimension and lengths until the user is done.
  while (dimension != 'X'){
    cin >> length1 >> length2;
    
    //Calculate area if dimension is feet or inches.
    if (dimension == 'I')              //Dimension is inches.
      area = double(length1) * length2 / 144;
    else                               //Dimension is feet.
      area = double(length1) * length2;

    //Output the area found for each set of data.
    cout << "Found a room with area " << area << endl;
    cin >> dimension;
  }

  //Signal end of program.
  cout << "Bye!" << endl;
 
  return 0;
}
