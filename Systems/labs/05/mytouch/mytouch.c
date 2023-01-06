#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

int main(int argc, char *argv[]){

  //variable decleration
  struct stat sb;
  struct timeval tv[2];

  //Check to see if file can be 'stat'ed
  if (stat(argv[1], &sb) < 0){
    perror(argv[1]);
    return 1;
  }

  //Print time last modified
  printf("Last Modified:\t%s", ctime(&sb.st_mtime));

  //Set new access time
  if (gettimeofday(&tv[0], NULL) < 0){
    perror(argv[0]);
    return 1;
  }

  //Set new modification time
  tv[1] = tv[0];

  //Does this need to go before/after the gettimeofday's
  if (utimes(argv[1], tv) < 0){
    perror(argv[0]);
    return 1;
  }

  //If I remove it will print the same time twice... why?
  stat(argv[1], &sb);

  printf("New Modified:\t%s", ctime(&sb.st_mtime));

  return 0;
}

