#include <stdio.h>
#include <stdlib.h>


int main(int argc, char * argv[]){
  
  int c;
  FILE * from, * to;

  //Check to see if there are two arguments after the command.
  if (argc < 3){
    fprintf(stderr, "ERROR:  Invalid number of arguments\n");
    exit(1);
  }

  //Open file to be read from and ensure that it can be read from.
  from = fopen(argv[1], "r");
  if (from == NULL){
    fprintf(stderr, "ERROR:  Cannot open input file for reading\n");
    exit(2);
  }

  //Open file to be written to and ensure that it can be output to.
  to = fopen(argv[2],"w");
  if (to == NULL){
    fprintf(stderr, "ERROR:  Cannot open output file for writing\n");
    exit(3);
  }

  //Write contents of "from" file to "to" file.
  while ((c = fgetc(from)) != EOF){
    fputc(c, to);
  }

  //Close the file streams.
  fclose(to);
  fclose(from);

  return 0;
}
