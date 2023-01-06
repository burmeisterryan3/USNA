// DMN
// A tic-tac-toe game using a Struct for the gameboard
// Game win function modified by LKM 

#include <iostream>
#include <fstream>
using namespace std;
 
struct TicTacToe
{
  char **board;  // this is the game board
  int winX, winO, draw;  //keep track of the number of wins and draws.
  char turn;
};
 
void createBoard(TicTacToe&);
void deallocateBoard(TicTacToe&);
void printBoard(TicTacToe);
bool is_A_Draw(TicTacToe);
void convertUserEntry(char, char, int&, int&);
char winningMove(TicTacToe, int, int);
void clearBoard(TicTacToe&);
void playGame(TicTacToe&, char&, char&);
void saveBoard(TicTacToe);
 
const int SIZE = 3;     // use a constant for row/col sizes to increase program readability
 
 
int main() {
 
  TicTacToe game;                // declare an instance of the tic-tac-toe game board
  game.winX = 0;
  game.winO = 0;
  game.draw = 0;                 // 'X' goes first
  game.turn = 'X';
 
  createBoard(game);

  //Initialize play for the loop.
  char play = 'y';
  char winner = 0;               // no winner initially 
 
  //Play the game.
  while(play == 'y'){
    playGame(game, winner, play);
  }
 
  cout << endl<< "Shutting down, have a fine Navy day and Beat Army!" << endl << endl;
  // delete dynamically allocated memory
  deallocateBoard(game);
  
  return 0;
}


// Returns 'X' if (i,j) was a winning move for X
// Returns 'Y' if (i,j) was a winning move for Y
// Returns ASCII value 0 otherwise 
char winningMove(TicTacToe gameIn, int i, int j) 
{
  if (gameIn.board[i][0]==gameIn.board[i][1] &&
      gameIn.board[i][0]==gameIn.board[i][2])  // row i all the same
    {
      return gameIn.board[i][j]; // return the id of the player in the [i,j] cell
    }
 
  if (gameIn.board[0][j]==gameIn.board[1][j] &&
      gameIn.board[0][j]==gameIn.board[2][j])  // column j all the same
    {
      return gameIn.board[i][j]; // return the id of player in the [i,j] cell
    }
 
  if (gameIn.board[0][0] != ' ' && // make sure not all blanks
      gameIn.board[0][0] == gameIn.board[1][1] && 
      gameIn.board[0][0] == gameIn.board[2][2])
    {
      return gameIn.board[i][j]; // left diagonal all the same
    }
 
  if (gameIn.board[0][2] != ' ' && // make sure not all blanks
      gameIn.board[0][2] == gameIn.board[1][1] &&
      gameIn.board[0][2] == gameIn.board[2][0])
    {
      return gameIn.board[i][j]; // right diagonal all the same
    }
 
  return 0;
 
} // end winningMove
 
 
// Returns true if the game is a draw
bool is_A_Draw(TicTacToe gameIn) 
{
  bool full = true;                      // assume full until determined otherwise
  for(int i = 0; full && i < SIZE; i++)
    for(int j = 0; full && j < SIZE; j++)
      full = gameIn.board[i][j] != ' ';   // found a non-blank cell, so board is NOT full
  if (full == true)
    gameIn.draw++;
  return full;
} // end is_A_Draw
 
 
// Prints the board
void printBoard(TicTacToe gameIn) 
{
  cout << endl << "      |1|2|3|" << endl;     // print top reference
  for(int i = 0; i < SIZE; i++)
    {
      cout << "     " << char(i + 'a') << '|';         // prints left reference
      for(int j = 0; j < SIZE; j++)
	cout << gameIn.board[i][j] << '|';  // prints current board cell contents
      cout << endl;
    }
} // end printBoard
 
 
// Converts moves like "b3" into corresponding array indexes (1,2)
void convertUserEntry(char charRowIn, char charColumnIn, int& row, int& column)
{
  row = toupper(charRowIn) - 'A';  // charRowIn uppercased, then converted to 0,1,2
  column = charColumnIn - '1';     // ColumnIn converted to 0,1,2
} // end convertUserEntry
 
 
// Creates the board with all squares init to ' '
void createBoard(TicTacToe& gameIn) 
{                               
  gameIn.board = new char*[SIZE];     // allocate dynamic memory for 2D board
  for(int i = 0; i < SIZE; i++)
    gameIn.board[i] = new char[SIZE];
                            
  for(int j =0;j < SIZE; j++)         // initialize board with blanks
    for(int k = 0; k < SIZE; k++)  
      gameIn.board[j][k] = ' ';
} // end creatBoard
 
 
// Free up all memory for the board
void deallocateBoard(TicTacToe& gameIn)
{
  for(int i = 0; i < SIZE; i++)  
    delete [] gameIn.board[i];     // deallocate the 2D array, cell by cell
  delete [] gameIn.board;          // delete the outer array
  gameIn.board = NULL;             // defensive programming (the board no longer exists)
} // end deallocateBoard

//Clear board of all moves.
void clearBoard(TicTacToe &gameIN)
{
  for (int i = 0; i < SIZE; i++){
    for (int j = 0; j < SIZE; j++){
      gameIN.board[i][j] = ' ';
    }
  }
  return;
}

void playGame(TicTacToe &game, char& winner, char &play)
{
  char charRowIn, charColumnIn;  // user's input (characters)
  int row, column;               // user's input converted to 2D array indices
  while(!winner && !is_A_Draw(game))
    {
      printBoard(game);
 
      // Read Move
      cout << "Player " << game.turn << "'s move (input format: a1): ";
      cin >> charRowIn >> charColumnIn;
      if (charRowIn == 's'){
	saveBoard(game);
	winner = 1;
      }
      else{
	convertUserEntry(charRowIn, charColumnIn, row, column); // convert chars ('a','0') to ints (0,0)
	
	if (game.board[row][column]==' ')            // Make move if square free
        {                   
          game.board[row][column] = game.turn;
          if (game.turn == 'X')                           // set turn to other person
	    game.turn = 'O';
          else                
	    game.turn = 'X';
	  
          winner = winningMove(game, row, column);  //  if this move wins the game, returns 'X' or 'O',
	  if (winner == 'X')
	    game.winX++;
	  else if (winner == 'O')
	    game.winO++;
	  if (is_A_Draw(game))
	    game.draw++;
        } // end if                                 //  else zero if no winner yet) 
	else{
	  cout<<"Square taken, try again!";         // disallow taking a previously filled cell
	} // end while (loop continues if no winner yet and not a draw)
      
	// Game over, declare winner
	printBoard(game);
	
	if (winner == 'X' || winner == 'O')
	  cout << "*** Congratulations, the winner is " << winner << '.' << endl;
	else
	  cout << "*** Game ends in a draw." << endl;
	
	cout << "X has won " << game.winX << " games!" << endl;
	cout << "O has won " << game.winO << " games!" << endl;
	cout << "There have been " << game.draw << " CAT games" << endl;

	//Ask if the user would like to play again.
	cout << "Play again ('y'es or 'q'uit): ";
	cin  >> play;
	if (play == 'y'){
	  clearBoard(game);
	  winner = 0;
	}
      }
    }
    return;
}

void saveBoard(TicTacToe game)
{
  ofstream fout("tictactoe.txt");
  for (int i = 0; i < SIZE; i++){
    for (int j = 0; j < SIZE; j++){
      if (game.board[i][j] == ' ')
	fout << 'B';
      else
	fout << game.board[i][j];
      fout << " ";
    }
  }
  fout << game.turn << " " << game.winX << " " << game.winO << " " << game.draw;
  fout.close();
  return;
}
