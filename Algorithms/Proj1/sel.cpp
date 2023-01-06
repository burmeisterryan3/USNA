/* MIDN Burmeister
 * Algorithms Professor Roche
 * Project 1
 */
#include <iostream>
#include <string>
#include <list>
#include <cstdlib>

using namespace std;

//Will allow me to know location of names for reference
struct nameRanks{
  string name;
  int orderRead;
};

//Update the ranks of each community for the prole just picked.
//The location in the commRanks array is represented as an empty
//string if the name has been selected.
void updateRanks(nameRanks** commRanks, int* commPicks, int** proleRanks,
                 int n, int k, int rank, int comm) {
  int prole = commRanks[comm][rank].orderRead;
  int tempRank;
  for (int i=0; i<k; ++i) {
    tempRank = proleRanks[prole][i];
    commRanks[i][tempRank].name = "";
    while (commPicks[i] < n &&
           commRanks[i][commPicks[i]].name == "") {
      commPicks[i]++;
    }
  }
}

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
 
  /*Allocate memory for the order of the names within
   *each community.  The nameRanks struct is used to ensure
   *that the name has an associated location with it.  The
   *currentPicks array will maintain the current pick for
   *each community.
   *proleRanks is a structure that contains each of the community
   *rankings for a particular prole. This structure will allow
   *for the removal of names in other communities once a name
   *is selected*/ 
  nameRanks** commRanks = new nameRanks*[k];
  int* currentPicks = new int[k];
  int** proleRanks = new int*[n];
  for (int i=0; i<k; ++i) {
    commRanks[i] = new nameRanks[n];
    proleRanks[i] = new int[k];
    currentPicks[i] = 0;
  }

  //Finish reading in proleRanks
  for (int i=k; i<n; i++) {
    proleRanks[i] = new int[k];
  }

  //Read in the names for each prole as well as the
  //the order read for each prole.  Note that
  //the commRanks are in order of community and not
  //the proles. 
  nameRanks nr; 
  int rank;
  for (int i=0; i<n; ++i) {
    cin >> nr.name;
    nr.orderRead = i;
    // Read in the rankings
    for (int j=0; j<k; ++j) {
      cin >> rank;
      proleRanks[i][j] = rank-1;
      commRanks[j][rank-1] = nr;
    }
  }

  //Now do the actual selection and update community rankings
  //as necessary
  for (int i=0; i<n; ++i) {
    int comm = i % k; // Which community gets this pick
    int rank = currentPicks[comm];
    string nextPick = commRanks[comm][rank].name;
    cout << nextPick << ' ' << communities[comm] << endl;
    updateRanks(commRanks, currentPicks, proleRanks, n, k, rank, comm);
  }

  // De-allocate the dynamic memory we got
  for (int i=0; i<k; ++i) {
    delete [] commRanks[i];
    delete [] proleRanks[i];
  }
  for (int i=k; i<n; ++i) {
    delete [] proleRanks[i];
  }
  delete [] currentPicks;  
  delete [] commRanks;
  delete [] communities;
  delete [] proleRanks;

  return 0;
}
