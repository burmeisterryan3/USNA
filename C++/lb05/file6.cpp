/************************************************
Given two vectors, I can determine if they are
perpendicular if the dot product of the two vectors
is zero.

Input two vectors in (r,theta) form, convert to
(x, y) form and report whether the vectors are 
perpendicular.
*************************************************/

#include <iostream>
#include <cmath>

using namespace std;

int main()
{

  // Get vector #1 from user
  double r1, theta1;
  char c;
  cout << "Enter vector one (r, theta), with theta in degrees: ";
  cin >> c >> r1 >> c >> theta1 >> c;

  // Get vector #2 from user
  double r2, theta2;
  cout << "Enter vector two (r, theta), with theta in degrees: ";
  cin >> c >> r2 >> c >> theta2 >> c;

  // Convert vectors to x,y
  double x1, y1, x2, y2, Pi;
  Pi = 3.14159265358979324;

  x1 = r1 * cos(theta1*(Pi/180));
  y1 = r1 * sin(theta1*(Pi/180));
  
  x2 = r2 * cos(theta2*(Pi/180));
  y2 = r2 * sin(theta2*(Pi/180));

  
  // caculate dotproduct
  double dotproduct;
  dotproduct = x1*x2 + y1*y2;

  // Print appropriate message
  cout << "Vectors 1 and 2 ";
  if (fabs(dotproduct) < 0.00001) 
    cout << "are perpendicular." << endl;
  else
    cout << "are not perpendicular." << endl;

  return 0;
}


