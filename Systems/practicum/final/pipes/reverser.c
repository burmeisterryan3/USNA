#include <stdlib.h>
#include <stdio.h>

#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#define BUF_SIZE 4096

int main(){

  
  //////////////////////////////////////////////////////////////////////
  // DO NOT EDIT 

  int c_to_p[2]; //pipe for the child to write data to the parent
  int p_to_c[2]; //pipe for the parent to write data to the child
  pid_t cpid;    //store the process id of the child

  int i;         //iteration
  int n;         
  char tmp;
  char buf[BUF_SIZE];

  //open pipes
  pipe(c_to_p); //pipe from child to parent
  pipe(p_to_c); //pipe from parent to child
  //////////////////////////////////////////////////////////////////////

  if( (cpid = fork()) == 0){
    /*CHILD*/
    
    //TODO: properly widow pipes
    close(c_to_p[0]);
    close(p_to_c[1]);


    //////////////////////////////////////////////////////////////////////
    // DO NOT EDIT 
    while( (n = read(p_to_c[0], buf, BUF_SIZE-1)) > 0){
      buf[n] = '\0';
      printf("Child: %s\n", buf);
      
      for(i=0;i<n/2;i++){
	tmp = buf[i];
	buf[i] = buf[(n-1)-i];
	buf[(n-1)-i] = tmp;
      }

      if( write(c_to_p[1], buf, n) <=0){
	break;
      }
    }
    //////////////////////////////////////////////////////////////////////

    //TODO: properly widow pipes

    exit(0);

  }else if(cpid > 0){
    /*PARENT*/

    //TODO: properly widow pipes
    close(p_to_c[0]);
    close(c_to_p[1]);
    
    //////////////////////////////////////////////////////////////////////
    // DO NOT EDIT 
    while( (n = read(0, buf, BUF_SIZE-1)) > 0){
      buf[n] = '\0';

      
      //write to child the buffer
      if( write(p_to_c[1], buf, n) < 0){
	perror("pipe to child");
	break;
      }

      //read from child, should be reversed
      n = read(c_to_p[0], buf, BUF_SIZE-1);

      //error check
      if( n < 0){
	perror("pipe from child");
	break;
      }
      
      //closed pipe
      if(n == 0){
	break;
      }

      //print result to stdout
      buf[n] = '\0';
      printf("Parent: %s\n", buf);
    }
    /////////////////////////////////////////////////////////////////////

    //TODO: properly widow pipes
    close(p_to_c[1]);
    close(c_to_p[0]);

    //////////////////////////////////////////////////////////////////////
    // DO NOT EDIT BELOW

    wait(NULL);
    exit(0);

  }else{
    /*ERROR*/
    perror("fork");
    exit(1);
  }

  return 0;
}
