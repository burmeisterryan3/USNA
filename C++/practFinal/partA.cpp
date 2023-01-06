#include<iostream>
#include<string>
#include<fstream>
using namespace std;

//Struct Definitions
struct Place
{
  int population;
  double miles;
  string name, type;
};

//Function Prototypes
Place *readData(istream&, int&);
void printData(Place*, string, int, int);

int main()
{
  cout << "Welcome to MIDN Burmeister's (160786) Censusizer!" << endl;
  
  //Obtain filename and open file.
  string filename;
  cout << "Enter filename: ";
  cin  >> filename;
  ifstream fin;
  fin.open(filename.c_str());

  //Read in the necessary information from the file.
  int num;
  Place *data;
  data = readData(fin, num);

  //Ask user what information he or she wants.
  string type;
  int pop;
  cout << "Read in " << num << " places." << endl;
  cout << "Enter type of place: ";
  cin  >> type;
  cout << "Enter maximum population: ";
  cin  >> pop;

  printData(data, type, pop, num);
  
  return 0;
}

//Function Definitions.
Place *readData(istream &IN, int &size)
{
  IN >> size;
  Place *data = new Place[size];
  string junk;
  for (int i = 0; i < size; i++){
    IN >> junk >> junk >> data[i].population >> junk >> data[i].miles
       >> junk >> junk >> data[i].name >> data[i].type;
  }
  return data;
}

void printData(Place *data, string type, int pop, int size)
{
  for (int i = 0; i < size; i++){
    if (data[i].type == type && data[i].population <= pop){
      cout << data[i].type << ": " << data[i].name << ", pop: "
	   << data[i].population << ", sq. miles: " << data[i].miles << endl;
    }
  }
  return;
}
