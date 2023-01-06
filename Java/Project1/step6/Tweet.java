/** A class to store information about a tweet */
public class Tweet
{
    /** The text of the tweet */
    private String text;
    /** The account name of the user who posted the tweet */
    private String username;
    /** The year the tweet was posted */
    private int year;
    /** The month the tweet was posted */
    private int month;
    /** The day the tweet was posted */
    private int day;

    /**
     * Used to contruct a Tweet if the text, username, and date of
     * the Tweet are known.
     *
     * @param newText The text of the tweet.
     * @param newUser The username of person who posted the tweet.
     * @param newDate The date the tweet was posted.
     */
    public Tweet(String newText, String newUser, String newDate)
    {
        String[] temp = new String[3];
        text     = newText;
        username = newUser;
        temp     = newDate.split("-");
        year     = Integer.parseInt(temp[0]);
        month    = Integer.parseInt(temp[1]);
        day      = Integer.parseInt(temp[2]);
    }

    /**
     * Used to return a String of the contents of the Tweet, including the
     * the text, username, and date of the Tweet.
     *
     * @return Returns a String of the text, username, and date of the Tweet
     */
    public String toString()
    {
        String tweet = new String();
        tweet += text + "\t";
        tweet += "[" + username + "]\t";
        tweet += month + "/" + day + "/" + year;
        return tweet;
    }

    /**
     * Used to return the text of the Tweet.
     *
     * @return Returns a String of the text of the Tweet.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Used to return the year the Tweet was posted.
     *
     * @return Returns an int of the year in which the Tweet was posted.
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Used to return the month the Tweet was posted.
     *
     * @return Returns an int of the month in which the Tweet was posted.
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * Used to return the day the Tweet was posted.
     *
     * @return Returns an int of the day in which the Tweet was posted.
     */
    public int getDay()
    {
        return day;
    }

    /**
     * Used to determine whether a specified String can be found
     * within the text of the Tweet.
     *
     * @param keyword The String that is to be searched for within the Tweet.
     * @return true if the String being searched for was found within the text of the tweet
     */
    public boolean containsKeyword(String keyword){
        boolean temp = false;
        if ((text.toLowerCase()).indexOf(keyword) != -1){
            temp = true;
        }
        return temp;
    }
}