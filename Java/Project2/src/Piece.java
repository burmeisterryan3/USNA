/*
 * An abstract class which contains the general purpose methods for each
 * of the methods necessary for all pieces of chess.
 */
public abstract class Piece
{
    //Constant which contains the color of the piece, if black
    public static final int BLACK = 0;
    //Constant which contains the color of the piece, if white
    public static final int WHITE = 1;
    /**
     * Reference to the board which allows pieces to to communicate with the
     * board when necessary.  This includes instances when castling, pawn promotion,
     * pawn movement diagonally, en Passant, and rules of check with the king.
     */
    public static Piece[][] reference;
    //Tracks whether the movement of a piece puts the opposing king in check
    public static boolean inCheck = false;
    
    //Contains the color of the object
    protected int color;
    //Contains the Location of the object
    protected Location loc;
    
    /**
     * Constructor for Piece when given the coordinates of the initial Location
     * and the color of the object.
     */
    protected Piece (char col, int row, int color)
    {
        loc = new Location(col, row);
        this.color = color;
    }
    
    /**
     * Constructor for Piece when given the Location of the object and its color
     */
    protected Piece (Location loc, int color)
    {
        this.loc = loc;
        this.color = color;
    }
    
    /**
     * Getter for the location of the piece object
     *
     * @return Returns the location of the piece object
     */
    public Location getLoc()
    {
        return loc;
    }
    
    /**
     * Getter for the color of the piece object
     * 
     * @return Returns the color of the piece object
     */
    public int getColor()
    {
        return color;
    }
    
    /**
     * Abstract method to be implemented to determine if a move is capable
     * of being made by a particular child of the piece class
     * Called by the makeMove method
     *
     * @param toSpot the location desired to be mvoed to
     * @return Returns true if the move is capable of being made and false otherwise
     */
    protected abstract boolean checkMove (Location toSpot);
    /**
     * Abstract method to be implemented to determine the array of locations between
     * a piece's child current location and the desired location
     *
     * @param toSpot the location desired to be moved to
     * @return Returns the array of Locations between the current and desired location
     */
    public abstract Location[] mustBeOpen (Location toSpot);
    /**
     * Abstract method to move a piece if the move is capable of being made.  The location
     * of the loc in the specific piece object will be changed.
     *
     * @param toSpot the location desired to be moved to
     * @return Returns true if the move is capable of being  
     */    
    protected abstract boolean makeMove (Location toSpot);
    /**
     * Abstract method to create a string representation of a specific piece
     *
     * @return Returns the string representation of the specific piece object
     */
    public abstract String toString();
    /**
     * Will set the static variable from the Piece class inCheck to true if the
     * opposing king in check as a reult of the movement of the rook and false otherwise
     */
    public abstract void isKingInCheck();   
}
