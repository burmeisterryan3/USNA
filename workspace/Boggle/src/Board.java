import java.util.*;
import java.io.*;
 
/** This is the main class that does the Boggle playing, using
 * your data structure.
 */
public class Board {
	// These are the faces of the 25 actual Boggle game dice!
	private static String[] dice = new String[] {
		"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    	"AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCENST",
    	"CEIILT", "CEILPT", "CEIPST", "DDHNOT", "DHHLOR",
    	"DHLNOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    	"FIPRSY", "GORRVW", "IPRRRY", "NOOTUW", "OOOTTU",
	};
 
	private Random rgen;
 
	private char[][] board = new char[5][5];
	
	private BoggleHashMap englishWords;
 
	/** Constructs the board, using a random seed for the random number generator.
	 */
	public Board() {
		rgen = new Random(); // seed will be different every time
		initBoard();
		initDictionary();
	}
 
	/** Constructs the board, creating one of your data structure, and filling it
	 * 	with the words in the English dictionary.
   	 *
   	 * Prints an error message if the file "american-english" is not in the
   	 * current directory.
   	 * @param seed the seed for the random object; running this with identical
   	 * seeds should get you identical dice rolls and placement.
   	 */
	public Board(long seed) {
		rgen = new Random(seed);
		initBoard();
		initDictionary();
	}
	
	private void initDictionary() {
		englishWords = new BoggleHashMap(15000);
		Scanner s = null;
		try { s = new Scanner(new File("american-english")); }
		catch (FileNotFoundException ee) {
			System.err.println("No american-english dictionary found! Aborting.");
			System.exit(1);
		}
		while (s.hasNext())
			englishWords.insert(s.next().toUpperCase());
		s.close();
	}
 
	/** Used to roll the dice, and fill the Boggle board with the results.
	 */
	private void initBoard() {
		List<Character> diceRolls = new ArrayList<Character>(25);
		for (String die : dice) {
			// Choose a random side from this die, and add it to the list
			diceRolls.add(die.charAt(rgen.nextInt(6)));
		}
		
		// put the rolls into a random order
		Collections.shuffle(diceRolls, rgen);
		
		// add the rolls to the board
		Iterator<Character> diceIter = diceRolls.iterator();
		for (int row = 0; row < 5; ++row) {
			for (int col = 0; col < 5; ++col) {
				board[row][col] = diceIter.next();
			}
		}
	}
 
	/**
	 *	String representation of the Board.
	 * 	Uses Unicode "box drawing" characters to make it look all purty.
	 */
	@Override
	public String toString() {
		String s = "";
		s += "\u256d\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u256e\n";
	    for (int r = 0; r < 5; r++) {
	      s += "\u2502 ";
	      for (int c = 0; c < 5; c++) {
	        s += board[r][c];
	        if (board[r][c]=='Q') s += "u";
	        else s += " ";
	      }
	      s += "\u2502\n";
	    }
	    s += "\u2570\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u256f\n";
	    return s;
	 }
 
	 /** Given a Queue full of Strings, returns the number of points that list of
	  * Strings receives in a game of Boggle.
	  */
	 public static int countPoints(Queue<String> q) {
	   int pts = 0;
	   for (String s : q) {
	     switch(s.length()) {
	       case 0:
	       case 1:
	       case 2: break;
	       case 3:
	       case 4: pts += 1;
	               break;
	       case 5: pts += 2;
	               break;
	       case 6: pts += 3;
	               break;
	       case 7: pts += 5;
	               break;
	       default:pts += 11;
	               break;
	     }
	   }
	   return pts;
	 }
	
	 /**
	  * A method that returns a Queue filled with every word that appears in the
	  * Boggle board.
	  *
	  * @return A Queue containing all English words in the board.
	  *
	  * This has two steps:
	  * (1) Fill your data structure with all words that appear on the Boggle
	  * board.
	  * (2) Traverse that data structure and return the Queue.
	  *
	  * Remember the rules of Boggle; you can move to any of the (up to) eight
	  * surrounding blocks for the next letter, but may not reuse a block.
	  * And don't forget that a Q character automatically includes the "QU"!
	  */
	 public Queue<String> allWords() {
	   // At first, no characters have been used.
	   boolean[][] used = new boolean[5][5]; 
	 	    BoggleHashMap foundWords = new BoggleHashMap(100);
	 	    // This loop calls your helper function, starting from each position.
	   for (int r = 0; r < 5; r++)
	     for (int c = 0; c < 5; c++)
	       allWords("", r, c, used, foundWords);
	   
	   // At this point, foundWords should be filled with all the valid English
	   // words in this board.
	   return foundWords.traverse();
	 }
	 /** Helper function for the allWords() method.
	  * The whole point of this function is to fill in foundWords with
	  * all the English words on the board.
	  * May I suggest recursion???
	  *
	  * @param sofar The partial word constructed so far.
	  * @param row The row in the board to get the next letter from.
	  * @param col The column in the board to get the next letter from.
	  * @param used Indicates which board letters are already in the word.
	  * @param foundWords Holds all the actual English words on the board.
	  */
	 private void allWords(String sofar, int row, int col,
	     boolean[][] used, BoggleHashMap foundWords) 
	 {
	   // Give up on words longer than 8 letters.
	   if (sofar.length() > 8) return;
	   used[row][col] = true;
	   sofar += this.board[row][col];
	   if (this.board[row][col] == 'Q') { sofar += 'U'; }
	   if (this.englishWords.find(sofar)) { foundWords.insert(sofar); }
	   if (indexInBounds(row+1, col) && !used[row+1][col]) { allWords(sofar, row+1, col, used, foundWords); } 
	   if (indexInBounds(row-1, col) && !used[row-1][col]) { allWords(sofar, row-1, col, used, foundWords); } 
	   if (indexInBounds(row+1, col+1) && !used[row+1][col+1]) { allWords(sofar, row+1, col+1, used, foundWords); } 
	   if (indexInBounds(row-1, col-1) && !used[row-1][col-1]) { allWords(sofar, row-1, col-1, used, foundWords); }
	   if (indexInBounds(row, col+1) && !used[row][col+1]) { allWords(sofar, row, col+1, used, foundWords); }
	   if (indexInBounds(row, col-1) && !used[row][col-1]) { allWords(sofar, row, col-1, used, foundWords); } 
	   if (indexInBounds(row+1, col-1) && !used[row+1][col-1]) { allWords(sofar, row+1, col-1, used, foundWords); } 
	   if (indexInBounds(row-1, col+1) && !used[row-1][col+1]) { allWords(sofar, row-1, col+1, used, foundWords); }
	   used[row][col] = false; /* reset tracker to previous state after checking all surrounding locations */
	 }
	 
	 private boolean indexInBounds(int row, int col) {
		 return (row >= 0 && row <= 4 && col >= 0 && col <= 4); /* are the indices valid or off the board? */
	 }
}