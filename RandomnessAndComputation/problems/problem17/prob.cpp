#include <stdio.h>
#include <stdlib.h>
#include <cmath>

int getSeed() { 
  int byte_count = 4;
  unsigned int data;
  FILE* fp;
  fp = fopen("/dev/urandom", "r");
  fread((void*)(&data), 1, byte_count, fp);
  fclose(fp);
  return abs(data);
}

int randomConvert(int x) {
  int m1 = int(pow(2, 31)-1);
  int m2 = 6;
  int c = m1/m2 * m2;
  if (x >= c) {
    return -1;
  } else {
    return x % m2;
  } 
}

int main() {
  srand(getSeed());

  int rand1 = rand();
  int rand2 = rand();

  do {
    rand1 = randomConvert(rand1);
  } while (rand1 == -1);

  do {
    rand2 = randomConvert(rand2);
  } while (rand2 == -1);

  printf("%d\n%d\n", rand1, rand2);
  return 0;
}
