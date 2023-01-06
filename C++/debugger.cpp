#include <iostream>

using namespace std;

void fillArray(int *array, int size, int value);

int main() {
  // Make an array
  int* A = new int[10];

  // Fill the array with the value 49999
  fillArray(A, 4999999, 10);

  return 0;
}

// Place 'value' into every cell in the array
void fillArray(int *array, int size, int value) {
  for (int i=0; i<size; i++) {
    array[i] = value;
  }

}
