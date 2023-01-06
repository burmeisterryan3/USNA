#include <stdio.h>
#include <stdlib.h>
#include <string.h> //for strlen() only!

void mystrcpy(char * from, char * to){
  int i;
  for (i = 0; i <= strlen(from); i++){
    to[i] = from[i];
  }
}

void reverse(char * str){
  char temp[strlen(str)+1];
  mystrcpy(str, temp);
  int j;
  for (j=0; j < strlen(str); j++){
    str[j] = temp[strlen(str)-1-j];
  }
}

int main(int argc, char * argv[]){

  char hello[] = "Hello World!";
  char hello_cp[100]; //declared another string of size 100

  mystrcpy(hello, hello_cp);
  
  printf("%s\n", hello_cp);
  
  printf("-----------\n");
  
  reverse(hello);
  printf("%s\n",hello_cp); //still prints "Hello World!"
  printf("%s\n", hello); //should print "!dlroW olleH"

}
