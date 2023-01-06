#include <stdio.h>
#include <stdlib.h>

/*
 *  Life generations display_period  < inputFile
 *
 *  ./myLife  50  10 /tmp/scratch.life
 *  runs for 50 generations, displaying every 10th
 *  input file format:
 *  # comment
 *  x y
 *
 */

char **board[2];  // two boards to alternate between
int XSIZ, YSIZ;   // dimensions of the board

/*
 * get both [0] and [1] boards
 */
void
initBoard(xsize, ysize)
{
    int b, i;

    for(b=0;b<=1;b++) {
  board[b] = calloc((size_t)XSIZ+2, sizeof(char *));
  for (i=0; i < XSIZ+2; i++) {
      board[b][i] = calloc((size_t)YSIZ+2, sizeof(char));
  }
    }

} // initBoard

// pass in 0 for raw x,y points; non-0 for ascii board
displayBoard(int type, int b)
{
    int i,j;

    if (type) {
  printf("-----------------------------------------------\n");
  for (j=1; j <= YSIZ; j++) {
      for (i=1; i <= XSIZ; i++) {
        printf ("%c", board[b][i][j]?'@':' ');
      }
      printf ("\n");
  }
  
    } else {
        printf ("%d %d\n", XSIZ, YSIZ);
  for (j=1; j <= YSIZ; j++) {
      for (i=1; i <= XSIZ; i++) {
        if(board[b][i][j]) {
      printf ("%d %d\n", i, j);
        }
      }
  }
    }

} // displayBoard

// pass in 0 for raw x,y points; non-0 for ascii board
// show the halo, too.
displayBuffers(int type, int b)
{
    int i,j;

    if (type) {
  printf("-----------------------------------------------\n");
  for (j=0; j <= YSIZ+1; j++) {
      for (i=0; i <= XSIZ+1; i++) {
        printf ("%c", board[b][i][j]?'@':' ');
      }
      printf ("\n");
  }
  
    } else {
        printf ("%d %d\n", XSIZ, YSIZ);
  for (j=0; j <= YSIZ+1; j++) {
      for (i=0; i <= XSIZ+1; i++) {
        if(board[b][i][j]) {
      printf ("%d %d\n", i, j);
        }
      }
  }
    }

} // displayBuffers

// read inputfile
void
loadlife()
{
    int x,y;
    /* first, read in the board size */
    scanf("%d %d\n", &XSIZ, &YSIZ);
    /* now allocate it */
    initBoard(XSIZ, YSIZ);

    /* now read in the existing live cells into board [0] */
    while (scanf("%d %d\n", &x, &y) != EOF) {
  board[0][x][y] = 1;
    }

} // loadlife

int
neighbors(char **ra, int x, int y)
{
    int cnt;
    int xm1, xp1;
    int ym1, yp1;


    xm1 = x-1;
    xp1 = x+1;
    ym1 = y-1;
    yp1 = y+1;

    cnt = 0;
    cnt += ((ra[xm1][y]) ? 1 : 0);
    cnt += ((ra[xm1][yp1]) ? 1 : 0);
    cnt += ((ra[x][yp1]) ? 1 : 0);
    cnt += ((ra[xp1][yp1]) ? 1 : 0);
    cnt += ((ra[xp1][y]) ? 1 : 0);
    cnt += ((ra[xp1][ym1]) ? 1 : 0);
    cnt += ((ra[x][ym1]) ? 1 : 0);
    cnt += ((ra[xm1][ym1]) ? 1 : 0);

    return cnt;

} // neighbors

void
newLife(char **newra, char **oldra, int i, int j)
{
    int nc;
    newra[i][j] = 0;
    nc = neighbors(oldra, i,j);
    do {
  if (oldra[i][j] == 0) {   // empty cells
      if (nc == 3) {
    newra[i][j] = 1;  // turn that cell on
      }
      break;
  }
          // live cells
  if ((nc < 2) || (nc > 3)) { // isolation or overcrowding
      newra[i][j] = 0;    // turn off that cell
      break;
  }
  // else: nc >= 2 nc <= 3 and non-zero
  newra[i][j] = 1;
  break;
    } while (0);  // once

    // fprintf(stderr, "[%d, %d] => %d\n", i, j, ra[i][j]);

} // newlife

haloXchange(int gen, char ***board, int x, int y)
{
    int i,j;
    char **bp;

    if (gen & 1) {
  // use board[1]
  bp = board[1];
    } else {
  // use board[0]
  bp = board[0];
    }
    // swap the y borders
    for(i=0; i< y; i++) {
  bp[0][i] = bp[x-2][i];
  bp[x-1][i] = bp[1][i];
    }
    // swap the x borders
    for(i=0; i< x; i++) {
  bp[i][0] = bp[i][y-2];
  bp[i][y-1] = bp[i][1];
    }

} // haloXchange

main(int argc, char **argv)
{
    int i,j;
    int gen, genOut, period;

    if (argc > 1) {
  genOut = atoi(argv[1]);
  period = atoi(argv[2]);
    } else {
  printf ("usage: myLife Generations period < infile\n");
  exit(-1);
    }

  // read inputfile and init array
  loadlife();

  for(gen=0; gen < genOut; gen++) {
    if (period > 0 && gen%period == 0) {
  displayBoard(period, gen&1);
  system("sleep 0.25");
    }
    // halo exchange
    haloXchange(gen, board, XSIZ+2, YSIZ+2);

    // debug - set period to -1
    if (period < 0) {
  displayBuffers(period, gen&1);
    }

    // compute next gen
    for (i=1; i <= XSIZ; i++) {
  for (j=1; j <= YSIZ; j++) {
      if (gen & 1) {
    newLife(board[0], board[1], i, j);
      } else {
    newLife(board[1], board[0], i, j);
      }
  } // next j
    } // next i

  } // next gen

  // final display
  displayBoard(period, gen&1);

} // main
