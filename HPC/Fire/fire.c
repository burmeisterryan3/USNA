//  MIDN Burmeister
//  Professor Albing
//  Lab 6 Fire Summation

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "mpi.h"

int** board[2];
int dimensions, haloDimension, myrank, myworld, sqroot;
MPI_Status status;
MPI_Comm comm;

// Randomly start a fire
void
startFire(int i, int ii, double fireProb) {
  double random = (double)rand()/RAND_MAX;
  if (random < fireProb) {
    board[0][i][ii] = -1*board[0][i][ii];
    board[1][i][ii] = board[0][i][ii];
  }
}

// Populate board with appropriate values
void
populateBoard(double density, double fireProb) {
  int ii, iii, age;
  srand(myrank + (unsigned)time(NULL));
  board[0] = (int**)malloc(haloDimension*sizeof(int*));
  board[1] = (int**)malloc(haloDimension*sizeof(int*));
  for (ii = 0; ii < haloDimension; ii++) {
    board[0][ii] = (int*)calloc(haloDimension, sizeof(int));
    board[1][ii] = (int*)calloc(haloDimension, sizeof(int));
    for (iii = 1; ii != 0 && ii <= dimensions && iii <= dimensions; iii++) {
      if (((double)rand()/RAND_MAX) < density) {
        age = (rand()%4)+1; // Ensure we get values >= 1
        if (age > 3) age = 3;
        board[0][ii][iii] = age;
        board[1][ii][iii] = age;
        startFire(ii, iii, fireProb); // Possibly start fire here
      }
    } // iii
  } // ii
}

// Get the buffer to send left
void
getBufferL(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[prevgen][i][1];
  } // i
}


// Get the buffer to send right
void
getBufferR(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[prevgen][i][dimensions];
  } // i
}


// Get the buffer to send up
void
getBufferU(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[prevgen][1][i];
  } // i
}

// Get the buffer to send down
void
getBufferD(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[prevgen][dimensions][i];
  } // i
}

// Update the left region of the halo
void
updateBoardL(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[prevgen][i][0] = buf[i];
  } // i
}


// Update the right region of the halo
void
updateBoardR(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[prevgen][i][dimensions+1] = buf[i];
  } // i
}


// Update the upper region of the halo
void
updateBoardU(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[prevgen][0][i] = buf[i];
  } // i
}

// Update the lower region of the halo
void
updateBoardD(int* buf, int prevgen) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[prevgen][dimensions+1][i] = buf[i];
  } // i
}

// Haloexchange for the left and right regions
void
sendLeftRight(int left, int right, int* buf, int prevgen) { 
  // Check for appropriate boundary conditions
  if ((myrank%2 == 0) && (0 != myrank%sqroot)) {
    // Send Left
    getBufferL(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, left, 0, comm);
    // Receive Left
    MPI_Recv(buf, haloDimension, MPI_INT, left, 0, comm, &status);
    updateBoardL(buf, prevgen);  
  } else if ((myrank%2 == 1) && ((sqroot-1) != myrank%sqroot)) {
    // Receive Right
    MPI_Recv(buf, haloDimension, MPI_INT, right, 0, comm, &status);
    updateBoardR(buf, prevgen);
    // Send Right
    getBufferR(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, right, 0, comm); 
  }
  if (myrank%2 == 0) {
    // Receive Right
    MPI_Recv(buf, haloDimension, MPI_INT, right, 0, comm, &status);
    updateBoardR(buf, prevgen); 
    // Send Right
    getBufferR(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, right, 0, comm); 
  } else {
    // Send Left
    getBufferL(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, left, 0, comm); 
    // Receive Left
    MPI_Recv(buf, haloDimension, MPI_INT, left, 0, comm, &status);
    updateBoardL(buf, prevgen); 
  }
} 

// Haloexchange for the upper and lower regions
void
sendUpDown(int top, int bot, int* buf, int prevgen) {
  // Check for appropriate boundary conditions
  if ((myrank%(2*sqroot) < sqroot) && (top >= 0)) {
    // Send up
    getBufferU(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, top, 0, comm);
    // Receive up
    MPI_Recv(buf, haloDimension, MPI_INT, top, 0, comm, &status);
    updateBoardU(buf, prevgen); 
  } else if ((myrank%(2*sqroot) >= sqroot) && (bot < myworld)) {
    // Receive down
    MPI_Recv(buf, haloDimension, MPI_INT, bot, 0, comm, &status);
    updateBoardD(buf, prevgen);
    // Send down
    getBufferD(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, bot, 0, comm); 
  }
  if (myrank%(2*sqroot) < sqroot) {
    // Receive down
    MPI_Recv(buf, haloDimension, MPI_INT, bot, 0, comm, &status);
    updateBoardD(buf, prevgen);
    // Send down
    getBufferD(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, bot, 0, comm); 
  } else {
    // Send up
    getBufferU(buf, prevgen);
    MPI_Send(buf, haloDimension, MPI_INT, top, 0, comm);
    // Receive up
    MPI_Recv(buf, haloDimension, MPI_INT, top, 0, comm, &status);
    updateBoardU(buf, prevgen);
  }
}

