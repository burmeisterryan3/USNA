#include <iostream>
using namespace std;

//Function prototype.
void sequence(int, int, int);

int main()
{
  //Read in the necessary components for the arithmetic in the function.
  int a, b, n;
  cout << "Enter a, b, and n: ";
  cin  >> a >> b >> n;

  //Send values to the function.
  sequence(a, b, n);

  cout << endl;
  return 0;
}

void sequence(int A, int B, int N)
{
  //Base case.
  if (N == 1){
    cout << A;
    return;
  }

  cout << A << ", ";

  //Recursive step.
  sequence(A + B, B, N-1);

  return;
}
