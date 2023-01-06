#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/errno.h>
#include <fcntl.h>

#define BUFFSIZE 4096

int main(int argc, char * argv[]){
  
  //Variable Decleartion
  char buf[BUFFSIZE];
  int n, fd_src, fd_dest;
  struct stat fs;

  if( stat(argv[1], &fs) < 0){
    perror(argv[1]);
    return 1;
  }

  //Check to see if there is an appropriate path entered into command line.
  if (argc < 2){
    fprintf(stderr, "Error: Require a path\n");
    return 2;
  }

  //Check to see if source file is a directory.
  if (S_ISDIR(fs.st_mode)){
    fprintf(stderr, "%s: %s: Is a directory\n", argv[0], argv[1]);
    return 1;
  }
  
  if ((fd_src = open(argv[1], O_RDONLY)) < 0){
    perror(argv[1]);
    return 1;
  }

  if (stat(argv[2], &fs) < 0){
    perror(argv[2]);
    return 1;
  }

  if ((fd_dest = open(argv[2], O_WRONLY | O_CREAT | O_TRUNC, fs.st_mode)) < 0){
    perror(argv[2]);
    return 1;
  }

  while ((n = read(fd_src, buf, BUFFSIZE)) > 0){
    if (write(fd_dest, buf, n) != n){
      perror(argv[0]);
    }
  }

  close(fd_dest);
  close(fd_src);

  return 0;
}
