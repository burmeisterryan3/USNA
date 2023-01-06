// MIDN Burmeister
// Professor Albing
// Lab 11 Heat Exchange

#include <stdio.h>
#include <stdlib.h>
#define FALSE 0
#define TRUE (!FALSE)

typedef struct {
  float** grid;
  int dim;
} Board;

Board* createBoards() {
  Board* boards = (Board*)calloc(2, sizeof(Board));
  int i, j, k;
  //Add 2 to length and height for collar region
  for (i = 0; i < 2; i++) {
    boards[i].dim = 200;
    int dim = boards[i].dim;
    boards[i].grid = (float**)calloc(dim+2, sizeof(float*));
    for (j = 0; j < dim+2; j++) {
      boards[i].grid[j] = (float*)calloc(dim+2, sizeof(float));
      for (k = 0; k < dim+2; k++) {
        if ((k == 0 || k == dim+1) && j != 0 && j != dim+1) {
          boards[i].grid[j][k] = 90;
          fprintf(stderr, "%f ", boards[i].grid[j][k]);
        } else {
          boards[i].grid[j][k] = 20;  
        }
      }
    }
    /* Uncomment when you need to put cooling rods in 
    // Place in the constant temperatures
    for (i = 662; i < 672; i++) {
      for (j = 662; j < 672; j++) {
        boards[i].grid[j] = 10;
      }
      for (j = 1129; j < 1139; j++) {
        boards[i].grid[j] = 10;
      }
    }
    // More constant temperatures
    for (i = 1129; i < 1139; i++) {
      for (j = 662; j < 672; j++) {
        boards[i].grid[j] = 10;
      }
      for (j = 1129; j < 1139; j++) {
        boards[i].grid[j] = 10;
      }
    }
    */
  }

  return boards;
}

// Generate output csv file
void csvGen(int iter, Board board) {
  int i, j, dim = board.dim;
  fprintf(stderr, "%f\n", board.grid[2001][2001]);
  char fname[40];
  snprintf(fname, 40, "log/output%05d.csv", iter);
  FILE* fp = fopen(fname, "wb");
  for (i = 1; i <= dim; i++) {
    for (j = 1; j < dim; j++) {
      fprintf(stderr, "%d\n", j);
      fprintf(fp, "%f, ", board.grid[i][j]);
    }
    fprintf(fp, "%f\n", board.grid[i][dim]);
  }
}

void runIteration(Board* boards, int iterations) {
  int i, j, k, dim = boards[0].dim;
  for (k = 0; k < iterations; k++) {
    for (i = 1; i <= dim; i++) {
      for (j = 1; j <= dim; j++){
        int sum = 0;
        sum += boards[0].grid[i-1][j];
        sum += boards[0].grid[i-1][j-1];
        sum += boards[0].grid[i-1][j+1];
        sum += boards[0].grid[i+1][j];
        sum += boards[0].grid[i+1][j-1];
        sum += boards[0].grid[i+1][j+1];
        sum += boards[0].grid[i][j-1];
        sum += boards[0].grid[i][j+1];
        sum += boards[0].grid[i][j];

        float average = sum/9.;
        boards[1].grid[i][j] = average;
      }
    } 
  
    //Swap pointers
    Board temp = boards[1];
    boards[1] = boards[0];
    boards[0] = temp;
  
    if (k % 5 == 0) {
      csvGen(k, boards[0]);
    } 
  }
} 

int main(int argc, char** argv) {
  if (argv[1] == NULL) return FALSE;

  int iterations;
  
  if (sscanf(argv[1], "%d", &iterations) == 0) {
    fprintf(stderr, "First argument is not an integer");
    return FALSE;
  }
  
  Board* boards = createBoards();
  runIteration(boards, iterations);
}
