#include <dirent.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <bsd/string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/errno.h>
#include <time.h>
#include <pwd.h>
#include <pwd.h>
#include <grp.h>


int main(int argc, char * argv[]){

  //directory navigation variables
  DIR * dir_p;
  struct dirent * entry;

  //listing variables
  struct stat filestat;
  struct passwd * pwd;
  struct group * grp;
  //Use 12 bc mode strings are always 11 chars long (+1 for NULL)
  char mode_str[12];

  //open the directory to read each of the entries
  if( (dir_p = opendir(".")) == NULL){
    fprintf(stderr, "ERROR: diropen: %s\n", strerror(errno));
    return 1;
  }

  //foreach entry in the directory
  while( NULL != (entry = readdir(dir_p)) ){

    //Check to see if you can stat file.
    if (stat(entry->d_name, &filestat) < 0){
      perror(entry->d_name);
      return 1;
    }

    //Print permissions
    strmode(filestat.st_mode, mode_str);
    printf("%s\t", mode_str);

    //Print name of the file
    printf("%s\t", entry->d_name);

    //Get & print user id
    if ((pwd = getpwuid(filestat.st_uid)) == 0){
      perror(entry->d_name);
      return 2; 
    }

    printf("%s\t", pwd->pw_name);

    //Get & print group id
    if ((grp = getgrgid(filestat.st_gid)) == 0){
      perror(entry->d_name);
      return 3;
    }

    printf("%s\t", grp->gr_name);

    //Print size of file
    printf("%ld\t", filestat.st_size);

    //Print time of last modification
    printf("%s", ctime(&filestat.st_mtime));
  }

  //close the directory
  closedir(dir_p);
}
