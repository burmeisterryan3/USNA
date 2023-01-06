#include <iostream>
#include <cstdlib>
using namespace std;

int main()
{
  cout << "MIDN Burmeister's (alpha 160786) dice program!" << endl;

  int D, R;
  cout << "Enter how many sides on each die: ";
  cin  >> D;
  cout << "Enter how many rounds to roll: ";
  cin  >> R;

  for (int i = 0; i < R; i++){
    cout << "Roll! Results ";
    int roll1 = rand() % D + 1;
    int roll2 = rand() % D + 1;
    cout << roll1 << " and " << roll2 << endl;
  }

  return 0;
}
