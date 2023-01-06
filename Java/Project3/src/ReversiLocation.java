/**
 * Class that allows for reference to a location on a ReversiBoard
 *
 */
public class ReversiLocation {
	private int row;
	private char col;
	
	/**
	 * Constructor when given a char for col and
	 * int for the row
	 * 
	 * @param col Contains the char representation of the column
	 * @param row Contains the user row value representation of the row
	 */
	public ReversiLocation(char col, int row) {
	    this.row = row;
	    this.col = col;
	}
	
	/**
	 * Constructor when given an int value for both the row and column
	 * 
	 * @param colIndex Contains the column index for the location
	 * @param rowIndex Contains the row index for the location
	 */
	public ReversiLocation(int colIndex, int rowIndex){
		this((char)('a'+colIndex), rowIndex+1);
	}
	
	/**
	 * Constructor when given the string representation of the move
	 * 
	 * @param move String representation of the move to be translated
	 */
	public ReversiLocation(String move) {
		move = move.toLowerCase();
		char col = move.charAt(0);
		int row;
	    try {
	      Integer i = Integer.decode(move.substring(1,2));
	      row = i.intValue();
	    } catch (NumberFormatException nfe) {
	      throw new IllegalArgumentException(move + " could not be parsed into " +
	          "a Location");
	    }
        checkRowCol(col, row);
	    this.row = row;
	    this.col = col;
	}
	
	/**
	 * Retrieves the computer science equivalent of the row
	 * 
	 * @return Returns the row index to be used more efficiently with arrays
	 */
	public int getRowIndex() {
	    return row-1;
	}

	/**
	 * Retrieves the computer science equivalent of the column
	 * 
	 * @return Returns the column index to be used more efficiently with arrays
	 */
	public int getColIndex() {
	    return (col-'a');
	}
	
	public void checkRowCol(char col, int row) throws IllegalArgumentException {
	    if (row < 1 || row > 8)
	      throw new IllegalArgumentException("Row is bad: " + row);
	    if (col < 'a' || col > 'h')
	      throw new IllegalArgumentException("Column is bad: " + col);
	}
}
