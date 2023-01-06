import java.util.Scanner;

/**
 * The Chess class is in charge of reading user input and controls
 * the game play of when/how the pieces move around the board.
 */
public class Chess
{

  /** 
   * Used to get a move from the user, such as "b1 to c3".
   *
   * @param in A Scanner which is probably listening to System.in
   * @return a 8-character String entered by the player.
   */
  public static String getMove(Scanner in)
  {
    String s;
    do {
      System.out.println("Please enter a move (e.g. \"b1 to c3\"): ");
      s = in.nextLine();
    } while( s.length() != 8 );
    return s;
  }

  public static void main(String[] args)
  {
    Chess chess = new Chess();
    Board board = new Board();
    Scanner scanner = new Scanner(System.in);
    String move, to, from, input[];
    
    while (true)
    {
        System.out.println(board);
        
        if (board.whiteTurn())
        {
            System.out.println("White's turn.");
        }
        else
        {
            System.out.println("Black's turn.");
        }
        
        move = chess.getMove(scanner);
        input = move.split(" ");
        from = input[0];
        to = input [2];
        
        try
        {
            board.makeMove(from, to);
        }
        catch (IllegalArgumentException iae)
        {
            iae.printStackTrace();
        }
        
        int winner = board.winner();
        if (winner == -1)
        {
            System.out.println("Black wins!");
            break;
        }
        else if (winner == 1)
        {
            System.out.println("White wins!");
            break;
        }
    }
  }
}
