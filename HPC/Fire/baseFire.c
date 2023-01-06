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

// Populate board with appropriate values
void
populateBoard(int density) {
  int i, ii, iii;
  for (i = 0; i < 2; i++) {
    board[i] = (int**)malloc(haloDimension*sizeof(int*));
    for (ii = 0; ii < haloDimension; ii++) {
      board[i][ii] = (int*)calloc(haloDimension, sizeof(int));
      for (iii = 1; ii <= dimensions && iii <= dimensions; iii++) {
        board[i][ii][iii] = myrank;
      } // iii
    } // ii
  } // i
}

// Get the buffer to send left
void
getBufferL(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[0][i][1];
  } // i
}


// Get the buffer to send right
void
getBufferR(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[0][i][dimensions];
  } // i
}


// Get the buffer to send up
void
getBufferU(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[0][1][i];
  } // i
}

// Get the buffer to send down
void
getBufferD(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    buf[i] = board[0][dimensions+1][i];
  } // i
}

// Update the left region of the halo
void
updateBoardL(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[0][i][0] = buf[i];
  } // i
}


// Update the right region of the halo
void
updateBoardR(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[0][i][dimensions+1] = buf[i];
  } // i
}


// Update the upper region of the halo
void
updateBoardU(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[0][0][i] = buf[i];
  } // i
}


// Update the lower region of the halo
void
updateBoardD(int* buf) {
  int i;
  for (i = 0; i < haloDimension; i++) {
    board[0][dimensions+1][i] = buf[i];
  } // i
}

// Haloexchange for the left and right regions
void
sendLeftRight(int left, int right, int* buf) {
  // Check for appropriate boundary conditions
  if ((myrank%2 == 0) && (0 != myrank%sqroot)) {
    // Send Left
    getBufferL(buf);
    MPI_Send(buf, haloDimension, MPI_INT, left, 0, comm);
    // Receive Left
    MPI_Recv(buf, haloDimension, MPI_INT, left, 0, comm, &status);
    updateBoardL(buf);  
  } else if ((myrank%2 == 1) && ((sqroot-1) != myrank%sqroot)) {
    // Receive Right
    MPI_Recv(buf, haloDimension, MPI_INT, right, 0, comm, &status);
    updateBoardR(buf);
    // Send Right
    getBufferR(buf);
    MPI_Send(buf, haloDimension, MPI_INT, right, 0, comm); 
  }
  if (myrank%2 == 0) {
    // Receive Right
    MPI_Recv(buf, haloDimension, MPI_INT, right, 0, comm, &status);
    updateBoardR(buf); 
    // Send Right
    getBufferR(buf);
    MPI_Send(buf, haloDimension, MPI_INT, right, 0, comm); 
  } else {
    // Send Left
    getBufferL(buf);
    MPI_Send(buf, haloDimension, MPI_INT, left, 0, comm); 
    // Receive Left
    MPI_Recv(buf, haloDimension, MPI_INT, left, 0, comm, &status);
    updateBoardL(buf); 
  }
} 

// Haloexchange for the upper and lower regions
void
sendUpDown(int top, int bot, int* buf) {
  // Check for appropriate boundary conditions
  if ((myrank%(2*sqroot) < sqroot) && (top >= 0)) {
    // Send up
    getBufferU(buf);
    MPI_Send(buf, haloDimension, MPI_INT, top, 0, comm);
    // Receive up
    MPI_Recv(buf, haloDimension, MPI_INT, top, 0, comm, &status);
    updateBoardU(buf); 
  } else if ((myrank%(2*sqroot) >= sqroot) && (bot < myworld)) {
    // Receive down
    MPI_Recv(buf, haloDimension, MPI_INT, bot, 0, comm, &status);
    updateBoardD(buf);
    // Send down
    getBufferD(buf);
    MPI_Send(buf, haloDimension, MPI_INT, bot, 0, comm); 
  }
  if (myrank%(2*sqroot) < sqroot) {
    // Receive down
    MPI_Recv(buf, haloDimension, MPI_INT, bot, 0, comm, &status);
    updateBoardD(buf);
    // Send down
    getBufferD(buf);
    MPI_Send(buf, haloDimension, MPI_INT, bot, 0, comm); 
  } else {
    // Send up
    getBufferU(buf);
    MPI_Send(buf, haloDimension, MPI_INT, top, 0, comm);
    // Receive up
    MPI_Recv(buf, haloDimension, MPI_INT, top, 0, comm, &status);
    updateBoardU(buf);
  }
}

// Perform the halo exchange
void
haloExchange() {  
  int* buf = (int*)malloc(haloDimension*sizeof(int));
  int left = myrank-1;
  int right = myrank+1;
  int top = myrank - sqroot;
  int bot = myrank + sqroot; 
  sendLeftRight(left, right, buf);
  sendUpDown(top, bot, buf);
}

// Calculate the sum of the Moore's neighbors
int
calcSum() {
  int sum = 0;
  sum += board[0][dimensions-1][dimensions-1];
  sum += board[0][dimensions-1][dimensions];
  sum += board[0][dimensions-1][dimensions+1];
  sum += board[0][dimensions][dimensions-1];
  sum += board[0][dimensions][dimensions+1];
  sum += board[0][dimensions+1][dimensions-1];
  sum += board[0][dimensions+1][dimensions];
  sum += board[0][dimensions+1][dimensions+1];
  sum += board[0][dimensions][dimensions];
  return sum;
}

main (int argc, char** argv) {
  int size;
  if (argc > 1) {
    size = atoi(argv[1]);
  } else {
    printf("usage: fire Size\n");
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

  // Populate the board and perform the haloexchange
  populateBoard(density);
  haloExchange();

  // Get the sum from every node and sum those values 
  int global_sum;
  int local_sum = calcSum(); 
  MPI_Reduce(&local_sum, &global_sum, 1, MPI_INT, MPI_SUM, 0, comm);
 
  // If the root, print the sum 
  if (myrank == 0) {
    printf("Sum: %d\n", global_sum);
  }
  
  MPI_Finalize();
}
