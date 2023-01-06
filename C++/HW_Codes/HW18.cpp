#include <iostream>
     using namespace std;

     int confuse(int&,int);
     int main()
     {
        int x, y, z;
        x = 3;
        y = 2;

        cout << "x=" << x << " y=" << y << endl;
        // What is printed by the above line?

        z = confuse(y, x);
        cout << "x=" << x << " y=" << y << " z=" << z << endl;
        // What is printed by the above line?
        return (0);
      }


      int confuse(int& a, int b)
      {
         a = a + b;
         b = 2 * b;
         cout << "b=" << b << " a=" << a << endl;
         // What is printed by the above line?

         return (b);
      }
