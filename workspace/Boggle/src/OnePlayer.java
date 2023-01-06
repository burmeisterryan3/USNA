import java.util.Queue;
 
public class OnePlayer{
  /** Runs a one-player game (computer only), for speed.
   * If there are no command-line arguments, a fresh board is generated.
   * Otherwise, the first command-line argument is a seed value for the
   * random number generator.
   */
  public static void main(String[] args) {
    Board game;
    long start = System.nanoTime();
 
    if (args.length > 0) game = new Board(Long.parseLong(args[0]));
    else game = new Board();
    System.out.println(game);
 
    Queue<String> words = game.allWords();
    for (String word : words) System.out.println(word);
    System.out.println(Board.countPoints(words) + " points");
 
    long elapsed = System.nanoTime() - start;
    System.out.println((elapsed/1.0e9) + " seconds");
  }
}