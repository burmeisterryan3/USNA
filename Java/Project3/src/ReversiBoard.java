import java.util.ArrayList;

public class ReversiBoard {
	public final boolean black = false;
	public final boolean white = true;
	private final char blank = '\u2B1C';
	private ReversiPiece[][] board = new ReversiPiece[8][8];
	
	/**
	 * Constructs the initial board for the game
	 */
	public ReversiBoard (){
		board[3][3] = new ReversiPiece(new ReversiLocation(3,3), white);
		board[3][4] = new ReversiPiece(new ReversiLocation(3,4), black);
		board[4][3] = new ReversiPiece(new ReversiLocation(4,3), black);
		board[4][4] = new ReversiPiece(new ReversiLocation(4,4), white);
	}
	
	public boolean isBoardFull() {
		boolean boardFull = true;
		for (int i = 0; boardFull && i < 8; i++){
			for (int j = 0; boardFull && j < 8; j++) {
				if (board[i][j] != null){
					boardFull = !boardFull;
				}
			}
		}
		return boardFull;
	}
	
	/**
	 * Determines if a location already has a piece on it
	 * 
	 * @param move Contains the location attempting to be moved to
	 * @return Returns true if the location already has a piece on it
	 */
	public boolean isFull(String move) {
		boolean isFull = false;
		ReversiLocation moveReversiLocation = new ReversiLocation(move);
		
		int r = moveReversiLocation.getRowIndex();
	    int c = moveReversiLocation.getColIndex();
		if (board[r][c] != null){
			isFull = true;
		}
		
		return isFull;
	}
	
	/**
	 * Makes the move for a given location <br/>
	 * Will flip all pieces necessary for a given move
	 * 
	 * @param move Contains the location attempting to be moved to
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if the move is made
	 */
	public boolean makeMove(String move, boolean isWhite) {
		boolean madeMove = false;
		ReversiLocation moveReversiLocation = new ReversiLocation(move);
		
		int r = moveReversiLocation.getRowIndex();
	    int c = moveReversiLocation.getColIndex();
	    
    	if (rightReversiLocationsFlipped(moveReversiLocation, isWhite)){
    		madeMove = true;
    	}
    	if (leftReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove) {
    		madeMove = true;
    	}
    	if (upReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove){
    		madeMove = true;
    	}
    	if (downReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove){
    		madeMove = true;
    	}
    	if (upRightDiagReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove){
    		madeMove = true;
    	}
    	if (downRightDiagReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove){
    		madeMove = true;
    	}
    	if (upLeftDiagReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove){
    		madeMove = true;
    	}
    	if (downLeftDiagReversiLocationsFlipped(moveReversiLocation, isWhite) && !madeMove){
    		madeMove = true;
    	}
    	if (madeMove) {
    		board[r][c] = new ReversiPiece(moveReversiLocation, isWhite);
    	}
	    
		return madeMove;
	}
	
