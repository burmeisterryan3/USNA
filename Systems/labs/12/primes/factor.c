#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <pthread.h>

char USAGE[]="./factor n\n"
  "\n"
  " Find a factor for the number n using threads or print that its prime\n";



#define MAX_THREADS 4


int       running[MAX_THREADS];     //track which threads are running,
                                    // 0 for not 1 for yes

pthread_t threads[MAX_THREADS];     //track thread information

//struct to pass arguments to factor
struct factor_args{
  long start; //start of range
  long end;   //end with this factor
  long n;     //number to factor
  int i;      //thread number
};


//The factor function
void * factor(void * args){
  //cast the factor
  struct factor_args * fargs = (struct factor_args *) args;
  
  long* result = (long*)calloc(1, sizeof(long));
  *result= -1;
  //TODO: complete the factor functoin
  // 
  // Check for a factor between start and end, return it if found,
  // otherwise return -1
  //
  // HINT: Be sure to return a result allocated on the heap!
  int i;
  for (i = fargs->start; i <= fargs->end; i++){
    if (fargs->n % i == 0){
      *result = i;
      break;
    }
  }
  
  running[fargs->i]=0; //set not running
  return (void *) result;
}

int main(int argc, char * argv[]){

  long n;        //the number to factor
  long start;    //the start factor for dividing work
  long span;     //the span of factors for dividing work
  long* result = (long*)calloc(1, sizeof(long));  //for retrieving results from a thread
  int i;         //iterator

  struct factor_args * fargs = (struct factor_args*)calloc(4, sizeof(fargs)); //pointer for allocator factor aguments

  //check for right number of arguments
  if(argc < 2){
    fprintf(stderr, "ERROR: require n\n%s",USAGE);
    exit(1);
  }

  //convert to a long
  n = atol(argv[1]);
  
  
  //check for rigth format
  if( n == 0){
    fprintf(stderr, "ERROR: invalid value for n\n%s",USAGE);
    exit(1);

  }
  
  //initialize all threads to not running
  for(i=0 ; i < MAX_THREADS; i++){
    running[i] = 0;
  }


  //TODO: Complete the program
  //
  // outline:
  // (1) Divide the work up so that each thread checks a different set
  // of factors: e.g.,
  //   [2,sqrt(n)/MAX_THREADS],
  //   (sqrt(n)/MAX_THREADS,2*sqrt(n)/MAX_THREADS]
  //   (2*sqrt(n)/MAX_THREADS,3*sqrt(n)/MAX_THREADS]
  //   etc.
  // (2) spawn each thread based
  // (3) Check for threads running state
  // (4) if not running, join and see if they found a factor
  // (5) if factor found, then print to screen and exit
  // (6) if no threads are running and no factor found, report that
  //     the number is prime and exit
  //
  // HINT: Don't forget to set thread running when you start it with running[i]=1
  // HINT: You'll need to write a loop to continually check the running state of threads
  fargs->n = n;
  span = sqrt(n)/MAX_THREADS + 1;
  for(i = 0; i < MAX_THREADS; i++){
    if (i == 0){
      fargs->start = 2;
    }else{
      fargs->start = i*span + 1;
    }
    fargs->end = (i+1)*span;
    fargs->i = i;
    pthread_create(&threads[i], NULL, factor, fargs);
    running[i] = 1;
  }
  
  for (i = 0; i < MAX_THREADS; i++){
    pthread_join(threads[i], (void**)&result);
    if (*result != -1) {
      printf("Factor: %ld\n", *result);
      break;
    }
  }
  
  if (*result == -1) {
    printf("Prime: %ld\n", n);
  }
  
  free(result);

  return 0;
}


