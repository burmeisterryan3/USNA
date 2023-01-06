#include <iostream>
using namespace std;

//Struct Definitions
struct Node
{
  int data;
  Node *next;
};

//Function Prototypes
void add2front(Node*);//possibly return node* or call by reference
void print(Node*);

int main()
{
  //create null list
  Node *list = NULL;
  
  //Record numbers user wants to record until 0 is entered.
  while(){//develop what condition needs to be
    add2front(list);
  }
  
  //Print the list out
  print(list);
  
  //Print the last number that was read
  cout << "The last number read was " << list->data;

  //Print the second to last number that was read
  cout << "The next to last number was " << list->next->data;

  return 0;
}
