#include <iostream>
#include <fstream>
#include <sstream>
#include <cstring>
#include <vector>
#include <string>
using namespace std;

/** Class to hold the contents of each unique delta transition
 *  based on the items contained within the class.
 */
class Delta {
public:
  int currentState;
  char currentTapeCell;
  int nextState;
  char newTapeCell;
  char move;
};

//Print the current configuration of the turing machine
void printConfig(int curState, int head, vector<char> tape) {
  for (int i = 0; i < head; i++) {
    cout << tape.at(i);
  }
  cout << '[' << curState << ']';
  //tape.size()-1 because we don't want the space at the end
  for (int i = head; i < tape.size()-1; i++) {
    cout << tape.at(i);
  }
  cout << endl;
};
 
int main(int argc, char* argv[]) {
  /** The next two variables are used to read in the
   *  file holding the information regarding the TM.
   */
  ifstream input;
  string line;

  /** The next five variables represent the structure
   *  of the turing machine itself.
   */
  int numStates;
  int acceptState;
  int rejectState;
  Delta** delta;
  vector<char> tapeAlphabet;

  /** The next three variables represent the current
   *  configuration of the turing machine.
   */
  vector<char> tape;
  int head = 0;
  int curState;

  input.open(argv[1]);
  if (input.is_open()) {
    int stateTracker;
    int stateTransNum;
    while(getline(input, line)) {
      istringstream iss(line);
      char c;
      iss >> c;
      if (c == 'n') {
	iss >> line;
	stringstream ss(line);
	ss >> numStates;
      } else if (c == 's') {
	iss >> line;
	stringstream ss(line);
	ss >> curState;
      } else if (c == 'a') {
	iss >> line;
	stringstream ss(line);
	ss >> acceptState;
      } else if (c == 'r') {
	iss >> line;
	stringstream ss(line);
	ss >> rejectState;
      } else if (c == 'G') {
	while (iss >> c) {
	  tapeAlphabet.push_back(c);
	}
	delta = new Delta*[numStates];
	for (int j = 0; j < numStates; j++) {
	  delta[j] = new Delta[tapeAlphabet.size()];
	}
      } else { //c == 'd'
	int currentState;
	int nextState;
	/** The number of the transition from the current state
	 *  Will be reset with each new state
	 */
	for (int i = 1; iss >> line; i++) {
	  if (i == 1) {
	    stringstream ss(line);
	    ss >> currentState;
	    if (stateTracker != currentState) {
	      stateTracker = currentState;
	      stateTransNum = 0;
	    } else
	      stateTransNum++;
	    delta[currentState-1][stateTransNum].currentState = currentState;
	  } else if (i == 2) {
	    delta[currentState-1][stateTransNum].currentTapeCell = line[0];
	  } else if (i == 3) {
	    stringstream ss(line);
	    ss >> nextState;
	    delta[currentState-1][stateTransNum].nextState = nextState;
	  } else if (i == 4) {
	    delta[currentState-1][stateTransNum].newTapeCell = line[0];
	  } else if (i == 5) {
	    delta[currentState-1][stateTransNum].move = line[0];
	  }
	}
      }
    }
  }

  //Read the tape input from the user from stdin
  for (int i = 0; i < strlen(argv[2]); i++) {
    tape.push_back(argv[2][i]);
  }
  //Add a space at the end of the tape
  tape.push_back('_');

  //Print the initial configuration
  printConfig(curState, head, tape);

  /** Run the turing machine. Update the current state and head as
   *  approrpriate.
   */
  while (curState != acceptState && curState != rejectState) {
    //Prevent unnecessary looping after transition is found
    bool deltaNotFound = true;
    for (int i = 0; i < numStates && deltaNotFound; i++) {
      for (int j = 0; j < tapeAlphabet.size() && deltaNotFound; j++) {
	if (curState == delta[i][j].currentState &&
	    tape.at(head) == delta[i][j].currentTapeCell) {
	  deltaNotFound = false;
	  tape[head] = delta[i][j].newTapeCell;
	  if (delta[i][j].move == 'L')
	    head--;
	  else
	    head++;
	  curState = delta[i][j].nextState;
	}
      }
      /** If we have cycled through all of the possible
       *  options must exit loop and reject.
       */
      if (numStates == i+1 && deltaNotFound)
	curState = rejectState;
    }	  
    printConfig(curState, head, tape);
  }

  /** Print reject or accept as appropriate at the conclusion of running
   *  the turing machine.
   */
  if (curState == acceptState)
    cout << "accept" << endl;
  else 
    cout << "reject" << endl;

  return 0;
}
