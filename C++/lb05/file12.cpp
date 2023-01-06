

// This program sums up 5 numbers

#include <iostream>

using namespace std;

int main() {

	// Declare variables
	int sum, num;

	// Read the numbers in and sum up
	sum = 0;
	cout << "Please enter 5 numbers: ";
	for (int i=0; i<5; i++) {
	  cin >> num;
	  sum = sum + num;
	}

	// Output the result
	cout << "The sum was " << sum << endl;

}
