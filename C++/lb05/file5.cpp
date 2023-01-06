
//  F 2 3 I 12 18 X
#include <iostream>

using namespace std;

int main() {

	// Prompt for input
	cout << "Welcome to MIDN W.T. Door's (12XXXX) program." << endl;
	cout << "Enter room dimensions: ";

	int length1, length2;
	char units;
	double area;
	double total=0;

	cin >> units;

	while (units != 'X') {
	  cin >> length1 >> length2;

		// compute square feet
		if (units == 'F') 	
			area = length1 * length2;
		else 
			// length in inches
			area = length1 * length2 / 144.0;

		cout << "Found room with area " << area << endl;
		total = total + area;

		// get next command so can test in loop condition	
		cin >> units;
	}
	cout << "Total area was " << total << endl;

	return 0;
}

