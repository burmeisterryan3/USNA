/***************************
6-week Practicum
***************************/

#include <iostream>
#include <fstream>
#include <string>
#include <cmath>
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

  //Declare necessary variables for loop.
  string team;
  int score, navyscore = 0, armyscore = 0, i =0;

  //Run loop until there is no more information to be read.
  while (fin >> team){
    fin >> junk >> score;
    if (team == "Navy")
      navyscore += score;
    else
      armyscore += score;
    i++;
    cout << "After event " << i << ", Navy: " << navyscore << " Army: " << armyscore << endl;
  }
  cout << "Time is up!" << endl;

  //Establish difference between scores and determine who won.
  int netscore;
  netscore = fabs(armyscore - navyscore);
  if (netscore >= 10){
    if (navyscore > armyscore)
      cout << "Navy wins by " << netscore << endl;
    else
      cout << "Army wins by " << netscore << endl;
  }
  else
      cout << "Too close, no winner!" << endl;
      
  return 0;
}
