#include <iostream>
#include <fstream>
#include <string>
using namespace std;

//Function Prototypes.
int *readDataFromFile(string, int&);
int computeSum(int*, int);
void printSomeData(int*, int, int);
bool isNumberInData(int*, int, int);
int *computeHistogram(int*, int, int);

int main()
{
  //Read in filename.
  string filename;
  cout << "Enter filename: ";
  cin  >> filename;
  cout << endl;

  //Use a function to determine the number of integers int the array.
  int N;
  int *data = readDataFromFile(filename, N);
  cout << "File read, it has " << N << " numbers." << endl;

  //Use a function to calculate a sum of the numbers in the file.
  int sum = computeSum(data, N);
  cout << "Those numbers sum up to " << sum << endl << endl;

  //Use function to print out a certain portion of array specified by user.
  int startIndex, stopIndex;
  cout << "For printing, enter start index: ";
  cin  >> startIndex;
  cout << "For printing, enter stop index: ";
  cin  >> stopIndex;
  printSomeData(data, startIndex, stopIndex);

  //Use function to determine if a number specified by the user is in the array.
  int specialnum;
  bool condition;
  cout << "Enter a number to search for: ";
  cin  >> specialnum;
  condition = isNumberInData(data, N, specialnum);
  cout << "That number WAS";
  if (!condition)
    cout << " NOT";
  cout << " found. " << endl << endl;

  //Use function to output how many times each number occurs.
  int maxNumber = 26;
  int* histogram;
  histogram = computeHistogram(data, N, maxNumber);
  cout << "How often does each number occur?" << endl;
  for (int i = 0; i < maxNumber; i++)
    cout << i << ": " << histogram[i] << endl;

  return 0;
}

/*************************************************
               Function Definitions
*************************************************/

//Read file into array and record total number of integers in array.
int* readDataFromFile(string fname, int &num)
{
  //Open file.
  ifstream fin;
  fin.open(fname.c_str());
  
  //Check to see if file exists.
  if (!fin)
    cout << "File cannot be found!" << endl;

  //Read in contents of file while recording the number of items.
  //Exclude the first number which gives the amount of contents in the array.
  fin >> num;
  int* point = new int[num];
  for (int i = 0; i < num; i++)
    fin >> point[i];

  return point;
}

//Compute the sum of the integers in the array.
int computeSum(int* numarray, int num)
{
  int total = 0;

  //Compute sum.
  for (int i = 0; i < num; i++)
    total += numarray[i];
  
  return total;
}

//Print out the values of the array between a certain segment of the array
//specified by the user.
void printSomeData(int* numarray, int start, int stop)
{
  for (int i = start; i <= stop; i++)
    cout << "At index " << i << " is " << numarray[i] << endl;

  cout << endl;
  return;
}

//Determine if a value specified by the user is in the array.
bool isNumberInData(int* numarray, int num, int special)
{
  for (int i = 0; i < num; i++)
  {
    if (numarray [i] == special)
      return true;
  }
  return false;
}
    
//Determine how many times occurs within the array.
int* computeHistogram(int *numarray, int num, int max)
{
  int* array = new int[num];
  int temp = 0;

  //Set each value of array to 0.
  for (int i = 0; i < num; i++)
    array[i] = 0;

  //Determine how many times each number occurs.
  for (int i = 0; i < num; i++)
  {
    temp = numarray[i];
    array[temp] += 1;
  }
  return array;
}
