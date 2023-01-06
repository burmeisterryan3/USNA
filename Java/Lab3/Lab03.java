/*
 *This class will take in four arguments, in the range from -1 <= n <= 1.  The first two
 *arguments will be treated as one point (x1, y1), and the last two arguments will be treated
 *as the second point.  It willl then print the leftmost point, rightmost point, and the
 *length between those two points with four decimals.
 */
public class Lab03
{
    /*
     *Main method for class Lab03
     */
    public static void main(String [] args)
    {
        //check to see if there is less than 4 arguments.
        if (args.length < 4){
            System.out.println("ERROR:  Too few command-line variables.  Four required.");
            return;
        }
        //check to see if there is more than 4 arguments.
        else if (args.length > 4){
            System.out.println("ERROR:  Too many command-line variables.  Four required.");
            return;
        }
        //Take arguments and assign them to float variables.
        float x1, y1, x2, y2;
        x1 = Float.parseFloat(args [0]);
        y1 = Float.parseFloat(args [1]);
        x2 = Float.parseFloat(args [2]);
        y2 = Float.parseFloat(args [3]);
        //Ensure that the values are between or equal to -1 and 1.
        if (x1 < -1 || x1 > 1 || x2 < -1 || x2 > 1 || y1 < -1 || y1 > 1 || y2 < -1 || y2 > 1){
            System.out.println("ERROR:  Values must be in range -1 <= n <= 1.");
            return;
        }
        //Create Line object.
        Line line = new Line(x1, y1, x2, y2);
        //Create two points and assign them values for the left- and right-most point.
        Point leftPoint = new Point();
        leftPoint = line.getLeftPoint();
        Point rightPoint = new Point();
        rightPoint = line.getRightPoint();
        //Find the length between the two points.
        float length = line.getLength();
        //Print the information for the user.
        System.out.println("Leftmost:\t " + leftPoint.toString());
        System.out.println("Rightmost:\t " + rightPoint.toString());
        System.out.format("Line length:\t %.4f\n", length);
    }
}
     