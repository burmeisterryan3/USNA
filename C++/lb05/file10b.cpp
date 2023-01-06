// This program reads a file describing census data, and counts the 
// number of places with type "city", "town", and "CDP", and outputs
// separate totals for each.


#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int main() {

	// Goal -- count number of cities in Maryland
	int city_count = 0;
	int town_count = 0;
	int cdp_count  = 0;
	string word;

	ifstream fin("24md.txt");

	while(fin >> word) {
		if (word == "city") {
			city_count = city_count + 1;
		}
		else if (word == "town") {
			town_count = town_count + 1;
		}
		else if (word == "CDP") {
			cdp_count = cdp_count + 1;
		}
	}

	cout << "City count is " << city_count << endl;
	cout << "Town count is " << town_count << endl;
	cout << "CDP  count is " << cdp_count << endl;

	return 0;
}
