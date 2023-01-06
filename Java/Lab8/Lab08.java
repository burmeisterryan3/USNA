import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.NullPointerException;

/*
 * Class that will read from a file and determine
 * if a data element is an int, float, or String
 */
public class Lab08
{
    /* 
     * Method which converts a string to an int
     * 
     * @exception NotAnIntException if the string cannot be converted to an int
     * @param s String to be converted to an int
     * @return Returns the int converted from the String parameter
     */
    public int stringToInteger(String s) throws NotAnIntException
    {
        try
        {
            int parsedInt = Integer.parseInt(s);
            return parsedInt;
        }
        catch(Exception e)
        {
            throw new NotAnIntException();
        }
    }
    
    /* 
     * Method which converts a string to an float
     * 
     * @exception NotAnIntException if the string cannot be converted to a float
     * @param s String to be converted to a float
     * @return Returns the float converted from the String parameter
     */
    public float stringToFloat(String s) throws NotAFloatException
    {
        try
        {
            float parsedFloat = Float.parseFloat(s);
            return parsedFloat;
        }
        catch (Exception e)
        {
            throw new NotAFloatException();
        }
    }
    
    /*
     * Method which will attempt to read a file until its completion and
     * print whether or not the data is an int or a float.  If it is neither
     * of these, it should print a string of the data element.
     *
     * @param filepath Filepath to be read from
     */
    public void readFile(String filepath)
    {
        Scanner fin = null;
        try
        {
            fin = new Scanner(new File(filepath));
            while (fin.hasNext())
            {
                String next = fin.next();
                
                try
                {
                    int newInt = stringToInteger(next);
                    System.out.println("INT:\t" + newInt);
                }
                catch (NotAnIntException naie)
                {
                    try
                    {
                        float newFloat = stringToFloat(next);
                        System.out.println("FLOAT:\t" + newFloat);
                    }
                    catch (NotAFloatException nafe)
                    {
                        System.out.println("STRING:\t" + next);
                    }
                }
            }
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println("I did not find the file " + filepath);
        }
        finally
        {
            fin.close();
        }
    }
    
    public static void main(String[] args)
    {
        Lab08 lab = new Lab08();
        try
        {
            lab.readFile(args[0]);
        }
        catch(ArrayIndexOutOfBoundsException aioobe)
        {
            System.out.println("You didn't give me a file!");
        }
        catch(NullPointerException npe)
        {
        }
    }
}
