/*
 *This class will represent Tweets from the input file.
 */
public class Tweet
{
    private String text = new String();
    private String username = new String();
    private int year;
    private int month;
    private int day;

    /*
     *Default constructor.
     */
    public Tweet()
    {
        text = null;
        username = null;
        year = month = day = 0;
    }
    /*
     *Tweet constructor for an input with text, a username, and a date.
     */
    public Tweet(String newText, String newUser, String newDate)
    {
        //Create temporary string for reading newDate before parsing to int.
        String[] temp = new String[3];
        text     = newText;
        username = newUser;
        //Split the string with the date into ints for year, month, and day.
        temp     = newDate.split("-");
        year     = Integer.parseInt(temp[0]);
        month    = Integer.parseInt(temp[1]);
        day      = Integer.parseInt(temp[2]);
    }

    /*
     *Copy tweet information to a single string object.
     */
    public String toString()
    {
        String tweet = new String();
        //Copy content of tweet.
        tweet += text + "\t";
        //Copy the name of the user who posted tweet.
        tweet += "[" + username + "]\t";
        //Copy the date when the tweet was created.
        tweet += month + "/" + day + "/" + year;
        return tweet;
    }

    /*
     *Getter which returns just the text of a tweet.
     */
    public String getText()
    {
        return text;
    }
}