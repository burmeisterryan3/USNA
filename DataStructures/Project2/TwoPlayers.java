import java.util.*;
import java.io.*;
 
/** A two-player game pitting human vs computer - who will be victorious?
 */
public class TwoPlayers {
  private class ComputerPlayer extends Thread {
    public void run() {
      compWords = game.allWords();
    }
  }
 
  public Board game;
  public ComputerPlayer computer;
  public volatile Queue<String> compWords = null;
 
  //TODO: Replace YourDataStructure below.
  public BoggleHashMap myWords = new BoggleHashMap(250);
 
  public TwoPlayers() {
    game = new Board();
    computer = new ComputerPlayer();
  }
 
  public void goHuman(long endTime) throws IOException {
    BufferedReader input = new BufferedReader(System.console().reader());
    PrintWriter output = System.console().writer();
 
    while (true) {
      output.print("\u001b[2J\u001b[H"); // clears the screen
      output.println(game);
      output.print("Current words:");
      for (String word : myWords.traverse()) {
        output.print(" " + word);
      }
      output.print("\n" + (endTime - System.currentTimeMillis())/1000
          + " seconds remaining.");
 
      output.print("\n\nNext word? ");
      output.flush();
      while (!input.ready() && System.currentTimeMillis() < endTime) {
        try { Thread.sleep(100); }
        catch (InterruptedException ee) { endTime = 0; }
      }
      if (System.currentTimeMillis() >= endTime) {
        output.println("TIME'S UP!");
        break;
      }
      String nextWord = input.readLine().trim().toUpperCase();
      myWords.insert(nextWord);
    }
  }
 
 
  public static void main(String[] args) {
    TwoPlayers play = new TwoPlayers();
    try {
      play.computer.start();
      play.goHuman(System.currentTimeMillis() + 60000);
    } catch (Exception e) {
      System.err.println("\n(game ended prematurely)");
    }
 
    System.out.println("\n================== RESULTS =====================\n");
 
    int myPoints = Board.countPoints(play.myWords.traverse());
    System.out.println("You got " + myPoints + " points.");
    System.out.println("(note: these words were not checked for validity!)\n");
    
    if (play.compWords == null) {
      System.out.println("\nWaiting for the computer to finish...");
      try { play.computer.join(); }
      catch(InterruptedException ee) { }
    }
 
    int compPoints = Board.countPoints(play.compWords);
    System.out.println("The computer got " + compPoints + " points.");
 
    try { 
      System.out.print("\n(let that sink in");
      for (int ii=0; ii<30; ++ii) {
        Thread.sleep(100);
        System.out.print(".");
      }
      System.out.println(")");
    }
    catch (InterruptedException ee) { }
 
    System.out.println("\nHere are the computer's words:");
    int online = 0;
    for (String word : play.compWords) {
      if (online >= 8) {
        System.out.println();
        online = 0;
      }
      System.out.print(word + " ");
      ++online;
    }
    System.out.println();
  }
}