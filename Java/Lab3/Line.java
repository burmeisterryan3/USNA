/**
 *The class will return either the length of a line segment
 *between two points or the leftmost or rightmost point.
 */

public class Line
{
    private Point cord1, cord2;
    /**
     *This is a contructor method that will initialize two points.
     */
    public Line(float x1, float y1, float x2, float y2)
    {
        cord1 = new Point();
        cord2 = new Point();
        cord1.setX(x1);
        cord1.setY(y1);
        cord2.setX(x2);
        cord2.setY(y2);
    }
    /**
     *This method will calculate the length between two coordinates.
     */
    public float getLength()
    {
        double xDiff = cord1.getX() - cord2.getX();
        double yDiff = cord1.getY() - cord2.getY();
        return (float)Math.sqrt(Math.pow(xDiff, 2.0) + Math.pow(yDiff, 2.0));
    }
    /**
     *This method will return the left-most coordinate.
     *If equal return the first coordinate.
     */
    public Point getLeftPoint()
    {
        if (cord1.getX() <= cord2.getX()){
            return cord1;
        }
        else{
            return cord2;
        }
    }
    /**
     *This method will return the right-most coordinate.
     *If equal return the second coordinate.
     */
    public Point getRightPoint()
    {
        if (cord1.getX() > cord2.getX()){
            return cord1;
        }
        else{
            return cord2;
        }
    }
}