/*****************************************
This program will convert between dollars,
Euros, and Pounds.

1.00 Dollar = 0.73346 Euros
1.00 Dollar = 0.49566 Pounds
*****************************************/

#include <iostream>
#include <string>
using namespace std;

int main()
{
  //Read in the necessary variables.
  string currency1, currency2, junk;
  double amount, DtoE, DtoP, DtoC;
  DtoE = 0.73346;
  DtoP = 0.49566;
  DtoC = 1.0534;
  cout << "Enter what you want to convert (i.e. Convert 3.50 Euros to Dollars): ";
  cin >> junk >> amount >> currency1;

  //Check to see if initial currency is either Canadian or US.
  if (currency1 == "Dollars")
    cin >> currency1 >> junk >> currency2;
  else
    cin >> junk >> currency2;

  //Check to see if second currency is either Canadian or US.
  if (currency2 == "Dollars")
    cin >> currency2;

  if (currency1 == "US") //First currency is dollars
  {
    if (currency2 == "Euros")
      amount = amount * DtoE;
    else if (currency2 == "Pounds")
      amount = amount * DtoP;
    else
      amount = amount * DtoC;
  }
  else if (currency1 == "Euros") //Fist currency is Euros
  {
    if (currency2 == "US")
      amount = amount * 1.0/DtoE;
    else if (currency2 == "Pounds")
      amount = amount * 1.0/DtoE * DtoP;
    else
      amount = amount * 1.0/DtoE * DtoC;
  }
  else if (currency1 == "Pounds") //First currency is Pounds
  {
    if (currency2 == "US")
      amount = amount * 1.0/DtoP;
    else if (currency2 == "Euros")
      amount = amount * 1.0/DtoP * DtoE;
    else
      amount = amount * 1.0/DtoP * DtoC;
  }
  else if (currency1 == "Canadian")
  {
    if (currency2 == "US")
      amount = amount * 1.0/DtoC;
    else if (currency2 == "Euros")
      amount = amount * 1.0/DtoE * 1.0/DtoC;
    else
       amount = amount * 1.0/DtoP * 1.0/DtoC;
  }

  //Output conversion with new units.
  cout << "You have " << amount << " " << currency2 << endl;
  
  return 0;
}
