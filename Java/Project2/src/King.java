import static java.lang.Math.abs;
/**
 * The King class is extended from the Piece class and moves
 * according to the rules allowed to for a king in chess.
 * The king is able to move horizontally, vertically, or diagonally
 * under normal conditions.  It can castle if neither it nor the rook,
 * closest to the king, have moved.  The king cannot move into check
 * and must move if in check.
 */
public class King extends Piece
{
    /**
     * Tracks whether or not the king is able to partake in castling.
     */
    private boolean ableToCastle = true;

    /**
     * Constructor for the King class when given the location and color
     * 
     * @param loc Contains the location of where to place the king
     * @param color Contains either Piece.WHITE or piece.BLACK depending on its color
     */
    public King (Location loc, int color)
    {
        super(loc, color);
    }
    
    /**
     * Constructor for the King class when given the coordinates and color
     *
     * @param col The column location of the king
     * @param row The row location of the king
     * @param color Contains either piece.WHITE or piece.BLACK depending on its color
     */
    public King (char col, int row, int color)
    {
        super(col, row, color);
    }
    
    /**
     * Getter for ableToCastle
     *
     * @return Returns true if the rook can castle and false otherwise
     */
    public boolean getAbleToCastle()
    {
        return ableToCastle;
    }
    
    /**
     * A king cannot place another king in check, so the value of Piece.inCheck
     * will always be set to false when moving the king.
     */
    public void isKingInCheck()
    {
        Piece.inCheck = false;
    }
    
    /**
     * Determines whether or not the location specified by the user to move
     * to is a location is allowed to move to (i.e. the king cannot move three
     * locations forward). This method does not check if other pieces obstruct
     * the motion of the king.
     *
     * If castling, the rook closest to the position being moved to will be moved from this method.
     *
     * @exception IllegalArgumentException if the king is attemting to move into check
     * @exception IllegalArgumentException if king is attempting to castle to or through a position of check
     * @exception IllegalArgumentException if king is attempting to castle and the closest rook has already moved
     * @param toSpot Refers to the Location that the king is attempting to move to
     * @return Returns true if the Location is a valid move and false otherwise
     */
    protected boolean checkMove(Location toSpot) throws IllegalArgumentException
    {
        boolean isLegalSpot = false;
        int fromRow, fromCol, toRow, toCol, diffRow, diffCol;
        
        fromRow = loc.getRowIndex();
        fromCol = loc.getColIndex();
        toRow = toSpot.getRowIndex();
        toCol = toSpot.getColIndex();
        
        diffRow = abs(fromRow - toRow);
        diffCol = abs(fromCol - toCol);
        
        if ((diffRow == 1 || diffRow == 0) && 
            (diffCol == 1 || diffCol == 0))
        {
            //loop over entire board && check to see if king is attempting to move into check
            for (int i = 0; i < Piece.reference.length; i++)
            {
                for (int j = 0; j < Piece.reference[i].length; j++)
                {
                    //if there is a piece at a particular location, check to see if it is opposing color
                    if (Piece.reference[i][j] != null && Piece.reference[i][j].getColor() != color)
                    {
                        //Can the piece move to this location?
                        if(Piece.reference[i][j].checkMove(toSpot))
                        {
                            Location[] positions = Piece.reference[i][j].mustBeOpen(toSpot);
                            // For every element of this array INCLUDING the last one,
                            int l = 0;
                            while (l < positions.length)
                            {
                                // if there is a piece in that spot, the piece cannot impede the castle.
                                if (Piece.reference[positions[l].getRowIndex()][positions[l].getColIndex()] != null)
                                {
                                    l++;
                                    break;
                                }
                                l++;   
                            }
                            
                            if (Piece.reference[positions[l-1].getRowIndex()][positions[l-1].getColIndex()] == null)
                            {
                                throw new IllegalArgumentException("Cannot move into check");
                            }
                        }
                    }
                }
            }
        }
        else if (ableToCastle && diffRow == 0 && diffCol == 2)
        {
            Location[] castle = loc.rowLocations(loc, toSpot);
            //loop over entire board
            for (int i = 0; i < Piece.reference.length; i++)
            {
                for (int j = 0; j < Piece.reference[i].length; j++)
                {
                    //if there is a piece at a particular location, check to see if it is opposing color
                    if (Piece.reference[i][j] != null && Piece.reference[i][j].getColor() != color)
                    {
                        //Check for all the spaces the king must move through
                        for (int k = 0; k < castle.length; k++)
                        {
                            //Can the piece move to this location?
                            if(Piece.reference[i][j].checkMove(castle[k]))
                            {
                                Location[] positions = Piece.reference[i][j].mustBeOpen(castle[k]);
                                // For every element of this array INCLUDING the last one,
                                int l = 0;
                                while (l < positions.length)
                                {
                                    // if there is a piece in that spot, the piece cannot impede the castle.
                                    if (Piece.reference[positions[l].getRowIndex()][positions[l].getColIndex()] != null)
                                    {
                                        l++;
                                        break;
                                    }
                                    l++;   
                                }
                                
                                if (Piece.reference[positions[l-1].getRowIndex()][positions[l-1].getColIndex()] == null)
                                {
                                    throw new IllegalArgumentException("Cannot castle while moving through or into check");
                                }
                            }
                        }
                    }
                }
            }
            //Check to see which Rook is closer and if the rooks can castle as well
            if (toCol == 2 && Piece.reference[fromRow][0] instanceof Rook && ((Rook)(Piece.reference[fromRow][0])).getAbleToCastle())
            {
                Piece.reference[fromRow][toCol+1] = Piece.reference[fromRow][0];
                Piece.reference[fromRow][0] = null;
                isLegalSpot = true;
            }
            else if (toCol == 6 && Piece.reference[fromRow][7] instanceof Rook && ((Rook)(Piece.reference[fromRow][7])).getAbleToCastle())
            {
                Piece.reference[fromRow][toCol-1] = Piece.reference[fromRow][7];
                Piece.reference[fromRow][7] = null;
                isLegalSpot = true;
            }
            else
            {
                isLegalSpot = false;
                throw new IllegalArgumentException("Cannot castle if rook has previously moved");
            }
        }
        
        return isLegalSpot;
        
    }
    
    /**
     * Checks to see if the king can make the move to the location desired. The location
     * of the loc in the king object will be changed to the new location
     *
     * @param toSpot Contains the Location that the king is attempting to move to
     * @return Returns true if the move is made by the king
     */
    protected boolean makeMove (Location toSpot)
    {
        boolean madeMove = false;
        try
        {
            if ((madeMove = checkMove(toSpot)))
            {
                loc = toSpot;
                ableToCastle = false;
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
     * location the king is currently at and where the king is attempting
     * to move
     * 
     * @param toSpot Contains the Location the king is attempting to move to
     * @return Returns a Location array of all the points on the board between the "from" and "to" locations
     */
    public Location[] mustBeOpen(Location toSpot)
    {
       Location[] reqToBeOpen = {loc};
       return reqToBeOpen; 
    }
    
    /**
     * Creates a string representation of the king object in Unicode and is dependent its color
     *
     * @return Returns the Unicode representation of the king object dependent its color
     */
    public String toString()
    {
        String rook;
        
        if (color != Piece.WHITE)
        {
            rook = "\u2654";
        }
        else
        {
            rook = "\u265A";
        }
        
        return rook;
    }
}
