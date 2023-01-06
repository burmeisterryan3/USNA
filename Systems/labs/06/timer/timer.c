#include <stdio.h>
#include <stdlib.h>

#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <sys/types.h>

/**
 * timeval_subtract : subtract timeval x - y and store result in result
 **/
int timeval_subtract (struct timeval *result, struct timeval *x, struct timeval *y) {
  /* Perform the carry for the later subtraction by updating y. */
  if (x->tv_usec < y->tv_usec) {
    int nsec = (y->tv_usec - x->tv_usec) / 1000000 + 1;
    y->tv_usec -= 1000000 * nsec;
    y->tv_sec += nsec;
  }
  if (x->tv_usec - y->tv_usec > 1000000) {
    int nsec = (x->tv_usec - y->tv_usec) / 1000000;
    y->tv_usec += 1000000 * nsec;
    y->tv_sec -= nsec;
  }
     
  /* Compute the time remaining to wait.
     tv_usec is certainly positive. */
  result->tv_sec = x->tv_sec - y->tv_sec;
  result->tv_usec = x->tv_usec - y->tv_usec;
     
  /* Return 1 if result is negative. */
  return x->tv_sec < y->tv_sec;
}

int main(int argc, char * argv[]){

  int status;
  pid_t c_pid;
  struct timeval start, end, diff;

  //Start time
  if (gettimeofday(&start, NULL) < 0){
    perror(argv[0]);
  }

  c_pid = fork();
  if (c_pid == 0){
    //Execute command
    execvp(argv[1], argv+1);
    
    perror("execvp failed");
  }
  else if(c_pid > 0){
    if (wait(&status) < 0){
      perror("wait");
    }
    //End time
    if(gettimeofday(&end, NULL) <  0){
      perror(argv[0]);
    }
    if (timeval_subtract(&diff, &end, &start) == 1){
      fprintf(stderr, "Error: Negative time difference between start and end of process");
      _exit(1);
    }
      
    printf("Run Time: %ld.%04ld (s)\n", diff.tv_sec, diff.tv_usec/1000);
  }

  return 0;
}

