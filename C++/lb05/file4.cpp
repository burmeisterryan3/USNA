 
// Read in a series of grades, terminated by a -1.
// Then output the average and minimum of those grades.

#include <iostream>

using namespace std;

int main() {

	// Declare variables
	int count=0;
	int total=0;
	int minimum=100;
	int grade=0;

	// Read numbers in until we see the -1
	// Sum up grades as we do, and look for minimum
	cin >> grade;

	while (grade != -1) {


		total = total + grade;
		count = count + 1;

		if (grade < minimum) {
			minimum = grade;
		}
		cin >> grade;
	}

	// Output results
	double average = double(total) / count;
	cout << "Average was " << average << endl;
	cout << "Minimum was " << minimum << endl;

	return 0;
}
