/* SI 335 Spring 2015
 * Project 1: OMA Service Selection
 *
 * This contains the "simple" algorithm to do the service
 * selection, provided as example/starter code.
 */

#include <iostream>
#include <string>
#include <list>

using namespace std;

int main() {
  int k, n;

  // Read in the number of communities
  cin >> k;
  cin.ignore(1024,'\n');

  // Read in the community names
  string* communities = new string[k];
  for (int i=0; i<k; ++i) {
    getline(cin, communities[i]);
  }

  // Read in the number of proles
  cin >> n;
  
  // Read in the proles' names and rankings
  // Rankings will be stored in an array of arrays.
  // Each array in the array will contain all k rankings for that prole.
  string* proleNames = new string[n];
  int** proleRanks = new int*[n];
  for (int i=0; i<n; ++i) {
    cin >> proleNames[i];

    // Read in the rankings
    proleRanks[i] = new int[k];
    for (int j=0; j<k; ++j) {
      cin >> proleRanks[i][j];
    }
  }

  // This list will hold the names that are already picked
  string* picked = new string[n];

  // Now do the actual selection
  for (int i = 0; i < n; ++i) {
    int comm = i % k; // Which community gets this pick
    int rank = 0; // start with the top-ranked pick
    bool found = false;
    string nextPick;

    while(! found) { 
      rank = rank + 1;

      for (int j = 0; j < n; ++j) {
        if (proleRanks[j][comm] == rank) {
          nextPick = proleNames[j];
        }
      }

      // Now nextPick is the name of the prole with rank "rank"
      // by community "comm". But are they already picked?

      found = true;
      for (int j = 0; j < i; ++j) {
        if (picked[j] == nextPick) found = false;
      }
    }

    // Now we have the actual pick!
    // Print it and then add to the "picked" list.
    cout << nextPick << ' ' << communities[comm] << endl;
    picked[i] = nextPick;
  }

  // De-allocate the dynamic memory we got
  delete [] picked;
  for (int i=0; i<n; ++i) delete [] proleRanks[i];
  delete [] proleRanks;
  delete [] proleNames;
  delete [] communities;

  return 0;
}
