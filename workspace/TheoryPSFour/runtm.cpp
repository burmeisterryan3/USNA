#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
#include <cstring>
#include <queue>
using namespace std;

class Configuration {
public:
  vector<char> u;
  int q;
  queue<char> v;

  Configuration () {
    q = 1;
  }
};

class Delta {
public:
  int currentState;
  char currentTapeCell;
  int nextState;
  char newTapeCell;
  char move;
};

class TuringMachine {
public:
  int numStates;
  int startState;
  int acceptState;
  int rejectState;
  vector<char> tape;
  vector<Delta*> delta;
};

void print(Configuration c) {
  int usize = c.u.size();
  int vsize = c.v.size();
  for (int i = 0; i < usize; i++) {
    cout << c.u.at(i);
  }
  cout << '[' << c.q << ']';
  for (int i = 0; i < vsize; i++) {
    cout << c.v.front();
    c.v.pop();
  }
  cout << endl;
};

void transition(Configuration &c, TuringMachine tm) {
  for (int i = 0; i < tm.delta.size()/5; i++) {
    if (c.q == tm.delta.at(i)->currentState && c.v.front() == tm.delta.at(i)->currentTapeCell) {
      cout << "i made it here" << endl;
      if (tm.delta.at(i)->move == 'L') {
	c.v.pop();
	c.v.push(tm.delta.at(i)->newTapeCell);
	c.v.push(c.u.back());
	c.u.pop_back();
	c.q = tm.delta.at(i)->nextState;
	break;
      } else {
	c.u.pop_back();
	c.u.push_back(tm.delta.at(i)->newTapeCell);
	c.u.push_back(c.v.front());
	c.v.pop();
	c.q = tm.delta.at(i)->nextState;
	break;
      }
    }
  }
};

int main(int argc, char* argv[]) {
  ifstream input;
  input.open(argv[1]);
  TuringMachine tm;
  string line;
  if (input.is_open()) {
    while(getline(input, line)) {
      istringstream iss(line);
      char c;
      iss >> c;
      if (c == 'n') {
	iss >> c;
	tm.numStates = c - '0';
      } else if (c == 's') {
	iss >> c;
	tm.startState = c - '0';
      } else if (c == 'a') {
	iss >> c;
	tm.acceptState = c - '0';
      } else if (c == 'r') {
	iss >> c;
	tm.rejectState = c - '0';
      } else if (c == 'G') {
	while (iss >> c) {
	  tm.tape.push_back(c);
	}
      } else { //c == 'd'
	for (int i = 1; iss >> c; i++) {
	  Delta* d = new Delta();
	  switch(i) {
	  case 1:
	    d->currentState = c - '0';
	    break;
	  case 2:
	    d->currentTapeCell = c;
	    break;
	  case 3:
	    d->nextState = c - '0';
	    break;
	  case 4:
	    d->newTapeCell = c;
	    break;
	  case 5:
	    d->move = c;
	    break;
	  }
	  tm.delta.push_back(d);
	}
      }
    }
  }

  Configuration config;
  for (int i = strlen(argv[2])-1; i >= 0; i--) {
    config.v.push(argv[2][i]);
  }
  print(config);

  while (config.q != tm.acceptState && config.q != tm.rejectState) {
    transition(config, tm);
    //print(config);
  }

  if (config.q == tm.acceptState)
    cout << "accept" << endl;
  else
    cout << "reject" << endl;
  return 0;
}
