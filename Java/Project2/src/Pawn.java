import static java.lang.Math.abs;
/**
 * The Pawn class is extended from the Piece class and moves
 * according to the rules allowed to for a pawn in chess.
 * The pawn is able to move two spots forward initially and one
 * spot forward after off its initially location.  Pawn promotion,
 * to a queen, occurs when a pawn reaches the opposing end of the board.
 * En passant is enabled and can be taken advantage of if the move is possible.
 */
public class Pawn extends Piece
{
    /**
     * Keep track of the moves sense a pawn moved two spaces from its original
     * location for en Passant purposes
     */
    public static int enPassant = 0;
    /**
     * If a pawn moves two spaces forward from its orginal position, this records
     * the column of the movement for en Passant purposes.
     */
    public static int enPassantCol = 0;
    
    /**
     * Constructor for the Pawn class when given the location and color
     * 
     * @param loc Contains the location of where to place the pawn
     * @param color Contains either Piece.WHITE or piece.BLACK depending on its color
     */
    public Pawn (Location loc, int color)
    {
        super(loc, color);
    }
    
    /**
     * Constructor for the Pawn class when given the coordinates and color
     *
     * @param col The column location of the pawn
     * @param row The row location of the pawn
     * @param color Contains either piece.WHITE or piece.BLACK depending on its color
     */
    public Pawn (char col, int row, int color)
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
     * to is a location is allowed to move to (i.e. the rook cannot move to a
     * diaganol location). This method does not check if other pieces obstruct
     * the motion of the pawn.
     *
     * @exception IllegalArgumentException if the king is in check (movement by pawn is illegal)
     * @param toSpot Refers to the Location that the pawn is attempting to move to
     * @return Returns true if the Location is a valid move and false otherwise
     */
    protected boolean checkMove(Location toSpot) throws IllegalArgumentException
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
        
        diffRow = fromRow - toRow;
        diffCol = abs(fromCol - toCol);
        
        if (color == Piece.BLACK && diffRow == 1 &&
            (diffCol == 1 || diffCol == 0))
        {
            if (diffCol== 0 && Piece.reference[toRow][toCol] != null)
            {
                throw new IllegalArgumentException("Pawn cannot take piece while " +
                "moving forward");
            }
            else if (fromRow == 3 && diffCol == 1 && Piece.reference[toRow][toCol] == null && 
                 Pawn.enPassant != 0 && toCol == Pawn.enPassantCol)
            {
                Piece.reference[toRow+1][toCol] = null;
                isLegalSpot = true;
            }
            else if (diffCol == 1 && Piece.reference[toRow][toCol] == null && Pawn.enPassant == 0)
            {
                throw new IllegalArgumentException("Pawn is not taking piece from " + 
                loc.toString() + " while moving diagonally to " + toSpot.toString());
            }
            else if (toRow == 0)
            {
                Piece.reference[fromRow][fromCol] = new Queen(new Location(fromRow, fromCol), Piece.BLACK);
                isLegalSpot = true;
            }
            else
            {
                isLegalSpot = true;
            }
        }
        else if (color == Piece.WHITE && diffRow == -1 &&
            (diffCol == 1 || diffCol == 0))
        {
            if (diffCol == 0 && Piece.reference[toRow][toCol] != null)
            {
                throw new IllegalArgumentException("Pawn cannot take piece while " +
                "moving forward");
            }
            else if (fromRow == 4 && diffCol == 1 && Piece.reference[toRow][toCol] == null && 
                Pawn.enPassant != 0 && toCol == Pawn.enPassantCol)
            {
                Piece.reference[toRow-1][toCol] = null;
                isLegalSpot = true;
            }
            else if (diffCol == 1 && Piece.reference[toRow][toCol] == null && Pawn.enPassant == 0)
            {
                throw new IllegalArgumentException("Pawn is not taking piece from " + 
                loc.toString() + " while moving diagonally to " + toSpot.toString());
            }
            else if (toRow == 7)
            {
                Piece.reference[fromRow][fromCol] = new Queen(new Location(fromRow, fromCol), Piece.WHITE);
                isLegalSpot = true;
            }
            else
            {
                isLegalSpot = true;
            }
        }
        else if (color == Piece.BLACK && diffRow == 2 &&
                 diffCol == 0 && fromRow == 6)
        {
            Pawn.enPassant = 2;
            Pawn.enPassantCol = fromCol;
            isLegalSpot = true;
        }
        else if (color == Piece.WHITE && diffRow == -2 &&
                 diffCol == 0 && fromRow == 1)
        {
            Pawn.enPassant = 2;
            Pawn.enPassantCol = fromCol;
            isLegalSpot = true;
        }
        
        return isLegalSpot;    
    }
    
    /**
     * Checks to see if the pawn can make the move to the location desired. The location
     * of the loc in the pawn object will be changed to the new location
     *
     * @param toSpot Contains the Location that the pawn is attempting to move to
     * @return Returns true if the move is made by the pawn
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
     * location the pawn is currently at and where the rook is attempting
     * to move
     *
     * Array will NOT include current location
     *
     * @param toSpot Contains the Location the pawn is attempting to move to
     * @return Returns a Location array of all the points on the board between the "from" and "to" locations
     */
    public Location[] mustBeOpen(Location toSpot)
    {   
        Location[] colMustBeOpen = {loc}; 
        
        try
        {
            if (abs(loc.getRowIndex() - toSpot.getRowIndex()) == 2)
            { 
                colMustBeOpen = loc.colLocations(loc, toSpot);
            }
        }
        catch (IllegalArgumentException iae)
        {
            iae.printStackTrace();
        }
        finally
        {
            return colMustBeOpen;
        }
    }
    
    /**
     * Creates a string representation of the pawn object in Unicode and is dependent its color
     *
     * @return Returns the Unicode representation of the pawn object dependent its color
     */
    public String toString()
    {
        String rook;
        
        if (color != Piece.WHITE)
        {
            rook = "\u2659";
        }
        else
        {
            rook = "\u265F";
        }
        
        return rook;
    }
}
