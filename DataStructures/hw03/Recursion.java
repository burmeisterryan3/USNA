/* This is really a linked list class for a linked list of Strings.
 * No loops allowed in these parts!
 */
public class Recursion {

  // Class for the links in the linked list.
  // No getters and setters necessary because the fields are public.
  private class Node {
    public String data;
    public Node next;

    public Node(String data) {
      this.data = data;
      next = null;
    }
  }

  // Pointer to the first Node
  private Node first;

  public Recursion() {
    first = null;
  }

  // Adds an element to the front of the linked list.  No recursion required.
  public void addToFront(String element) {
	  //If list is empty, the new node is the first node.
	  if (first == null) 
		  	first = new Node(element);
	  else {
		  Node newFirstNode = new Node(element);
		  //The old first new should be pointed to by the new first node
		  newFirstNode.next = first;
		  first = newFirstNode;
	  }
  }

  // Prints the data of the lists, in order.
  // You'll need a helper method, which I have started for you below.
  public void printInOrder() {
	  //Check to see if the list is empty
	  if (first != null)
		  printInOrder(first);
	  else
		  System.out.println("The list is empty!");
  }

  // Helper method
  private void printInOrder(Node node) {
	  System.out.println(node.data);
	  if (node.next != null)
		  printInOrder(node.next);
  }

  // Prints the list in reverse order, last node first.
  public void printInReverseOrder() {
	  //Make sure there is at least one node in the list
	  if (first != null)
		  printInReverseOrder(first);
	  else 
		  System.out.println("The list is empty!");
  }

  // Helper method
  private void printInReverseOrder(Node node) {
	  //Are we at the end? If so, print the string
	  if (node.next == null)
		  System.out.println(node.data);
	  else {
		  //Go to the next element in the list and print the string once the function returns
		  printInReverseOrder(node.next);
		  System.out.println(node.data);
	  }
  }

  // Gets the longest word in the list.
  // If more than one word is tied for the longest,
  // you should return the FIRST one.
  public String longest() {
	  //Check to see if the list is empty
	  if (first != null)
		  return(longest(first));
	  else
		  return("The list is empty!");
  }

  // Helper method
  private String longest(Node node) {
	  String longest = null;
	  if (node.next != null)
		  longest = longest(node.next);
	  else
		  //If at the last node, the longest string is the string of the last node.
		  longest = node.data;
	  //check to see if the value returned from the previous node is longer than
	  //the string contained in the current node.
	  if (node.data.length() >= longest.length())
		  longest = node.data;
	  return longest;
  }

  // Gets the contents of the i-th node, counting from 0.
  public String get(int i) {
	  if (first != null)
		  return get(first, i);
	  else
		  return("The list is empty!");
  }

  // Helper method
  private String get(Node node, int i) {
	  //Return the data if at the desired location
	  if (i == 0)
		  return node.data;
	  else {
		  //Ensure that the there is another node in the list before calling the next method
		  if (node.next != null)
			  return get(node.next,--i);
		  else
			  return("You have requested data from a node that does not exist!");
	  }
  }

  // Remove the i-th node and its data.
  public void remove(int i) {
	  //Ensure that the list is not empty
	  if (first != null)
		  remove(first, i);
	  else
		  System.out.println("The list is empty!");
  }

  // Helper method
  public void remove(Node node, int i) {
	  //2 Conditions: 1. The next node should be the node to remove... must stop here
	  //              2. Ensure that the node we want to remove actually exists
	  if (i == 0) {
		  first = first.next;
	  } else if (i == 1 && node.next != null) {
		  Node temp = node.next.next;
		  node.next = temp;
	  } else
		  remove(node.next, --i);
  }
}