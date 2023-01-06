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
    public static Queue readFile(String file)
    {
        Queue queue = new Queue();
        //String for entire line.
        String readIn = new String();
        //String to split line into three separate parts.
        String[] line = new String[3];
        //Read in from file and catch if file is unreadable or nonexistent.
        try {
            Scanner in = new Scanner(new File(file), "utf-8");
            for (int i = 0; in.hasNextLine(); i++){
                readIn = in.nextLine();
                line   = readIn.split("\t");
                Tweet tweet  = new Tweet(line[0], line[1], line[2]);
                queue.push(tweet);
            }
        }
        catch( Exception ex ) {
            ex.printStackTrace();
            System.exit(0);
        }
        return queue;
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
        Queue queue = readFile(filepath);
        //Print the number of tweets and all tweets to screen.
        System.out.println("Queue size: " + queue.currentSize());
        //
        Queue findWord = queue;
        Scanner scanner = new Scanner(System.in);
        String input = new String();
        do {
            System.out.print("? ");
            input = scanner.next();
            //Check to see if user wants to exit the program.
            if (input.equals("!quit")){
                System.out.println("Goodbye!");
            }
            //Check to see if the user wants to print all the elements of the queue with search information.
            else if (input.equals("!dump")){
                findWord.printAll();
                System.out.println("Queue size: " + findWord.currentSize());
            }
            //If none of the above options, search for the word within the main queue.
            else {
                findWord = findWord.filterForKeyword(input);
                System.out.println("Queue size: " + findWord.currentSize());
            }
        } while(!input.equals("!quit"));
    }
}