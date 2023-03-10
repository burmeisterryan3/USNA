#include <stdio.h>
#include <stdlib.h>

#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>


#include <signal.h>
#include <sys/signal.h>

pid_t c_pid[4]; //array of 4 children pid's

void child_handler(int signum, siginfo_t * siginfo, void * context){
  int i;
  pid_t pid;

  //TODO: Complete the handler to print info about the child's state
  //
  //  You should investigate the following fields of the siginfo structure
  //  siginfo->si_pid    : pid of the signaling child
  //  siginfo->si_code   : why the child signaled SIGINFO
  //  signifo->si_status : status of a child
  //
  //  Read the man page for sigaction for more detail
  pid = siginfo->si_pid;

  //siginfo->si_stats==16 refers to the exit(16)
  if (siginfo->si_code == CLD_EXITED && siginfo->si_status != 16){
    for (i = 0; i < 4; i++){
      if (pid == c_pid[i]){
        printf("Parent: Child %d: %d terminated and exited with status %d\n", i, pid, siginfo->si_status);
      }
    }
  }
  else if(siginfo->si_code == CLD_DUMPED || siginfo->si_code == CLD_KILLED || siginfo->si_status == 16){
    for (i = 0; i < 4; i++){
      if (pid == c_pid[i]){
        printf("Parent: Child %d: %d terminated and exited due to signal no. %d\n", i, pid, siginfo->si_status);
      }
    }
  }
  else if(siginfo->si_code == CLD_STOPPED){
    for (i = 0; i<4; i++){
      if (pid == c_pid[i]){     
        printf("Parent: Child %d: %d stopped, continuing ...\n", i, pid);
        kill(pid, SIGCONT);
      }
    }
  }
  else if(siginfo->si_code == CLD_CONTINUED){
    for (i = 0; i < 4; i++){
      if (pid == c_pid[i]){
        printf("Parent: Child %d: %d just continued\n", i, pid);
      }
    }
  }
  fflush(stdout);
}

int main(){


  pid_t pid;
  int i;

  setvbuf(stdout, NULL, _IONBF, 0);

  struct sigaction action;

  //TODO: Setup the sigaction
  //  
  //  (hint: you will need a restart, siginfo, and nodefer flag. Use and ORing)
  action.sa_handler = child_handler;
  action.sa_flags = SA_RESTART | SA_SIGINFO | SA_NODEFER;

  sigaction(SIGCHLD, &action, NULL);

  for(i = 0; i < 4; i ++){

    c_pid[i] = fork();

    if (c_pid[i] == 0){
      /* Child */
      pid = getpid();

      switch(i){
      case 0:
	//Child 0: The wise son

	printf("Child 0: %d: Sending myself SIGSTOP\n", pid);
	fflush(stdout);
	kill(getpid(), SIGSTOP);
	
	exit(16); //exit 
	break;
      case 1:
	//Child 1: The simple son
	printf("Child 1: %d: I'm going to dereference a NULL pointer \n", pid);
	fflush(stdout);
	int * p = NULL; 
	*p = 10; //dereferencing NULL
	  
	exit(1);//just in case
	break;
      case 2:
	//Child 2: The wicked son
	printf("Child 2: %d: Scheduling an alarm for 1 second\n", pid);
	fflush(stdout);
	alarm(1);
	pause();

	exit(1);//just in case
	break;
      case 3:
	//Child 3: The son who doesn't know how to ask question
	printf("Child 3: %d: Ummm .... exit 0?\n", pid);
	fflush(stdout);
	exit(0); //success?
	break;
      }
      
    }
  } 
  /*Parent*/
  
  //Loop unitl all children terminate
  while(wait(NULL) > 0);

  //DO NOT PLACE ANY CODE HERE!!!!
  // ALL LOGIC GOES IN HANDLER

  return 0;
}
