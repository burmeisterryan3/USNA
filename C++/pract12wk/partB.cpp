#include <iostream>
#include <cstdlib>
using namespace std;

int **rollDice (int**, int, int);
int **makeArray(int**, int, int);
void  printArray(int**, int);

int main()
{
  cout << "MIDN Burmeister's (alpha 160786) dice program!" << endl;

  int D, R;
  cout << "Enter how many sides on each die: ";
  cin  >> D;
  cout << "Enter how many rounds to roll: ";
  cin  >> R;

  //Create array.
  int **array = new int*[D];
  for (int i = 0; i < D; i++){
    array[i] = new int[D];
  }
  //Give every index of array zero to start.
  for (int i = 0; i < D; i++){
    for (int j = 0; j < D; j++){
      array[i][j] = 0;
    }
  }

  //Make array of values rolled.
  array = rollDice(array, D, R);

  //Print out the values rolled.
  printArray(array, D);
  
  return 0;
}

int **rollDice(int** array, int size, int numRolls)
{
  //Obtain each roll, print the roll, and send the values to makeArray
  //so that they can be recorded within the array.
  int roll1, roll2;
  for (int i = 0; i < numRolls; i++){
    cout << "Roll! Results ";
    roll1 = rand() % size + 1;
    roll2 = rand() % size + 1;
    cout << roll1 << " and " << roll2 << endl;
    array = makeArray(array, roll1, roll2);
  }

  return array;
}

int **makeArray(int** array, int r1, int r2)
{
  //Increment location in array by 1.
  array[r1-1][r2-1]++;
  return array;
}

void printArray(int** array, int size)
{
  cout << "Done rolling, here are the counts: " << endl;
  cout << "Here are the counts: " << endl;
  for (int i = 0; i < size; i++){
    for (int j = 0; j < size; j++){
      cout << array[i][j] << " ";
    }
    cout << endl;
  }
  return;
}
