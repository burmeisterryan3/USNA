#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char * argv[]){
  
  int c,i;
  FILE * file;

  //Ensure that mycat has at least one file to write
  if (argc < 2){
    while ((c = fgetc(stdin)) != EOF){
        fputc(c, stdout);
    }
  }
  else {
    //run loop for the number of arguments
    for (i = 1; i < argc; i++){
      //check to see if argument is a "-" and if so read from stdin
      if (strcmp(argv[i], "-") == 0){
        while((c = fgetc(stdin)) != EOF){
          fputc(c, stdout);
        }
      }
      //if not a "-" read from file
      else{
        file = fopen(argv[i], "r");
        if (file == NULL){
          fprintf(stderr, "ERROR:  Cannot open file \'%s\' for reading\n", argv[i]);
        }
        else {
          while ((c = fgetc(file)) != EOF){
            fputc(c, stdout);
          }
          fclose(file);
        }
      }
    }
  }
  return 0;
}
