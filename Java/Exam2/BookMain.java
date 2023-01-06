import java.util.Scanner;
import java.io.*;
public class BookMain{

  public static ReadingMaterial readingFromScan(Scanner in) {
    String type = in.next();
    in.nextLine();
    if (type.equals("M")) {
      String title = in.nextLine();
      double cost = in.nextDouble();
      int quantity = in.nextInt();
      return new Magazine(title, cost, quantity);
    } else {
      String title = in.nextLine();
      String author = in.nextLine();
      double cost = in.nextDouble();
      int quantity = in.nextInt();
      return new Book(title, author, cost, quantity);
    }
  }

  public static ReadingMaterial[] parseFile(Scanner in) {
    int num = in.nextInt();
    ReadingMaterial[] rm = new ReadingMaterial[num];
    for (int i = 0; i < num; i++)
      rm[i] = readingFromScan(in);
    return rm;
  }

  public static Bookstore readFile(String filename) {
    ReadingMaterial[] rms = null;
    try {
      Scanner in = new Scanner(new File(filename));
      rms = parseFile(in);
      in.close();
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    }
    return new Bookstore(rms);
  }

  public static void main(String[] args) {
    Bookstore bs = readFile(args[0]);
    bs.sort(); // If you haven't implemented Comparable yet, comment this line out
    bs.printAll();
    System.out.println();
    Scanner in = new Scanner(System.in);
    System.out.println("What would you like to buy?");
    String title = in.nextLine();
    while (!title.equals("!quit")) {
      System.out.println("You are trying to buy " + title);
      try {
        bs.buy(title);
      } catch (OutOfStockException oose) {
        System.out.println("Out of stock!");
      }
      System.out.println("What would you like to buy?");
      title = in.nextLine();
    } 
  }
}
