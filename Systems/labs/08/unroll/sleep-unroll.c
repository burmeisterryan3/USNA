#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <readline/readline.h>
#include <readline/history.h>

#include <unistd.h>
#include <sys/wait.h>
#include <signal.h>

#define MAX_ARGS 128

/************************************************************
 * sleep-unroll.c
 *
 * Unroll a pipeline of sleep calls, placing all children in their own
 * process group. 
 *
 * Sample Usage:
 *  $ ./sleep_unroll "sleep 1"   
 *  $ ./sleep_unroll "sleep 1 | sleep 2"
 *  $ ./sleep_unrill "sleep 2 | sleep 3 | sleep 1"
 *
 ************************************************************/


int main(int argc, char * argv[]){

  pid_t cpid;
  pid_t cpgid=0; 

  int i, status;
  char * cmd_argv[MAX_ARGS];
  char * line, * tok;
   
  //split the command based on | into lines
  line = strtok(argv[1], "|");
  do{
    //TODO: Complete the unrolling
    //
    //   General outline:
    //    (fork)
    //      + Child: setpgid of itself
    //      + Child: parse command and exec
    //      + Parent: setpgid of child
    //
    //   Notes:
    //     - All children should be in the same process group
    //     - Use the pid of the *first* child as the pgid
    //     - Take advantage of setpgid(cpid,0) and setpgid(0,0)
    //       properties to assist logic
    //     - You can create orphaned process groups, be sure to kill
    //     - those with killall

    cpid = fork();
    
    if (cpid == 0){
      if (cpgid == 0){
        setpgrp();
        //cpgid gets the first child process id
        cpgid = getpgrp();
      }
      else{
        //If not the first child, set pgid to first child process id
        setpgid(0, cpgid);
      }

      tok = strtok(line, " ");
      cmd_argv[0] = tok;
      i = 1;
      while ((tok = strtok(NULL, " ")) != NULL  && i < MAX_ARGS){
        cmd_argv[i] = tok;
        i++;
      }

      cmd_argv[i] = NULL;

      execvp(cmd_argv[0], cmd_argv);

      perror("exec");
      _exit(1);
    }

    if (cpid > 0){
      //Child process group has not yet been set
      if (cpgid == 0){
        setpgid(cpid, cpid);
        //cpgid gets the first child process id
        cpgid = cpid;
      }
      //Set child process group to first child process id
      else{
        setpgid(cpid, cpgid);
      }
    }

  }while( (line = strtok(NULL, "|")) != NULL);

  //wait for all children in a process group
  while(waitpid(-cpgid, &status, 0) > 0); //DO NOT EDIT
	 
  //SUCCESS!
  return 0;
}
