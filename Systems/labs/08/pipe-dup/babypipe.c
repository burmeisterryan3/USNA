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

/***************************************************************************************
 * babypipe.c
 *
 * Will unroll and execute the pipeline with pipe() and dup()
 *
 * Three pipeline options to compile, using make
 *  - make 0  : cat /etc/passwd | cut -d : -f 7 | sort | uniq 
 *  - make 1  : cat sample-db.csv | cut -d , -f 8 | sort | uniq | wc -l
 *  - make 2  : cat sample-db.csv | cut -d , -f 10 | cut -d - -f 1 | sort | uniq |wc -l
 *  - make 3  : cat | cut -d , -f 10 | cut -d - -f 1 | sort | uniq |wc -l 
 ****************************************************************************************/
int main(int argc, char * argv[]){


#ifndef PIPE
  // make
  // make 0
  // cat /etc/passwd | cut -d : -f 7 | sort | uniq ;
  char * pipeline[5][6] = {
    { "cat", "/etc/passwd", NULL},    
    { "cut", "-d", ":", "-f" , "7", NULL},
    { "sort", NULL},
    { "uniq", NULL},
    {NULL},
  };
#elif PIPE == 1
  // make 1
  // cat sample-db.csv | cut -d , -f 8 | sort | uniq | wc -l;
  char * pipeline[6][6] = {
    { "cat", "sample-db.csv", NULL},    
    { "cut", "-d", ",", "-f" , "8", NULL},
    { "sort", NULL},
    { "uniq", NULL},
    { "wc", "-l", NULL},
    {NULL},
  };
#elif PIPE == 2
  // make 2
  // cat sample-db.csv | cut -d , -f 10 | cut -d - -f 1 | sort | uniq |wc -l;
  char * pipeline[7][6] = {
    { "cat", "sample-db.csv", NULL},    
    { "cut", "-d", ",", "-f" , "10", NULL},
    { "cut", "-d", "-", "-f" , "1", NULL},
    { "sort", NULL},
    { "uniq", NULL},
    { "wc", "-l", NULL},
    {NULL},
  };
#elif PIPE == 3
  // make 2
  // cat | cut -d , -f 10 | cut -d - -f 1 | sort | uniq |wc -l;
  char * pipeline[7][6] = {
    { "cat", NULL},    
    { "cut", "-d", ",", "-f" , "10", NULL},
    { "cut", "-d", "-", "-f" , "1", NULL},
    { "sort", NULL},
    { "uniq", NULL},
    { "wc", "-l", NULL},
    {NULL},
  };

#else
  char * pipeline[1][1] = {{NULL}};
#endif


  pid_t cpid;
  int i, status;

  int cur_pipe[2];
  int last_pipe[2];

  
  //iterate through pipeline
  for(i=0; pipeline[i][0] != NULL; i++){
  
    //TODO: Setup the pipeline:
    //
    //   General outline:
    //     - create cur_pipe in parent
    //     - fork
    //       + child: duplicate read from last_pipe
    //       + child: duplicate write to cur_pipe
    //       + parent: shift cur_pipe to last pipe
    //       + parent: widow pipe by closing last_pipe[1]
    //    - Parent: wait for all children to close
    //
    //  Notes: 
    //   - Will hange if forget to widow pipe
    //   - Check for ends of pipeline. The front of the pipeline, doe
    //     not duplicate stdin The end of pipeline, do not duplicate stdout
    
    if (pipe(cur_pipe) < 0){
      perror("pipe");
    }

    cpid = fork();

    if (cpid == 0){
      if (i == 0){
        close(cur_pipe[0]);
        close(1);
        dup2(cur_pipe[1], 1);
      }
      else if (pipeline[i+1][0] == NULL){
        close(last_pipe[1]);
        close(cur_pipe[0]);
        dup2(last_pipe[0], 0);
      }
      else{
        close(cur_pipe[0]);
        dup2(last_pipe[0], 0);
        close(last_pipe[1]);
        dup2(cur_pipe[1], 1);
      }

      execvp(pipeline[i][0], pipeline[i]);
      perror("exec");
      _exit(1);
    }
    else if(cpid > 0){
      last_pipe[0] = cur_pipe[0];
      last_pipe[1] = cur_pipe[1];

      close(last_pipe[1]);
    }
  }  

  //wait for all children;
  while( wait(&status) > 0) ;
  
  return 0;
}
