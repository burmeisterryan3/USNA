#include <stdio.h>
#include <stdlib.h>

struct pair{
  int first;
  int second;
};


/*Function Decleration*/
void swap(int *, int*);
void swap_pair(struct pair *);


//TODO: Write swap and swap_pair functions
void swap(int * a, int* b) {
  int temp = *a;
  *a = *b;
  *b = temp;
}
void swap_pair(struct pair * p) {
  int temp = p->first;
  p->first = p->second;
  p->second = temp;
}



int main(int argc, char * argv[]){

  int a,b;
  struct pair p;

  a = 10;
  b = 20;

  printf("BEFORE SWAP: a: %d b: %d\n", a,b);
  swap(&a,&b);
  printf("AFTER SWAP: a: %d b: %d\n", a,b);

  printf("----------------------\n");

  p.first = 50;
  p.second = 60;

  printf("BEFORE SWAP: p.first: %d p.second: %d\n", p.first,p.second);
  swap_pair(&p);
  printf("AFTER SWAP: p.first: %d p.second: %d\n", p.first,p.second);

}
