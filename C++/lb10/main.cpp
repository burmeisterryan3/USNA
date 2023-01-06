#include <iostream>
#include <fstream>
#include <string>
using namespace std;

string* selectionSort(string*, int);
bool    before(string, string);
string* read(string, int&);
void    write(string*, int, string);
void    noDuplicates(string*, int&);

int main()
{
  //Get filename from user.
  string inputFile, outputFile;
  cout << "Enter input file name: ";
  cin  >> inputFile;
  //Get the output filename from the user.
  cout << "Enter output file name: ";
  cin  >> outputFile;
  //Find out how big to make array and then make array.
  int count = 0;
  string *text = read(inputFile, count);
  //Sort the array.
  text = selectionSort(text, count);
  //Delete duplicates.
  string *noDuplicates(text, count);
  //Output the information into the output file.
  write(newArray, newSize, filename);
  return 0;
}

string *read(string filename, int &count)
{
  ifstream fin;
  fin.open(filename.c_str());
  string junk;
  while (!fin.eof()){
    count++;
    fin >> junk;
  }
  fin.close();
  //Sort the information.
  string *text = new string[count];
  fin.open(filename.c_str());
  for (int i = 0; i < count; i++){
    fin >> text[i];
  }
  return text;
}

string *selectionSort(string *array, int size)
{
  for (int i = size-1; i >= 0; i--){
    int iMax = 0;
    for (int j = 1; j < i; j++){
      if (before(array[iMax], array[j]))
	  iMax = j;
    }
    string temp = array[iMax];
    array[iMax] = array[i];
    array[i] = temp;
  }
  return array; 
}

bool before(string a, string b)
{
  return a < b;
}

void write(string *array, int size, string filename)
{
  ofstream OUT;
  OUT.open(filename.c_str());
  for (int i = 0; i < size; i++){
    OUT << array[i] << endl;
  }
  return;
}

void noDuplicates(string *array, int &size)
{
  int newSize = 0;
  for (int i = 0; i < size; i++){
    if (i == size-1 || array[i] != array[i+1])
      newSize++;
  }
  cout << newSize << ' ' << size;
  string *newArray = new string[newSize];
  int j = 0;
  for (int i = 0; i < size; i++){
    if (array[i] != array[i+1]){
      newArray[j] = array[i];
      j++;
    }
  }
  size = newSize;
  return newArray;
}
