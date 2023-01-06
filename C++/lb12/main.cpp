#include <iostream>
using namespace std;

struct Node
{
  double data;
  Node* next;
};

void add2front(Node*&);
void combine(Node*&, Node*&);

int main()
{
  Node *p =  NULL;
  add2front(p);

  Node *q = NULL;
  add2front(q);

  combine(p, q);

  return 0;
}

void add2front(Node* m)
{
  double number;
  char answer = 'y';
  Node *n = new Node;
  while(answer == 'y'){
    cout << "Enter number: ";
    cin  >> number;
    n->data = number;
    n->next = m;
    m = n;
    cout << "Do you want to enter another number?: ";
    cin >> answer;
  }
  return;
}

void combine(Node* &m, Node* &n)
{
  Node* q = m;
  while(q != NULL){
    q = q->next;
  }
  q = n;
  delete n;
}
