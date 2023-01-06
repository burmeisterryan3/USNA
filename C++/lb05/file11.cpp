#include <iostream>

using namespace std;

// Reads in a value 'n', then prints out the Hankel
// matrix for that value
// e.g. first row:
//     1 2 3 ... n
//     2 3 4 ... n+1
//     ...
//     n n+1 ... 2n-1
// Make the columns line up nicely.  Assume n<=50.
int main() {

	// Read in n
	int N;
	cout << "Please enter N: ";
	cin  >> N;

	// Print out Hankel matrix for N
	// Loop of every row 1 to N
	for (int i=1; i <= N; i++) {
		// Print out one row of numbers
		// Loop over N columns to print number
		for (int j=1; j<=N; j++) {
			// Print out correct number using 2 spaces
			// a. Compute correct number
			int number;
			number = -1 + j + i;

			// b. Print that number in two spaces
			if (number < 10) {
				cout << " ";
			}
			cout << number << " ";

		}

		// finished one row, move down
		cout << endl;
	}

	return 0;
}
