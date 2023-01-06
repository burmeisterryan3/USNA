
// This program reads a file describing census data, and counts the 
// number of places with type "city"

#include <iostream>
#include <fstream>
#include <string>

using namespace std;

int main() {

	int city_count = 0;
	string word;

	ifstream fin("24md.txt");

	while(fin >> word) {
	  if (word=="city")
	    city_count = city_count + 1;
	}

	cout << "City count is " << city_count << endl;

	return 0;
}
