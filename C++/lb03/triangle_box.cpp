/*******************************************
Allow the user to input three inputs for the
points of a triangle.  The program should
then find the smallest rectangle that can
contain the triangle within it.
*******************************************/

#include <iostream>
using namespace std;

int main()
{
  int c11, c12, c21, c22, c31, c32;
  int rlx, rrx, rty, rby;
  char j;
  cout << "Enter three coordinates for the points of your triangle (i.e. (3,4), 4,5), (0,5): ";
  cin >> j >> c11 >> j >> c12 >> j >> j >> c21 >> j >> c22 >> j >> j >> c31 >> c32 >> j;

  if (c11 < c21)
  {
    //Check for smallest x.
    if (c11 < c31)
      rlx = c11;
    else if (c31 < c21)
      rlx = c31;

    //Check for largest x.
    if (c11 < c31 && c31 > c21)
      rrx = c31;
    if (c11 < c31 && c21 > c31)
      rrx = c21;
  }
  else if (c21 < c31)
  {
    c31 = rrx;
    
    if (c11 < c31)
      c11 = rlx;
    else
      c31 = rlx;
  }
  else if


  return 0;
}
