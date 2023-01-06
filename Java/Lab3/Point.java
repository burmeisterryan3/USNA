/**
 *This class will obtain x & y coordinates and converts this
 *to a standard String format (x,y). 
 */

public class Point
{
    private float xCord, yCord;
    /**
     *This method initializes xCord and yCord to 0.
     */
    public Point()
    {
        xCord = 0f;
        yCord = 0f;
    }
    /**
     *This method obtains a x-coordinate from user input.
     */
    public float getX()
    {
        return xCord;
    }
    /**
     *This method obtains a y-coordinate from user input.
     */
    public float getY()
    {
        return yCord;
    }
    /**
     *This method sets the value of x from user input.
     */
    public void setX(float xInput)
    {
        xCord = xInput;
    }
    /**
     *This method sets the value of y from user input.
     */
    public void setY(float yInput)
    {
        yCord = yInput;
    }
    /**
     *This method converts the x- and y- coordinates to string.
     */
    public String toString()
    {
        return ("(" + xCord + "," + yCord + ")");
    }
}