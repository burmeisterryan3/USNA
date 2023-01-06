#include <iostream>
#include <string>
using namespace std;

//Function prototypes.
void writeReverse(istream&, ostream&);

int main()
{
  //Ask user for list of words and call the function to read in the words.
  cout << "Enter a sequence of words terminated with 'end': ";
  writeReverse(cin, cout);
  cout << endl;
  return 0;
}

//This function will output the words input by the user.
void writeReverse(istream &IN, ostream &OUT)
{
  //Read in a word.
  string word;
  IN >> word;

  //Base case.
  if (word == "end")
    return;
  
  //Recursive step.
  writeReverse(IN, OUT);

  //Output word to screen.
  OUT << word << " ";
  return;
}
  
