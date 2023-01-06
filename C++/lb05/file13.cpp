

//*************************************************************
//     File: highestAvg.cpp
/*     Description: - Given a list of midn names/alpha codes followed                  
                      by a grade followed by a ;  
         -        Print out the name of the student with the highest grade
                  (and the grade). 
*/
//*********************************************************************
// Sample input
//    Jones m040101 85;
//    Smith m040201 99;
//    Xenon m050400 78;

#include <iostream>
#include <fstream>
#include <string>
using namespace std;

int main() {
    double grade, maxGrade=-1; // why is -1 safe here?
    string name, alpha, maxGradeName;
    char junkCh;  
     
    // Open the file
    ifstream fin("grades1-input.txt");

    // Process each line, checking if new max is found
    // Keep going so long as we don't run out of names
    while (fin >> name) {

	    // Already have name, now get rest of line
	    fin >> alpha >> grade;

	    fin >> junkCh;  // get semicolon

	    // If new max is found, update
	    if (grade > maxGrade){
		    maxGrade     = grade;		    
		    maxGradeName = name;
	    }
    }

    cout << maxGradeName << " had the highest grade, a " << maxGrade << endl;

    return 0;
}
