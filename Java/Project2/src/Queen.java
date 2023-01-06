import static java.lang.Math.abs;
/**
 * The Queen class is extended from the Piece class and moves
 * according to the rules allowed to for a queen in chess.
 * The queen is able to move horizontally, vertically, and diagonally
 * as far as possible without any obstructions.
 */
public class Queen extends Piece
{
    
    /**
     * Constructor for the Queen class when given the location and color
     * 
     * @param loc Contains the location of where to place the queen
     * @param color Contains either Piece.WHITE or piece.BLACK depending on its color
     */
    public Queen (Location loc, int color)
    {
        super(loc, color);
    }
    
    /**
     * Constructor for the Queen class when given the coordinates and color
     *
     * @param col The column location of the queen
     * @param row The row location of the queen
     * @param color Contains either piece.WHITE or piece.BLACK depending on its color
     */
    public Queen (char col, int row, int color)
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
                    Location kingPiece = new Location(i, j);
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
     * to is a location is allowed to move to (i.e. the queen cannot in an
     * L-shape). This method does not check if other pieces obstruct the motion
     * of the queen.
     *
     * @exception IllegalArgumentException if the king is in check (movement by queen is illegal)
     * @param toSpot Refers to the Location that the queen is attempting to move to
     * @return Returns true if the Location is a valid move and false otherwise
     */
    protected boolean checkMove(Location toSpot)
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
        
        if (diffRow == 0 || diffCol == 0)
        {
            isLegalSpot = true;
        }
        else if (diffRow == diffCol)
        {
            isLegalSpot = true;
        }
        
        return isLegalSpot;
        
    }
    
    /**
     * Checks to see if the queen can make the move to the location desired. The location
     * of the loc in the queen object will be changed to the new location
     *
     * @param toSpot Contains the Location that the queen is attempting to move to
     * @return Returns true if the move is made by the queen
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
     * Will create an array of all the points on the board between the
     * location the queen is currently at and where the queen is attempting
     * to move
     *
     * Array will NOT include current location
     * 
     * @param toSpot Contains the Location the queen is attempting to move to
     * @return Returns a Location array of all the points on the board between the "from" and "to" locations
     */
    public Location[] mustBeOpen(Location toSpot)
    {
        Location[] reqToBeOpen = {loc};

        try
        {
            reqToBeOpen = loc.rowLocations(loc, toSpot);
        }
        catch (IllegalArgumentException e)
        {
            try
            {
                reqToBeOpen = loc.colLocations(loc, toSpot);
            }
            catch (IllegalArgumentException ia)
            {
                try
                {
                    reqToBeOpen = loc.diagLocations(loc, toSpot);
                }
                catch (IllegalArgumentException iae)
                {
                    iae.printStackTrace();
                }
            }  
        }
        finally
        {
            return reqToBeOpen;
        }
    }
    
    /**
     * Creates a string representation of the queen object in Unicode and is dependent its color
     *
     * @return Returns the Unicode representation of the queen object dependent its color
     */
    public String toString()
    {
        String rook;
        
        if (color != Piece.WHITE)
        {
            rook = "\u2655";
        }
        else
        {
            rook = "\u265B";
        }
        
        return rook;
    }
}
