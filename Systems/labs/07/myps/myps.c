#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>

char * clean_comm(char * comm){
  char *p;
  for(p=comm+1;*p;p++){
    if ( *p == ')' ){
      *p = '\0';
      break;
    }
  }
  return comm+1;
}

int main(int argc, char * argv[]){

  pid_t pid, ppid;
  char comm[1024];
  char state;
  
  int i;
  FILE * stat_f;
  char proc_path[1024];

  if (argc < 2){
    fprintf(stderr, "ERROR: Require a pid to print the satus of\n");
    _exit(1);
  }


  printf("PID\tCOMM\tSTATE\tPPID\n");

  for(i = 1 ; i < argc; i ++){

    if (snprintf(proc_path, 1024, "/proc/%s/stat", argv[i]) < 0){
      perror(argv[i]);
    }

    if ((stat_f = fopen(proc_path, "r")) == NULL){
      fprintf(stderr, "ERROR: Invalid pid %s\n", argv[i]);
    }
    else {
      fscanf(stat_f, "%d %s %c %d", &pid, comm, &state, &ppid);
      fclose(stat_f);
    }

    printf("%d\t%s\t%c\t%d\n", pid, clean_comm(comm), state, ppid);


  }

}
