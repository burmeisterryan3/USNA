#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/errno.h>
#include <time.h>

#include "filesystem.h"

dir_t * myfs_mkdir(){
  dir_t* directory = (dir_t*)calloc(1, sizeof(dir_t));
  directory->flist = (file_t**)calloc(INIT_SIZE, sizeof(file_t*));
  directory->nfiles = 0;
  directory->size = INIT_SIZE;

  return directory;
}

void myfs_rmdir(dir_t * dir){
  int i;
  for (i = 0; i < dir->size; i++){
    if (dir->flist[i] != NULL){
      free(dir->flist[i]->fname);
      free(dir->flist[i]);
    }
  }
  free(dir->flist);
  free(dir);
  return;
}

int myfs_rm(dir_t * dir, char *fname){
  //Remove file with name fname... only loop over the number of files not size of array.
  int i;
  for (i = 0; i < dir->size; i++){
    //If "strings" are equal, then free memory associated with the char* and then the file_t*.
    if (dir->flist[i] != NULL && !strcmp(fname, dir->flist[i]->fname)){
      free(dir->flist[i]->fname);
      dir->flist[i]->fname = NULL;
      free(dir->flist[i]);
      //Zero out memory deallocated.
      dir->flist[i] = NULL;
      break;
    }
  }

  return -1;
}
 
  

void myfs_ls(dir_t * dir){
  //Print the file name and date last modified to the screen.
  int i;
  for (i = 0; i < dir->size; i++){
    if(dir->flist[i] != NULL){
      printf("%s \t %s", dir->flist[i]->fname, ctime(&dir->flist[i]->last));
    }
  }
}



file_t * myfs_touch(dir_t * dir, char *fname){
  //Check to see if file exists within array... if yes, then set "exists" to 1.
  int exists;
  for (exists = 0; exists < dir->size; exists++){
    if (dir->flist[exists] != NULL && !strcmp(dir->flist[exists]->fname, fname)){
      exists = 1;
      break;
    }
  }
  //Check to see if there is an empty pointer.  If there is,
  //make fname the name of the file and change the date last modified.
  int j;
  for (j = 0; j < dir->size; j++){
    //If file alread exists, just update time
    if (dir->flist[j] != NULL && !strcmp(dir->flist[j]->fname, fname)){
      dir->flist[j]->last = time(NULL);
      return dir->flist[j];
    }
    else if (dir->flist[j] == NULL && exists != 1){
      dir->flist[j] = (file_t*)calloc(1, sizeof(file_t));
      dir->flist[j]->fname = (char*)calloc(strlen(fname)+1, sizeof(char));
      //Copy string and account for string terminator.
      int l;
      for (l = 0; l <= strlen(fname); l++){
        dir->flist[j]->fname[l] = fname[l];
      }
      dir->flist[j]->last  = time(NULL);
      dir->nfiles++;
      return dir->flist[j];
    }
  }

  //Increase size of array for more files.
  dir->flist = (file_t**)realloc(dir->flist, (dir->size + INIT_SIZE)*sizeof(file_t*));
  //Zero new memory allocated.
  int k;
  for (k = dir->size; k < (dir->size + INIT_SIZE); k++){
    dir->flist[k] = NULL;
  }
  //Place file name in newly allocated memory position one after the old memory.
  dir->flist[dir->size] = (file_t*)calloc(1, sizeof(file_t));
  dir->flist[dir->size]->fname = (char*)calloc(strlen(fname)+1, sizeof(char));
  int i;
  for (i = 0; i <= strlen(fname); i++){
    dir->flist[dir->size]->fname[i] = fname[i];
  }
  dir->flist[dir->size]->last  = time(NULL);
  dir->size += INIT_SIZE;
  dir->nfiles++;

  //Return pointer of last file entered.
  return dir->flist[j]; 
}


void myfs_ls_sorttime(dir_t * dir){
  //TODO: Extra Credit

}
void myfs_ls_sortname(dir_t * dir){
  //TODO: Extra Credit
}
