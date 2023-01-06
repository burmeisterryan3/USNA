import java.util.Scanner;
public class HW3
{
  /*
    Method Definitions
  */

  //Create Midshipman with input from user.
  public static Mid createMid()
  {
    Scanner scan = new Scanner(System.in);
    Mid midshipman = new Mid();
    System.out.print("Alpha? ");
    midshipman.alpha = scan.nextInt();
    System.out.print("First Name? ");
    midshipman.firstName = scan.next();
    System.out.print("Last Name? ");
    midshipman.lastName = scan.next();
    System.out.print("Company? ");
    midshipman.company = scan.nextInt();
    return midshipman;
  }

  //Print Midshipman
  public static void printMid(Mid midshipman)
  {
    if (midshipman.alpha < 100000)
      {
	System.out.print("0");
      }
    if (midshipman.alpha < 10000)
      {
	System.out.print("0");
      }
    if (midshipman.alpha < 1000)
      {
	System.out.print("0");
      }
    if (midshipman.alpha < 100)
      {
	System.out.print("0");
      }
    if (midshipman.alpha < 10)
      {
	System.out.print("0");
      }
    System.out.println(midshipman.alpha + " " + midshipman.lastName + " " +
		       midshipman.firstName + " " + midshipman.company);
    return;
  }

  /*
    Main Definition
  */

  public static void main (String [] args)
  {
    Scanner scanner = new Scanner(System.in);
    int numMids;
    //Read in number of midshipman
    System.out.print("How many mids? ");
    numMids = scanner.nextInt();
    //Declare and allocate memory for array of midshipman
    Mid [] middy = new Mid [numMids];
    //Create mids using function createMid
    for (int i = 0; i < numMids; i++)
      {
	middy[i] = createMid();
      }
    //Determine what company the user wants to print from.
    int numCompany;
    System.out.print("What company would you like to print out? ");
    numCompany = scanner.nextInt();
    //Print the midshipman with the company designated by the user.
    for (int ii = 0; ii < middy.length; ii++)
      {
	if (middy[ii].company == numCompany)
	  {
	    printMid(middy[ii]);
	  }
      }
  }
}
