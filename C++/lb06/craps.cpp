/******************************************
This program will produce a game of craps.
The user will enter a default bid of $5 and
the program will output the amount won or
lost when the user decides to stop playing.
*******************************************/

#include <iostream>
#include <cstdlib>
#include <cmath>
using namespace std;

int rand();
int rollDice();
void showMoney(int, int);

int main()
{
  srand(time(0));

  //Initialize loop so that the user wants to play.
  char play = 'y';
  int per = 0, house = 0;

  //Run loop while user wants to play.
  while (play == 'y')
  {

    //Initialize necessary variables and obtain a sum.
    int sum = 0;
    sum = rollDice();

    //Determine if the user is a winner on the first roll.
    if (sum == 7 || sum == 11)
    {
      cout << "The sum is " << sum << "\t You win!" << endl << endl;
      per += 1;
    }

    //Determine if the user is a loser on the first roll.
    else if (sum == 2 || sum == 3 || sum == 12)
    {
      cout << "The sum is " << sum << " House Wins!" << endl << endl;
      house += 1;
    }

    //If the user is not a winner or loser on the first roll, establish setpoint.
    else
    {
      int setpoint = sum;
      cout << "The sum is " << sum << endl << "No Winner.  Your setpoint is " << setpoint << endl;
      sum = 0; //Ensure that setpoint will not equal sum.

      //Run loop until there is a winner or loser.
      while (sum != setpoint && sum != 2 && sum != 3 && sum != 7 && sum != 12) 
      {
	sum = rollDice();

	//If the user rolls the setpoint, they are a winner.
	if (sum == setpoint)
	{
	  cout << "The sum is " << sum << "\t You win!" << endl << endl;
	  per += 1;
	}

	//If the user rolls a 2, 3, 7, or 12, they are a loser.
	else if (sum == 2 || sum == 3 || sum == 7 || sum == 12)
	{
	  cout << "The sum is " << sum << "\t House Wins!" << endl << endl;
	  house += 1;
	}

	//If there is no winner, roll again.
	else
	  cout << "The sum is " << sum << "\t No winner -- Roll again!" << endl << endl;
      }
    }

    //Ask the user if they want to play again.
    cout << "Play Again? Enter 'y' or 'n': ";
    cin  >> play;
    cout << endl;
  }
  showMoney(per, house);
    
  return 0;
}

int rollDice()
{
  int randomNum1, randomNum2, SUM;
  randomNum1 = rand() % 6 + 1;
  randomNum2 = rand() % 6 + 1;
  SUM        = randomNum1 + randomNum2;
  return SUM;
}

void showMoney(int pWins, int hWins)
{
  int net = fabs(hWins - pWins) * 5;
  if (pWins > hWins)
    cout << "You won $" << net << "!" << endl << endl;
  else if (hWins > pWins)
    cout << "Pay $" << net << " to the cashier or your legs will be broken!" << endl << endl;
  else
    cout << "You broke even... you won't be so lucky next time..." << endl << endl;
  return;
}
