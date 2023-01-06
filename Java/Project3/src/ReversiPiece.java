
public class ReversiPiece {
	private char color;
	private final char whitePiece = '\u25CB';
	private final char blackPiece = '\u25CF';
	private ReversiLocation rloc;
	
	/**
	 * Constructor when given the char of the column, int for the row,
	 * boolean value for whose turn it is
	 * 
	 * @param col Contains the char representation of a column to put the piece in
	 * @param row Contains the int representation of a row to put the piece in
	 * @param white Contains the boolean value for whose turn it is
	 */
	protected ReversiPiece(char col, int row, boolean white){
		rloc = new ReversiLocation(col, row);
		if (white){
			color = whitePiece;
		} else {
			color = blackPiece;
		}
		
	}
	
	/**
	 * Constructor for when given a ReversiLocation and boolean value for
	 * whose turn it is
	 * 
	 * @param rloc Contains the ReversiLocation to place the piece on
	 * @param white Contains the boolean for whose turn it is
	 */
	protected ReversiPiece(ReversiLocation rloc, boolean white){
		this.rloc = rloc;
		if (white){
			color = whitePiece;
		} else {
			color = blackPiece;
		}
	}
	
	public String toString() {
		String piece;
		if (color == whitePiece){
			piece = whitePiece + "";
		} else {
			piece = blackPiece + "";
		}
		return piece;
	}
	
	/**
	 * Switches the color of a piece
	 * 
	 * @param isWhite Contains whose turn it is
	 */
	public void switchColor(boolean isWhite) {
		if (isWhite) {
			color = blackPiece;
		} else {
			color = whitePiece;
		}
	}
	
	/**
	 * Effectively a getter for the color of a piece
	 * 
	 * @return Returns true if the piece is white
	 */
	public boolean isWhite() {
		boolean isWhite = true;
		if (color == blackPiece) {
			isWhite = false;
		}
		
		return isWhite;
	}
}
