#include <stdio.h>
#include <stdlib.h>


int main(int argc, char * argv[]){
  
  int c;
  FILE * output;
  
  char outputFileName[] = "saved.out";

  //Open file and check to see if it can be written to.
  output = fopen(outputFileName, "w");
  if (output == NULL){
    fprintf(stderr, "ERROR:  %s cannot be written to\n", outputFileName);
    exit(1);
  }

  while ((c = fgetc(stdin)) != EOF){
    fputc(c, output);
  }

  //Close file stream.
  fclose(output);

  return 0;
}
