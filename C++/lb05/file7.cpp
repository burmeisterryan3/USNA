/***************************************************
Sum of numbers

Write a program that reads in positive integers from 
the user, each separated by a space, and the whole 
terminated by a negative number (e.g. 3 22 10 -2),
and outputs the sum of the numbers entered (not
including the terminated negative number!).
***************************************************/
#include <iostream>
using namespace std;

int main()
{
	// initialization
	int num;
	int sum=0;
	cout << "Enter numbers separated by spaces"
	     << " and terminated with a negative number." << endl;

	// read first number
	cin >> num;

	while (num >= 0)  {
		// add in to the sum
		sum = sum + num;

		// read in next number (getting ready for next loop test)
		cin >> num;
	}
	
	cout << "Sum was " << sum << endl;
	
	return 0;
}



