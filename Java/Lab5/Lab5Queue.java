/** A class to create and perform simple tasks on a queue.*/
public class Lab5Queue {
    /** A reference to the first object in the queue.*/
    private Node head;
    /** A reference to the last object in the queue.*/
    private Node tail;

    /**
     * Used to contruct a queue of Nodes with the content of each node 
     * being a string.
     */
    public Lab5Queue(){
        head = null;
        tail = null;
    }

    /**
     * Used to add another Node object to the queue.
     *
     *@param s The string content of a node to be added to the queue.
     */
    public void push(String s) {
        Node node = new Node(s);
        if (tail != null)
            tail.setNext(node);
        tail = node;
        if (head == null)
            head = node;
    }

    /**
     * Used to see the content of the node in the first position within the queue.
     *
     *@return Returns the string of the first node or null if the queue is empty.
     */
    public String peek() {
        if (head == null)
            return null;
        return head.getData();
    }

    /**
     * Used to remove the first node off the queue.
     *
     * @return Returns the content of the first node or null if the queue is empty.
     */
    public String pop() {
        if (head == null)
            return null;
        String s = head.getData();
        head = head.getNext();
        if (head == null)
            tail = null;
        return s;
    }
}
