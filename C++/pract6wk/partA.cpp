/***************************
6-week Practicum
***************************/

#include <iostream>
#include <fstream>
#include <string>
using namespace std;

int main()
{
  cout << "Welcome to MIDN Burmeister's (alpha 160786) program" << endl;
  
  //Declare string and obtain filename.
  string filename;
  cout << "Enter filename: ";
  cin >> filename;

  //Open file stream to file named by user.
  ifstream fin;
  fin.open(filename.c_str());

  //Obtain year from file.
  string junk;
  int year;
  fin >> junk >> junk >> junk >> junk >> year;
  cout << "Results for year: " << year << endl;

  return 0;
}
