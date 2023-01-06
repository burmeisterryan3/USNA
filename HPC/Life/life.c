/**
 * MIDN Burmeister (m160786) HPC Professor Albing
 * Game of Life
 */
#include <stdlib.h>
#include <stdio.h>
#define FALSE 0
#define TRUE !(FALSE)

/**
 * Define a struct for a board
 * The length and height correspond to the dimensions
 * of the board
 */
typedef struct {
  int** grid;
  int length;
  int height;
} Board;

/**
 * Allocate memory for two boards for the game
 * The first board will be used for printing
 * The second board will be used for storing the next
 * generation when determing what that generation should
 * look like.
 */
Board* createBoards(int length, int height, FILE* fp) {
  //Allocate memory for board
  Board* boards = (Board*)calloc(2, sizeof(Board));
  int i, j;
  //Add 2 to length and height for collar region
  for (i = 0; i < 2; i++) {
    boards[i].length = length;
    boards[i].height = height;
    boards[i].grid = (int**)calloc(length+2, sizeof(int*));
    for (j = 0; j < length+2; j++) {
      boards[i].grid[j] = (int*)calloc(height+2, sizeof(int));
    }
  }

  //Place initial values on the board
  int x, y;
  while (fscanf(fp, "%d %d", &x, &y) != EOF) {
    boards[0].grid[x][y] = 1;
  }

  return boards;
}

/**
 * Read the file and initialize boards
 */
Board* readFile(FILE* fp) {
  //Obtain the size of the board
  int length, height;
  fscanf(fp, "%d %d", &length, &height);

  //Create board
  Board* boards = createBoards(length, height, fp);

  //Close the file and return the board to main
  fclose(fp);
  return boards;
}

/**
 * Make halo region around board to ensure edges
 * are also surrounded by 8 cells
 */
void copyCollarRegion(Board* board){
  board->grid[0] = board->grid[board->length];
  board->grid[board->length+1] = board->grid[1];
  int i;
  for (i = 1; i <= board->height; i++){
    board->grid[i][0] = board->grid[i][board->length];
    board->grid[i][board->length+1] = board->grid[i][1];
  }
}

/**
 * Determine the next generation by checking the
 * surrounding cells for each cell on the board
 */
void playGeneration(Board* boards){
  int i, j;
  for (i = 1; i <= boards[0].length; i++) {
    for (j = 1; j <= boards[0].height; j++){
      int numCells = 0;
      if (boards[0].grid[i-1][j] == 1)
	numCells++;
      if (boards[0].grid[i-1][j-1] == 1)
	numCells++;
      if (boards[0].grid[i-1][j+1] == 1)
	numCells++;
      if (boards[0].grid[i+1][j] == 1)
	numCells++;
      if (boards[0].grid[i+1][j-1] == 1)
	numCells++;
      if (boards[0].grid[i+1][j+1] == 1)
	numCells++;
      if (boards[0].grid[i][j-1] == 1)
	numCells++;
      if (boards[0].grid[i][j+1] == 1)
	numCells++;

      //Place values onto second board for storage
      if (boards[0].grid[i][j] == 1) {
	 if (numCells < 2 || numCells > 3)
	    boards[1].grid[i][j] = 0;
         else
           boards[1].grid[i][j] = 1;
      } else if (boards[0].grid[i][j] == 0 && 
		 numCells == 3)
	boards[1].grid[i][j] = 1;
    }
  }
  
  //Swap pointers
  Board temp = boards[1];
  boards[1] = boards[0];
  boards[0] = temp;
}

/**
 * If display frequency is given to be 0, output
 * the last generation to a file.
 */
void printToFile(Board board){
  //Open file for writing
  FILE* ofp;
  ofp = fopen("input.data", "w");

  //Print the contents of the board to the file
  fprintf(ofp, "%d %d\n", board.length, board.height);
  int i, j;
  for (i = 1; i <= board.length; i++) {
    for (j = 1; j <= board.height; j++){
      if (board.grid[j][i] == 1)
	fprintf(ofp, "%d %d\n", j, i);
    }
  }
}

/**
 * Print the board
 */
void printBoard(Board board){
  int i, j;
  for (i = 1; i <= board.length; i++) {
    for (j = 1; j <= board.height; j++){
      printf("%d ", board.grid[i][j]);
      if(j == board.height) printf("\n");
    }
  }
  printf("\n\n\n\n");
}

/**
 * Determine the next generation and print the board
 * as necessary.
 */  
void playLife(Board* boards, int generations, int displayFreq) {
  int i, displayTracker = 1;
  for (i = 0; i < generations; i++) {
    copyCollarRegion(&boards[0]);
    playGeneration(boards);
    if (displayTracker == displayFreq) {
      printBoard(boards[0]);
      displayTracker = 1;
    } else if (displayTracker != 0) 
      displayTracker++;
  }
  if (displayFreq == 0)
    printToFile(boards[0]);
}

int main(int argc, char* argv[]) {
  //Ensure arguments are included
  if (argv[1] == NULL || argv[2] == NULL) return FALSE;

  int generations, displayFreq;

  //Ensure that the arguments are of the correct format
  if (sscanf(argv[1], "%d", &generations) == 0) {
    fprintf(stderr, "First argument is not an integer");
    return FALSE;
  }

  if (sscanf(argv[2], "%d", &displayFreq) == 0) {
    fprintf(stderr, "Second argument is not an integer");
    return FALSE;
  }
  
  //open with input file or reading through stdin
  Board* boards;
  if (argv[3] != NULL) {
    //Open file
    FILE* fp;
    fp = fopen(argv[3], "r");
    boards = readFile(fp);
  } else
    boards = readFile(stdin);
    
  playLife(boards, generations, displayFreq);
  return 0;
}
