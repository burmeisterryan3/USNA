#include <iostream>
using namespace std;

int main()
{
  int height, width, offset;
  
  cout << "Enter height (greater than 2): ";
  cin  >> height;

  cout << "Enter width (greater than 2): ";
  cin  >> width;

  cout << "Enter offset: ";
  cin  >> offset;

  cout << "\n" << "\n";

  for (int i = 0; i < width; i++)
    cout << "*";
  cout << endl;
  for (int i = 0; i < 3; i++){
    cout << "*";
    for (int j = 0; j < (width - 2); j++)
      cout << " ";
    cout << "*" << endl;
  }
  for (int i = 0; i < width; i++)
    cout << "*";

  return 0;
}

  
