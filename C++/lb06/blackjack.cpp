#include <cstdlib>
#include <string>
using namespace std;

string cardgen();

int main()
{
  srand(time(0));
  cardgen();
  return 0;
}

string cardgen()
{
  string card = "A";
  int num1 = rand() % 52 + 1;
  string num = num1;
  if (num >= 1 && num1 <= 13)
  {
    if (num1 >= 2 && num1 <= 10)
      card == num;
    else if (num1 == 11)
      card == "J";
    else if (num1 == 12)
      card == "Q";
    else if (num1 == 13)
      card == "K";
    card += "H";
  }
  else if (num1 > 13 && num1 <= 26)
  {
    if (num1 >= 15 && num1 <= 23)
      card = num;
    else if (num1 == 24)
      card =  "J";
    else if (num1 == 25)
      card = "Q";
    if (num1 == 26)
      card = "K";
    card += "C";
  }
  else if (num1 > 26 && num1 <= 39)
  {
    if (num1 >= 28 && num1 <= 36)
      card = num;
    else if (num1 == 37)
      card =  "J";
    else if (num1 == 38)
      card = "Q";
    else if (num1 == 39)
      card = "K";
    card += "S";
  }
  else
  {
    if (num1 >= 41 && num1 <= 49)
      card = num;
    else if (num1 == 50)
      card =  "J";
    else if (num1 == 51)
      card = "Q";
    else if (num1 == 52)
      card = "K";
    card += "D";
  }
  return card;
}
