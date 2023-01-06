/*************************************************************
 * mywc.c
 *
 * NAME: MIDN Burmeister
 * ALPHA: 160786
 *
 *************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>


/* USAGE Information */
char USAGE[] = "mywc [OPTIONS] [file] [...]\n"
  "Print the number lines, words, and bytes of each file or from standard input\n"
  "\n"
  "All count information is written to standard out when no options are provided, and\n"
  "by default data is read from standard input if no file is provided. To read from \n"
  "standard input and a file, indicate standard input using '-'\n"
  "\n"
  "OPTIONS:\n"
  "\t -h \t print USAGE\n"
  "\t -l \t print line count\n"
  "\t -w \t print word count\n"
  "\t -c \t print byte count\n";

/**
 * Data type for mywc options
 **/
struct mywc_opt_t{
  int all; 
  int lines; //== 1 if counting lines
  int words; //== 1 if counting words
  int bytes; //== 1 if countying bytes
  int f_index; //index into argv for start of file arguments
};

/**
 * Data type for mywc results
 **/
struct mywc_res_t{
  int lines; //number of lines
  int words; //number of words
  int bytes; //number of bytes
};

/*Function Delceartions*/
void parse_args(int argc, char * argv[], struct mywc_opt_t * opts);
void print_opts(int argc, char * argv[], struct mywc_opt_t * opts);
void count(FILE * f, struct mywc_res_t * res);
void print_res( );


///////////////////////////////////////////////////////////////////////////////


/**
 * parse_args(int argc, char * argv[], struct mywc_opt_t * opts);
 * returns: index of remaining argv[]  values
 *
 * Set the opts structure with apropriate flags and return
 **/
void parse_args(int argc, char * argv[], struct mywc_opt_t * opts){
  int c;

  /*default settings*/
  opts->all = 1;
  opts->lines = 0;
  opts->bytes = 0;
  opts->words = 0;
  opts->f_index = -1;

  /*parse each flag*/
  while ((c = getopt(argc, argv, "wlch")) != -1){

    switch (c)
      {
      case 'h':
	printf("%s", USAGE);
	exit(0);
	break;
      case 'w':
	opts->all = 0;
	opts->words = 1;
	break;
      case 'l':
	opts->all = 0;
	opts->lines = 1;
	break;
      case 'c':
	opts->all = 0;
	opts->bytes = 1;
	break;
      default:
	fprintf(stderr, "mywc: Unknown option '%c' \n", c);
	exit(0);
	break;
      }
  }

  /*if all is set, set all to 1*/
  if (opts->all){
    opts->lines = 1;
    opts->words = 1;
    opts->bytes = 1;
  }
  
  /*store index of last arguemnt*/
  opts->f_index = optind;

  return;
}

/**
 * count(FILE * f, struct mywc_res_t * res)
 *
 * Count the number lines, words, and bytes in the file opned at the
 * file pointer f. Set the results in res.
 *
 **/
void count(FILE * f, struct mywc_res_t * res){
  int c, prev = 1;
  char backToChar;
  //Read from file until EOF is reached.
  while((c = fgetc(f)) != EOF){
    backToChar = c;
    res->bytes++;
    //Count the number of words... if space is preceeded by space do not count
    if(isspace(c) && (isspace(prev) == 0)){
      res->words++;
    }
    //Count the number of lines designated by new line character
    if(backToChar == '\n'){
      res->lines++;
    }
    prev = c;
  }
}

/**
 * print_res(                    )
 *
 * Print the output for a results. The order of the output is always
 * prefered lines, words, bytes and then file name, dependent on which
 * options are provided. All results are tab deliminated with a
 * leadding tab. 
 *
 **/
void print_res(struct mywc_opt_t opts, struct mywc_res_t res, struct mywc_res_t total, char * fname, int counter, int argc){
  //print if the condition is satisfied based on the command given by user.
  if (opts.lines == 1){
    printf("%d \t", res.lines);
  }
  if (opts.words == 1){
    printf("%d \t", res.words);
  }
  if (opts.bytes == 1){
    printf("%d \t", res.bytes);
  }
  printf("%s\n", fname);
  //Must satisfy both to print total:
  //1- counter cannot be 0 (i.e. one file from stdin)
  //2- counter must be at a value equal one less than the number of argumnets but should not include a situation where
  //   one file is entered (counter begins at value equivalent to location of first file listed on command line)
  if ((counter == argc-1 && (counter != 1)) && counter != 0){
    if (opts.lines == 1){
      printf("%d \t", total.lines);
    }
    if (opts.words == 1){
      printf("%d \t", total.words);
    }
    if (opts.bytes == 1){
      printf("%d \t", total.bytes);
    }
    printf("TOTAL\n");
  }
}

int main( int argc, char *argv[] ){


  /* DEFINE MORE VARIABLES HERE*/
  struct mywc_opt_t opts; //options structure
  struct mywc_res_t res, total; //results struct and total
  int i; //iterator
  char * fname; //filename
  FILE * file; //file pointer

  //Initialize res and total
  res.lines = total.lines = 0;
  res.words = total.words = 0;
  res.bytes = total.bytes = 0;

  parse_args(argc, argv, &opts); // parse the options

  //Check to see if just reading from stdin.
  if (argv[1] == NULL){
    count(stdin, &res);
    total.lines += res.lines;
    total.words += res.words;
    total.bytes += res.bytes;
    print_res(opts, res, total, fname, i, argc);
  }
  //Loop starting at the first index of the files.
  for (i = opts.f_index; i < argc; i++){
    fname = argv[i];
    //Reset values for each file.
    res.lines = res.words = res.bytes = 0;
    if (strcmp(fname, "-") == 0){
      count(stdin, &res);
      total.lines += res.lines;
      total.words += res.words;
      total.bytes += res.bytes;
      print_res(opts, res, total, fname, i, argc);
    }
    else {
      file = fopen(fname, "r");
      //Ensure that file can be found and read from.
      if (file == NULL){
        fprintf(stderr, "%s: File not found: %s\n", argv[0], fname);
      }
      //Do this if file and can be read from.
      else {
        count(file, &res);
        total.lines += res.lines;
        total.words += res.words;
        total.bytes += res.bytes;
        print_res(opts, res, total, fname, i, argc);
        fclose(file);
      }
    }
  }
  return 0;
}
