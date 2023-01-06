import java.util.Scanner;

/** Program to read names and times into a max-heap and print the
 * 10 names with the smallest times.
 */
public class TopTen {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    // This heap will hold the smallest 10 entries at any given time
    TimeHeap bestTimes = new TimeHeap();

    while (in.hasNext()) {
      String name = in.next();
      long time = in.nextLong();
      // insert into the max-heap
      bestTimes.insert(time, name);
      // if more than 10 entries, remove the worst one
      if (bestTimes.size() > 10) bestTimes.removeMax();
    }

    // count down the smallest times
    for (int place = bestTimes.size(); place >= 1; --place) {
      System.out.print(place);
      System.out.print(". ");
      System.out.println(bestTimes.removeMax());
    }
  }
}
