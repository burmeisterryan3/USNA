/*
 *This class allows the user to interact with the queue of tweets.
 */
public class Queue {
    private Node first;
    private Node last;

    /*
     *Method to add additional node onto queue.
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
    }
    /*
     *Method to look at what is in the first point of the array.
     */
    public Tweet peek()
    {
        //If nothing is in queue return a null value.
        if (first.data == null){
            return null;
        }
        return first.data;
    }
    /*
     *Method to take tweet first in queue off of the queue.
     */
    public Tweet pop()
    {
        //If nothing is in queue return a null value.
        if (first.data == null){
            return null;
        }
        //Create temp to store tweet intended to be popped off.
        Tweet temp = new Tweet();
        temp = first.data;
        first = first.next;
        //If the queue is empty then set the node pointing to the last
        //tweet in the queue to null.
        if (first == null){
            first = null;
        }
        return temp;
    }
    /*
     *Method to print all of the tweets in the queue.
     */
    public void printAll()
    {
        Node printer = new Node();
        printer = first;
        while (printer != null){
            System.out.println("\"" + printer.data.getText() + "\"");
            printer = printer.next;
        }
    }
    /*
     *Method to count the number of tweets in the queue.
     */
    public int currentSize()
    {
        Node counter = new Node();
        counter = first;
        int i;
        for (i = 0; counter != null; i++){
            counter = counter.next;
        }
        return i;
    }
}
 
