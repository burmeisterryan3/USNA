/** A class to organize Tweets in a queue */
public class Queue {
    /** A reference to the first Tweet in the queue */
    private Node first;
    /** A reference to the last Tweet in the queue */
    private Node last;
    /** Contains the number of Tweets within the queue */
    private int size;

    /**
     * Used to add a Tweet to the end of the queue.
     *
     * @param newTweet The text, username, and date of the Tweet to be added
     */
    public void push(Tweet newTweet)
    {
        Node node = new Node();
        node.data = newTweet;
        if (last != null){
            last.next = node;
        }
        last = node;
        if (first == null){
            first = node;
        }
        size++;
    }
    /**
     * Used to see what Tweet is at the front of the queue.
     *
     * @return Returns a String of the text of the Tweet or null if the queue is empty.
     */
    public Tweet peek()
    {
        if (first.data == null){
            return null;
        }
        return first.data;
    }
    /**
     * Used to obtain the text, username, and date of the Tweet at the front of the queue.
     * Will remove this Tweet from the queue.
     *
     * @return Returns the Tweet object at the front of the queue or null if the queue is empty.
     */
    public Tweet pop()
    {
        if (first.data == null){
            return null;
        }
        Tweet temp = first.data;
        first = first.next;
        if (first == null){
            last = null;
        }
        size--;
        return temp;
    }
    /**
     * Used to print the text, username, and date of each of the Tweets of the queue.
     */
    public void printAll()
    {
        Node printer = first;
        while (printer != null){
            System.out.println(printer.data);
            printer = printer.next;
        }
    }
    /**
     * Used to obtain the number of Tweets within the queue.
     *
     * @return Returns an int of the number of Tweets within the queue.
     */
    public int currentSize()
    {
        Node counter = first;
        int i;
        for (i = 0; counter != null; i++){
            counter = counter.next;
        }
        return i;
    }
    /**
     * Used to filter Tweets within a queue.  Will search for the
     * the specified String within each Tweet and eliminate those without
     * this String.
     *
     * @param keyword The String to be searched for within the queue of Tweets.
     * @return Returns a queue of the Tweets which contained the specified
     *         String within their text.
     */
    public Queue filterForKeyword(String keyword)
    {
        Queue matching = new Queue();
        Node temp = this.first;
        for (int i = 0; i < this.size; i++){
            if (temp.data.containsKeyword(keyword)){
                matching.push(temp.data);
            }
            temp = temp.next;
        }
        return matching;
    }

    /**
     * Used to filter Tweets within a queue.  Will search for the
     * specified String within each Tweet and eliminate those containing
     * the String.
     *
     * @param keyword The String to be searched for within the queue of Tweets.
     * @return Returns a queue of the Tweets which did NOT contain the specified
     *         String within their text.
     */
    public Queue filterForNotKeyword(String keyword)
    {
        Queue matching = new Queue();
        Node temp = this.first;
        for (int i = 0; i < this.size; i++){
            if (!temp.data.containsKeyword(keyword)){
                matching.push(temp.data);
            }
            temp = temp.next;
        }
        return matching;
    }

    /**
     * Used to filter Tweets within a queue.  Will search for the
     * specified date, represented as a String, within each Tweet and
     * elinate those that do not have the matching date.
     *
     * @param keyword The String representation of the date to be searched for
     *                within the queue of Tweets.
     * @return Returns a queue of the Tweets which contained the date entered as
     *         the String parameter.
     */
    public Queue filterForDate(String keyword)
    {
        Queue matching = new Queue();
        Node temp = this.first;
        String[] date = keyword.split("-");
        for (int i = 0; i < this.size; i++){
            if (temp.data.getYear()  == Integer.parseInt(date[0]) && 
                temp.data.getMonth() == Integer.parseInt(date[1]) && 
                temp.data.getDay()   == Integer.parseInt(date[2]) ){
                matching.push(temp.data);
            }
            temp = temp.next;
        }
        return matching;
    }
}
 
