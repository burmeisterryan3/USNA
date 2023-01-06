import java.util.Scanner;
import java.io.File;
/**
 * A class used to search for text within file of tweets using
 * the Queue, Node, and Tweet classes.
 */
public class Search
{
    /**
     * Used to create a queue given a file of tweets. Tweets will
     * be placed in a node and into a queue of Tweets.
     *
     * @param file Contains the name of the file of tweets.
     * @return Returns the queue created from the file of tweets.
     */
    public static Queue readFile(String file)
    {
        Queue queue = new Queue();
        String readIn = new String();
        String[] line = new String[3];
        try {
            Scanner in = new Scanner(new File(file), "utf-8");
            for (int i = 0; in.hasNextLine(); i++){
                readIn = in.nextLine();
                line   = readIn.split("\t");
                Tweet tweet  = new Tweet(line[0], line[1], line[2]);
                queue.push(tweet);
            }
        } catch( Exception ex ) {
            ex.printStackTrace();
            System.exit(0);
        }
        return queue;
    }
    /**
     * Main method which will allow user to enter certain commands
     * to find tweets within the queue. 
     *
     * "!quit"  will terminate the program.  
     * "!reset" will reset the queue containing the tweets
     *          searched for and reset it back to the original queue.  
     * "!dump"  prints the tweets that have been searched for to the screen.
     * Beginning an input with "-" will filter the search so only to 
     * return those tweets not having the text following the "-".  
     * The "+" allows the user to enter a date to be searched for.  It will
     * return all those tweets submitted on that particular date.
     *
     * @param args Contains arguments entered on command line by user.
     */
    public static void main(String[] args)
    {
        String filepath = new String();
        filepath = args[0];
        Queue queue = readFile(filepath);
        System.out.println("Queue size: " + queue.currentSize());
        Queue findWord = queue;
        Scanner scanner = new Scanner(System.in);
        String input = new String();
        do {
            System.out.print("? ");
            input = scanner.next();
            if (input.equals("!quit")){
                System.out.println("Goodbye!");
                break;
            }
            else if (input.equals("!dump")){
                findWord.printAll();
            }
            else if (input.equals("!reset")){
                findWord = queue;
            }
            else if (input.charAt(0) == '-'){
                input = input.substring(1);
                findWord = findWord.filterForNotKeyword(input);
            }
            else if (input.charAt(0) == '+'){
                input = input.substring(1);
                findWord = findWord.filterForDate(input);
            }
            else {
                findWord = findWord.filterForKeyword(input);
            }
            System.out.println("Queue size: " + findWord.currentSize());
        } while(!input.equals("!quit"));
    }
}