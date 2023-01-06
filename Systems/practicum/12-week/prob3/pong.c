#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <sys/signal.h>

//TODO: Complete the signal handler
//
//   * The arguments to the signal handler function depend on the type
//     of handler, e.g., a basic handler or one that requires a
//     siginfo

void handler(int signum, siginfo_t * siginfo, void * context ){
  pid_t pid = siginfo->si_pid;
  printf("pong %d\n", signum/*siginfo->si_code*/);
  signal(signum, SIG_IGN);
  kill(pid, signum);

  exit(0); //DO NOT EDIT
}

int main(int argc, char * argv[]){
  
  //DO NOT EDIT///////////////////////////////////
  pid_t cpid; //child pid
  char * cmd_ping[] = {"./ping", NULL}; //cmd_argv
  int i; //iterator
  ////////////////////////////////////////////////
  
  //TODO: Setup signal handler
  //
  //   * Will need to handle potentiall all signals, except SIGCHLD,
  //     SIGKILL, and SIGSTOP
  //
  //   * Will probably need to use sigaction() insted of signal() to
  //     establish the handler, may want siginfo data
  
  struct sigaction action;
  action.sa_handler = handler;
  action.sa_flags = SA_RESTART | SA_SIGINFO | SA_NODEFER;

  sigaction(4, &action, NULL);
  sigaction(1, &action, NULL);
  sigaction(2, &action, NULL);
  sigaction(31, &action, NULL);
  sigaction(6, &action, NULL);
  sigaction(13, &action, NULL);
  sigaction(15, &action, NULL);
  sigaction(16, &action, NULL);
  sigaction(7, &action, NULL);
  sigaction(24, &action, NULL);
  sigaction(29, &action, NULL);
  sigaction(26, &action, NULL);
  sigaction(14, &action, NULL);
  sigaction(12, &action, NULL);
  sigaction(27, &action, NULL);
  sigaction(25, &action, NULL);
  sigaction(10, &action, NULL);
  sigaction(8, &action, NULL);
  sigaction(18, &action, NULL);
  sigaction(23, &action, NULL);
  sigaction(11, &action, NULL); 
  sigaction(28, &action, NULL);
  sigaction(30, &action, NULL);
  sigaction(3, &action, NULL);
  sigaction(5, &action, NULL);
  sigaction(21, &action, NULL);
  sigaction(20, &action, NULL);
  sigaction(22, &action, NULL);
  sigaction(17, &action, NULL);
  sigaction(19, &action, NULL);
  sigaction(22, &action, NULL);
  sigaction(32, &action, NULL);
  

  //DO NOT EDIT///////////////////////
  if( (cpid = fork()) == 0 ){
    /* CHILD */
    execvp(cmd_ping[0], cmd_ping);
    perror("exec");
    exit(2);
  }else{
    /* PARENT */
    wait(NULL);
  }
  return 0;
  //////////////////////////////////
}
