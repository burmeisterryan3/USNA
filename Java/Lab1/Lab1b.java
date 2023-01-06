import java.util.*;
public class Lab1b
{
  public static int factorial(int x)
  {
    if (x == 1)
      return x;

    x = x * factorial(x-1);
    return x;
  }
  public static int numerator(int n, int k)
  {
    int x = n;
    for (int i = n-k+1; i<n; i++)
      {
	x = x*i;
      }
    return x;
  }
  public static void main (String [] args)
  {
    Scanner in = new Scanner(System.in);
    System.out.print("Please enter your name: ");
    String name = in.nextLine();
    System.out.print("Please input n ");
    int n = in.nextInt();
    System.out.print("Please input k ");
    int k = in.nextInt();
    System.out.println("The two ints were " + n + " and " + k);
    int x = numerator(n,k);
    int y = factorial(k);
    x = x/y;
    System.out.println(name + " your odds are 1 : " + x);
  }
}