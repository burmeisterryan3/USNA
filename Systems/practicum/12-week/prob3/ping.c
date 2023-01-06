#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <signal.h>
#include <time.h>


//DO NOT EDIT THIS FILE!

void handler(int signum){
  
  printf("ACK %d\n",signum);

}

int main(int argc, char * argv[]){

  int signum;
  
  srand(time(NULL));

  do{
    signum = rand() % 32;
  }while(signum == 0 || signum == SIGSTOP || signum == SIGKILL || signum == SIGCHLD);
  
  signal(signum, handler);
  
  printf("ping %d\n",signum);
  fflush(stdout);
  kill(getppid(), signum);

  pause();
}