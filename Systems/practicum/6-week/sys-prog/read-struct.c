#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>


/************************************************************
 *
 * Instructions:
 *   - Complete the program below where the TODO
 *   - Your program should not segfault or have memory errors
 *   - Your program output should exactly match the instrucitons
 *
 * USAGE: ./read_struct file-db
 *    output will be ach entry formatted like below
 *
 *  printf("Name: %s, %s\n", be.last_name, be.first_name);
 *  printf("Bal: %d\n", be.balance);
 *  printf("Crd: %d\n", be.credit);
 *
 ************************************************************/



//DO NOT EDIT the struct  
struct bank_entry{

  char first_name[64]; //string to store first name, max length 63+1 for NULL
  char last_name[64]; //string to store last name, max lenght 63+1 for NULL

  unsigned int balance; //account balance
  unsigned int credit; //allowable credit
};


int main(int argc, char * argv[]){

  int fd; //file descriptor number
  int check = 1;
  struct bank_entry be; //stores a bank entry

  if(argc < 2){
    fprintf(stderr, "ERROR: Require File\n");
    return 1;
  }

  fd = open(argv[1], O_RDONLY);
  while (check != 0){
    check = read(fd, &be, sizeof(be));
    write(1, &be, sizeof(be));
  }

  close(fd);

  return 0;
}