	/**
	 * Checks for the pieces to be flipped on the right side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean rightReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex();
		int col = start.getColIndex() + 1;
		
		ReversiLocation[] array = null;
	    while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		col++;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped on the left side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean leftReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex();
		int col = start.getColIndex() - 1;
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		col--;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped on the upper side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean upReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex() + 1;
		int col = start.getColIndex();
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		row++;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped below the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean downReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex() - 1;
		int col = start.getColIndex();
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		row--;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped on the upper-right side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean upRightDiagReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex() + 1;
		int col = start.getColIndex() + 1;
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		row++;
	    		col++;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped on the upper-left side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean upLeftDiagReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex() + 1;
		int col = start.getColIndex() - 1;
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		row++;
	    		col--;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped on the lower-left side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean downLeftDiagReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex() - 1;
		int col = start.getColIndex() - 1;
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		row--;
	    		col--;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Checks for the pieces to be flipped on the lower-right side of the move
	 * 
	 * @param start Contains the location will be placed at
	 * @param isWhite Contains whose turn it is
	 * @return Returns true if pieces have been flipped
	 */
	public boolean downRightDiagReversiLocationsFlipped(ReversiLocation start, boolean isWhite) {
		boolean piecesFlipped = false;
		ArrayList<ReversiLocation> capture = new ArrayList<ReversiLocation>();
		int row = start.getRowIndex() - 1;
		int col = start.getColIndex() + 1;
		
		ReversiLocation[] array = null;
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != isWhite) {
	    		ReversiLocation add = new ReversiLocation(col, row);
	    		capture.add(add);
	    		row--;
	    		col++;
	    	} else {
	    		array = capture.toArray(new ReversiLocation[capture.size()]);
	    		break;
	    	}
	    }
	    if (array != null && array.length != 0) {
	    	this.flipPieces(array);
	    	piecesFlipped = true;
	    }
	    return piecesFlipped;
	}
	
	/**
	 * Flips the pieces if a move requires certain pieces to be flipped
	 * 
	 * @param array Contains an array of all the pieces to be flipped
	 */
	public void flipPieces(ReversiLocation[] array) {
		for (int i = 0; i < array.length; i++) {
			int row = array[i].getRowIndex();
			int col = array[i].getColIndex();
			
			board[row][col].switchColor(board[row][col].isWhite());
		}
	}
	
	/**
	 * Returns a string statement to inform the user of the score
	 * 
	 * @return Returns a String of the score
	 */
	public String getScore() {
		int numWhite = 0, numBlack = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != null && board[i][j].isWhite()){
					numWhite++;
				} else if (board[i][j] != null){
					numBlack++;
				}
			}
		}
		return (" Score: Black " + numBlack + " White " + numWhite);
	}
	
	/**
	 * Determines if a player is capable of making a move during his/her turn
	 * 
	 * @param whiteTurn Contains whose move it is
	 * @returnm Returns true if a player can make a move
	 */
	public boolean isAbleToMove(boolean whiteTurn) {
		boolean canMove = false;
		for (int i = 0; i < board.length && !canMove; i++){
			for (int j = 0; j < board[i].length && !canMove; j++){
				if (board[i][j] != null && board[i][j].isWhite() == whiteTurn){
					if (canMakeMove(i, j, whiteTurn)){
						canMove = true;
					}
				}
			}
		}
		return canMove;
	}
	
	/**
	 * Checks each of the directions to ensure that a user can make a move
	 * 
	 * @param row Contains row of move to be checked
	 * @param col Contains column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if a move can be made
	 */
	public boolean canMakeMove(int row, int col, boolean whiteTurn){
		boolean canMakeMove = false;
		if (canMoveRight(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveLeft(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveUp(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveDown(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveUpRight(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveDownRight(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveUpLeft(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		if (!canMakeMove && canMoveDownLeft(row, col, whiteTurn)){
    		canMakeMove = true;
    	}
		return canMakeMove;
	}
	
	/**
	 * Checks to see if there are any spaces left on the board
	 * 
	 * @return Returns true if the board is full
	 */
	public boolean noMoreSpace(){
		boolean noSpaces = true;
		int numSpaces = 0;
		for (int i = 0; i < board.length && noSpaces; i++){
			for (int j = 0; j < board[i].length && noSpaces; j++){
				if (board[i][j] == null){
					numSpaces++;
				}
			}
		}
		
		if (numSpaces > 0){
			noSpaces = false;
		}
		return noSpaces;
	}
	
	/**
	 * Counts the number of black pieces
	 * 
	 * @return Returns the number of black pieces
	 */
	public int numBlack() {
		int numBlack = 0;
		for (int i = 0; i < board.length; i++ ){
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] != null && !board[i][j].isWhite()){
					numBlack++;
				}
			}
		}
		return numBlack;
	}
	
	/**
	 * Counts the number of white pieces
	 * 
	 * @return Returns the number of white pieces
	 */
	public int numWhite(){
		int numWhite = 0;
		for (int i = 0; i < board.length; i++ ){
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] != null && board[i][j].isWhite()){
					numWhite++;
				}
			}
		}
		return numWhite;
	}
	
	/**
	 * Checks to see if a move can be made to the right without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveRight(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		col++;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		col++;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (col == 8){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;
	}
	
	/**
	 * Checks to see if a move can be made to the left without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveLeft(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		col--;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		col--;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (col < 0){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;	
	}
	
	/**
	 * Checks to see if a move can be made to the upper-right without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveUpRight(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		row++;
		col++;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		row++;
	    		col++;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (row == 8 || col == 8){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;
	}
	
	/**
	 * Checks to see if a move can be made to the lower-right without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveDownRight(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		row--;
		col++;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		row--;
	    		col++;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (row < 0 || col == 8){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
	    return canMove;
	}
	
	/**
	 * Checks to see if a move can be made to the lower-left without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveDownLeft(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		row--;
		col--;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		row--;
	    		col--;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (row < 0 || col < 0){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;
	}
	
	/**
	 * Checks to see if a move can be made to the upper-left without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveUpLeft(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		row++;
		col--;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		row++;
	    		col--;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (row == 8 || col < 0){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;
	}
	
	/**
	 * Checks to see if a move can be made down without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveDown(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		row--;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		row--;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (row < 0){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;
	}
	
	/**
	 * Checks to see if a move can be made up without flipping any pieces
	 * or making the move
	 * 
	 * @param row Row of move to be checked
	 * @param col Column of move to be checked
	 * @param whiteTurn Contains whose turn it is
	 * @return Returns true if the move can be made
	 */
	private boolean canMoveUp(int row, int col, boolean whiteTurn){
		boolean canMove = false;
		int possiblePiecesCaptured = 0;
		row++;
		
		while (row < 8 && row >= 0 && col < 8 && col >= 0) {
	    	if (board[row][col] == null) {
	    		break;
	    	} else if (board[row][col].isWhite() != whiteTurn) {
	    		possiblePiecesCaptured++;
	    		row++;
	    	} else {
	    		possiblePiecesCaptured = 0;
	    		break;
	    	}
	    }
		
		if (row == 8){
			possiblePiecesCaptured = 0;
		}
		
		if (possiblePiecesCaptured > 0){
			canMove = true;
		}
		
	    return canMove;
	}
	public String toString() {
	    String s = "\n  ";

	    // print the column chars across the top
	    for (int c = 0; c < 8; c++)
	      s+= (char)(c+'A') + " ";
	    //end the column labels line
	    s+='\n';

	    // for each row,
	    for (int r = 0; r < 8; r++) {
	      // print the label for the row,
	      s+= r + 1 + " ";

	      // and for each column,
	      for (int c = 0; c < 8; c++) {
	        // if that spot in the board contains a Piece object,
	        if (board[r][c] != null) {
	          // call that piece's toString()
	          s+=board[r][c].toString();
	          s+=' ';
	        } else {
	        	s+= blank + " ";
	        }
	      }
	      // newline at the end of each row
	      s+='\n';
	    }
        return s;
	}
}
