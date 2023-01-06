import java.util.Scanner;
import java.io.File;
/*
 *This class represents the main class for searching through a file of tweets.
 */
public class Search
{
    /*
     *Method to read input file from command line.
     */
    public static Tweet[] readFile(String file)
    {
        //String for entire line.
        String readIn = new String();
        //String to split line into three separate parts.
        String[] line = new String[3];
        //Array or tweets.
        Tweet[] tweet = new Tweet[33];
        //Read in from file and catch if file is unreadable or nonexistent.
        try {
            Scanner in = new Scanner(new File(file), "utf-8");
            for (int i = 0; in.hasNextLine(); i++){
                readIn = in.nextLine();
                line = readIn.split("\t");
                tweet[i] = new Tweet(line[0], line[1], line[2]);
            }
        }
        catch( Exception ex ) {
            ex.printStackTrace();
            System.exit(0);
        }
        return tweet;
    }
    /*
     *Main method within the Search class
     */
    public static void main(String[] args)
    {
        //String to store filename.
        String filepath = new String();
        filepath = args[0];
        //Return array of tweets. 
        Tweet[] tweets = readFile(filepath);
        //Print all tweets to screen.
        System.out.println("Array size: 33");
        for (int i = 0; i < 33; i++){
            System.out.println(tweets[i]);
        }
    }
}