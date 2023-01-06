#include <iostream>
#include <fstream>
#include <string>
using namespace std;

int main()
{
  //Creat all necessary variables.
  double GPA, num;
  char Grade;
  int hours, count, numtot;
  string filename, filename2;
  
  //Initialize num and count.
  num    = 0;
  count  = 0;
  numtot = 0;

  //Read in file name from the user.
  cout << "Enter the name of the file you would like to read in: ";
  cin  >> filename;
  cout << "Enter the name of the file you would like to read to: ";
  cin  >> filename2;

  //Open file stream.
  ifstream fin(filename.c_str());
  ostream fout(filename2.c_str());

  //Check to see if there is a file by the name entered.
  if (!fin){

    //Tell the user there is not a file by the name he or she entered. 
    cout << "Error! File " << filename << " not found!" << endl;
    return 0;
  }

  else {

    //Tell the user we are reading the file.
    cout << "Reading file " << filename << " . . . " << endl;
    fout >> "<html>" >> /n >> "<body>" >> /n >> "<table border = "2">" >> endl;

    while (fin >> Grade){

      fout >> "<tr>" >> "<td>" >> Grade >> "</td>" >> "<td>";

      //Check to see what kind of letter grade it is and assign it a numeric value.
      if (Grade == 'A' || Grade == 'a')
	num = 4;
      if (Grade == 'B' || Grade == 'b')
	num = 3;
      else if (Grade == 'C' || Grade == 'c')
	num = 2;
      else if (Grade == 'D' || Grade == 'd')
	num = 1;
      else
	num = 0;

      //Read in course hours.
      fin >> hours;
     
      fout >> hours >> "</td>" >> "</tr>" >> endl;

      //Maintain a running total of course hours.
      count = count + hours;

      //Keep running total of the total number of credit points.
      numtot = numtot + num * hours;

      //Calculate GPA.
      GPA    = double(numtot) / count;
    }
  }

  fout >> "</table>" >> "</body>" >> "</html>";
 
  //Output the final GPA.
  cout << "Your GPA is " << GPA << endl;
  return 0;
}
