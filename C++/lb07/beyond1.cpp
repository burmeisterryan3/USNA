#include <iostream>
#include <string>
#include <fstream>
using namespace std;

void writeTime(ostream&, int);

int main()
{
  int time;
  string location;
  cout << "Enter time in seconds: ";
  cin  >> time;
  cout << "Select output location... enter \"screen\" or a filename: ";
  cin  >> location;

  if (location == "screen")
    writeTime(cout, time);
  else{
    ofstream fout(location.c_str());
    writeTime(fout, time);
  }

  return 0;
}

void writeTime(ostream &OUT, int timeSec)
{
  int hour, min, sec;
  hour = timeSec / 3600;
  if (hour < 10)
    OUT << "0" << hour << ":";
  min  = (timeSec % 3600) / 60;
  if (min < 10)
    OUT << "0";
  OUT << min << ":";
  sec  = (timeSec % 3600) % 60;
  if (sec < 10)
    OUT << "0";
  OUT << sec << endl;
  
  return;
}
