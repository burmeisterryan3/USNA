import static java.lang.Math.abs;
/**
 * The Knight class is extended from the Piece class and moves
 * according to the rules allowed to for a knight in chess.
 * The knight is able to move in an L-shape.  The knight will
 * move forward or sideways three spaces and perpendicular one space.
 */
public class Knight extends Piece
{

    /**
     * Constructor for the Knight class when given the location and color
     * 
     * @param loc Contains the location of where to place the knight
     * @param color Contains either Piece.WHITE or piece.BLACK depending on its color
     */
    public Knight (Location loc, int color)
    {
        super(loc, color);
    }
    
    /**
     * Constructor for the Knight class when given the coordinates and color
     *
     * @param col The column location of the knight
     * @param row The row location of the knight
     * @param color Contains either piece.WHITE or piece.BLACK depending on its color
     */
    public Knight (char col, int row, int color)
    {
        super(col, row, color);
    }
    
    /**
     * Will set the static variable from the Piece class inCheck to true if the
     * opposing king in check as a reult of the movement of the rook and false otherwise
     */
    public void isKingInCheck()
    {
        //Find King of opposite color
        for (int i = 0; i < Piece.reference.length; i++)
        {
            for (int j = 0; j < Piece.reference[i].length; j++)
            {
                if (Piece.reference[i][j] != null && Piece.reference[i][j] instanceof King &&
                Piece.reference[i][j].getColor() != color)
                {
                    //record location of king
                    Location kingPiece = new Location(j, i);
                    //Find pieces which could have king in check
                    for (int ii = 0; ii < Piece.reference.length; ii++)
                    {
                        for (int jj = 0; jj < Piece.reference[ii].length; jj++)
                        {
                            if (Piece.reference[ii][jj] != null &&
                                Piece.reference[ii][jj].getColor() == color)
                            {
                                //Can the piece move to the King's location?
                                if(Piece.reference[ii][jj].checkMove(kingPiece))
                                {
                                    Location[] positions = Piece.reference[ii][jj].mustBeOpen(kingPiece);
                                    int l = 0;
                                    while (l < positions.length)
                                    {
                                        if (Piece.reference[positions[l].getRowIndex()][positions[l].getColIndex()] != null)
                                        {
                                            l++;
                                            break;
                                        }
                                        l++;   
                                    }
                                    
                                    if (Piece.reference[positions[l-1].getRowIndex()][positions[l-1].getColIndex()] instanceof King)
                                    {
                                        Piece.inCheck = true;
                                    }
                                    else
                                    {
                                        Piece.inCheck = false;
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Determines whether or not the location specified by the user to move
     * to is a location is allowed to move to (i.e. the knight cannot move to a
     * horizontal location). This method does not check if other pieces obstruct
     * the motion of the knight.
     *
     * @exception IllegalArgumentException if the king is in check (movement by knight is illegal)
     * @param toSpot Refers to the Location that the knight is attempting to move to
     * @return Returns true if the Location is a valid move and false otherwise
     */
    protected boolean checkMove (Location toSpot)
    {
        boolean isLegalSpot = false;
        
        if (Piece.inCheck)
        {
            throw new IllegalArgumentException("King is in check");
        }
        
        int fromRow, fromCol, toRow, toCol, diffRow, diffCol;
        
        fromRow = loc.getRowIndex();
        fromCol = loc.getColIndex();
        toRow = toSpot.getRowIndex();
        toCol = toSpot.getColIndex();
        
        diffRow = abs(fromRow - toRow);
        diffCol = abs(fromCol - toCol);
        
        if (diffRow == 2 && diffCol == 1)
        {
            isLegalSpot = true;
        }
        else if (diffCol == 2 && diffRow == 1)
        {
            isLegalSpot = true;
        }
        
        return isLegalSpot;
    }
    
    /**
     * Checks to see if the knight can make the move to the location desired. The location
     * of the loc in the knight object will be changed to the new location
     *
     * @param toSpot Contains the Location that the knight is attempting to move to
     * @return Returns true if the move is made by the knight
     */
    protected boolean makeMove (Location toSpot)
    {
        boolean madeMove = false;
        
        try
        {
            if (madeMove = checkMove(toSpot))
            {
                loc = toSpot;
                if (Pawn.enPassant != 0)
                {
                    Pawn.enPassant--;
                }
            }
        }
        catch(IllegalArgumentException iae)
        {
            iae.printStackTrace();
        }
        
        return madeMove;
    }
    
    /**
     * Returns an array of only the current location as none of the pieces between
     * the "from" and "to" locations will obstruct from moving to its desired location
     *
     * @param toSpot Contains the Location the knight is attempting to move to
     * @return Returns only the current location as the knight can skip over pieces
     */
    public Location[] mustBeOpen (Location toSpot)
    {
        Location[] empty = {loc};
        return empty;
    }
    
    /**
     * Creates a string representation of the knight object in Unicode and is dependent its color
     *
     * @return Returns the Unicode representation of the knight object dependent its color
     */
    public String toString()
    {
        String knight;
        
        if (color != Piece.WHITE)
        {
            knight = "\u2658";
        }
        else
        {
            knight = "\u265E";
        }
        
        return knight;        
    }
}
