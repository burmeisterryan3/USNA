#include <iostream>
#include <string>
#include <fstream>
using namespace std;

//Function Prototypes.
void readPoint(istream&, double&, double&);

int main()
{
  //Declare necessary variables.
  string input;
  double x, y;

  //Ask user if the information should be taken from the screen or a file.
  cout << "Select input location...enter \"screen\" or a filename: ";
  cin  >> input;

  //Determine whether to read from screen or a file.
  if (input == "screen"){
    cout << "Enter point in the format (x, y): ";
    readPoint(cin, x, y);
  }
  else{    
    ifstream fin(input.c_str());
    readPoint(fin, x, y);
  }
  //Output x and y.
  cout << "x = " << x << ", y = " << y << endl;
  return 0;
}

//Reads in point from a file designated by user.
void readPoint(istream &IN, double &X, double &Y)
{
  char junk;
  IN >> junk >> X >> junk >> Y >> junk;
  return;
}