// Perform the halo exchange
void
haloExchange(int prevgen) { 
  int* buf = (int*)malloc(haloDimension*sizeof(int));
  int left = myrank-1;
  int right = myrank+1;
  int top = myrank - sqroot;
  int bot = myrank + sqroot; 
  sendLeftRight(left, right, buf, prevgen);
  sendUpDown(top, bot, buf, prevgen);
}

// Calculate the sum of the Moore's neighbors
int
calcSum(int prevgen, int i, int ii) {
  int sum = 0;
  if (board[prevgen][i-1][ii-1] < 0) sum++;
  if (board[prevgen][i-1][ii]   < 0) sum++;
  if (board[prevgen][i-1][ii+1] < 0) sum++;
  if (board[prevgen][i+1][ii-1] < 0) sum++;
  if (board[prevgen][i+1][ii]   < 0) sum++;
  if (board[prevgen][i+1][ii+1] < 0) sum++;
  if (board[prevgen][i][ii-1]   < 0) sum++;
  if (board[prevgen][i][ii+1]   < 0) sum++;
  return sum;
}

// Print Board
void
printBoard(int gen) {
  int i, ii;
  for (i = 1; i <= dimensions; i++) {
    for (ii = 1; ii <= dimensions; ii++) {
      if (board[gen][i][ii] < 0) printf(" -");
      else printf(" %d", board[gen][i][ii]);
    }
    printf("\n");
  }
  printf("\n\n\n\n");
}

// Spread the fire to neighboring regions
void
spreadFire(int prevgen, int gen, int i, int ii, double spreadProb) {
  // Determine the threshold our probability must be greater than to spread 
  int sum = calcSum(prevgen, i, ii);
  double threshold = spreadProb*sum;

  // Random number that we will check against our threshold
  double random = (double)rand()/RAND_MAX;
  if (random < threshold) { // Fire did spread
    board[gen][i][ii] = -1*board[prevgen][i][ii];
  } else { // Fire did not spread
    board[gen][i][ii] = board[prevgen][i][ii];
  }
}

// Simulate the fire
void
simulateFire(double spreadProb) {
  int flag, sum, prevgen=0, gen=1, i, ii, tree;
  int** temp;
  // if (myrank == 0) printBoard(gen); // Print for debugging
  do {
    flag = 0; // Reset the flag each generation
    haloExchange(prevgen); // Perform haloExchange
    
    // Perform the spread of fire
    for (i = 1; i <= dimensions; i++) {
      for (ii = 1; ii <= dimensions; ii++) {
        tree = board[prevgen][i][ii];
        if (tree < 0) { // if on fire update both boards
          board[gen][i][ii] = board[prevgen][i][ii] + 1;
          flag = 1;
        } else if (tree > 0) {
          spreadFire(prevgen, gen, i, ii, spreadProb);
        } else { // board is 0 at this position
          board[gen][i][ii] = 0;
        }
      } // i
    } // ii

    // if (myrank == 0) printBoard(gen); // Print for debugging

    gen++; // Update the generation
    if (gen == 2) gen = 0;
    prevgen++;
    if (prevgen == 2) prevgen = 0;

    //Check to see if there are no more fires
    MPI_Allreduce(&flag, &sum, 1, MPI_INT, MPI_SUM, comm);
  } while (sum != 0);
}

// Calculates the final density of each portion of the forest
double
calcDensity() {
  // Determine the size at each of the ranks
  double size = dimensions*dimensions;
  int i, ii, numTrees = 0;
  for (i = 1; i <= dimensions; i++) {
    for (ii = 1; ii <= dimensions; ii++) {
      if (board[0][i][ii] > 0) {
        numTrees++;
      }
    } // ii
  } // i
  // Calculate the density
  double density = numTrees/size;
  double totDensity;

  // Receive the density from each of the ranks
  MPI_Reduce(&density, &totDensity, 1, MPI_DOUBLE, MPI_SUM, 0, comm);
  // Print the average density if rank 0
  if (myrank == 0) {
    double avg = totDensity/myworld;
    printf("The average density was: %lf\n", avg);
  }
}

main (int argc, char** argv) {
  int size, numFires;
  double density, spreadProb;
  if (argc == 5) { // program size density
    sscanf(argv[1], "%d", &size);
    sscanf(argv[2], "%lf", &density);
    sscanf(argv[3], "%d", &numFires);
    sscanf(argv[4], "%lf", &spreadProb);
  } else {
    printf("usage: fire Size Density\n");
    exit(1);
  }

  // Initialize necessary MPI details
  MPI_Init(NULL, NULL);
  comm = MPI_COMM_WORLD;
  MPI_Comm_size(comm, &myworld);
  MPI_Comm_rank(comm, &myrank);

  // Determine the dimensions of the board
  sqroot = sqrt(myworld);
  dimensions = size/sqroot;
  haloDimension = dimensions + 2;

  // Determine fire probability
  double fireProb = (double)numFires/(density*size*size);  

  // Populate the board and perform the haloexchange
  populateBoard(density, fireProb);
  // Simulate the fire
  simulateFire(spreadProb);
  // Calculate the average density at each rank when fire is finished 
  calcDensity(); 

  MPI_Finalize();
}
