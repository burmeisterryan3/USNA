import java.util.*;
public class Lab1c
{
  public static void main (String [] args)
  {
    Random rand = new Random(System.currentTimeMillis());
    int x = rand.nextInt(10);
    Scanner in = new Scanner(System.in);
    System.out.print("Please enter your guess ");
    int guess = in.nextInt();
    int count = 1;
    while (guess != x)
      {
	if (count == 1)
	  System.out.print("Nope! Try again... ");
	if (count == 2)
	  System.out.print("Incorrect! Try again... ");
	if (count == 3)
	  System.out.print("Still wrong! Keep trying... ");
	if (count == 4)
	  System.out.print("Can't believe you still haven't gotten it... ");
	if (count == 5)
	  System.out.print("You sure you still want to keep trying... ");
	if (count == 6)
	  System.out.print("This is embarrassing! ");
	if (count == 7)
	  System.out.print("You should just quit now... ");
	if (count == 8)
	  System.out.print("Just pathetic. ");
	if (count == 9)
	  System.out.print("Well you can't get it wrong now... ");
	guess = in.nextInt();
	count++;
      }
    System.out.print("Correct!");
  }
}