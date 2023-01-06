#include <stdio.h>
#include <stdlib.h>


int main(int argc, char * argv[]){
  
  int c;
  FILE * input;
  
  input = fopen("saved.out", "r");
  if (input == NULL){
    fprintf(stderr, "ERROR:  Cannot open output file for reading\n");
    exit(1);
  }

  while ( (c = fgetc(input)) != EOF){
    fputc(c, stdout);
  }
  
  fclose(input);
    
  return 0;
}
