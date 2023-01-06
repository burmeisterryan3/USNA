import java.util.Scanner;
public class HW2
{
  public static void main (String[] args)
  {
    //Create Scanner variable to read in input from user.
    Scanner scanner = new Scanner(System.in);
    //Obtain a user specified number of integers.
    System.out.print("How many integers will you enter? ");
    int numInt = scanner.nextInt();
    int [] intArray = new int [numInt];
    System.out.println("Please enter your integers: ");
    for (int i = 0; i < numInt; i++)
      {
	intArray[i] = scanner.nextInt();
      }
    //Obtain a string from a user.
    System.out.println("Now, please enter a String: ");
    String string = scanner.next();
    //Reverse the order of the integers.
    for (int i = 0; i < intArray.length/2; i++)
      {
	int a = intArray[i];
	intArray[i] = intArray[intArray.length-1-i];
	intArray[intArray.length-1-i] = a;
      }
    //Print the reverse order of the integers.
    System.out.print("Your integers in reverse are: ");
    for (int i = 0; i < intArray.length; i++)
      {
	System.out.print(intArray[i] + " ");
      }
    //Place spaces within the string.
    System.out.print("\nYour String, with shuffled-in spaces, is: ");
    for (int i = 0; i < string.length(); i++)
      {
	System.out.print(string.charAt(i) + " ");
      }
    System.out.println("");
  }
}