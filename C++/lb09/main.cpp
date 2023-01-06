#include <iostream>
#include <string>
using namespace std;

double *create(int&);
double *shift (string*, int);

int main()
{
  //Fill array.
  int mem;
  double *words= create(mem);

  //Shift array.
  words = shift(words, mem);

  //Output last "mem" words.
  cout << "The last " << mem << " words are: ";
  for (int i = mem-1; i >= 0; i--){
    cout << words[i] << " ";
  }

  cout << endl;

  return 0;
}


//This function will create an array of a size specified by the user.
double *create(int &size)
{
  //Obtain memory size.
  cout << "Memory size? ";
  cin  >> size;

  //Create array.
  double *P = new double[size];

  //Initialize all values of the array to zero.
  for (int i = 0; i < size; i++){
    P[i] = "";
  }

  //Read in all the strings entered by user and stop when 
  //user enters the word "end".
  cout << "Enter strings: ";
  for (int i = 0; i < size; i++){
    cin >> P[i];
  }

  return P;
}

//This function will shift values as they are read into the array.
double *shift (double *P, int size)
{
  double temp;
  while (temp != "end"){
    cin >> temp;
    if (temp != "end"){
      for (int i = size-1; i > 0; i--){
	  P[i]=P[i-1];
      }
      P[0] = temp;
    }
  }
  return P;
}
