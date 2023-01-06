import java.util.Scanner;

public class Lab02
{
  /*
    Method Definitions
  */

  //Add nodes to the back of the queue
  public static void push (Queue queue, String nextString)
  {
    Node nn = new Node();
    //check to see if queue is empty
    if (queue.first == null && queue.last == null)
      {
	//Point the first and last node at first node and get string
	nn.text = nextString;
	queue.first = queue.last = nn;
      }
    else
      {
	//Add to back of queue if not empty.
	nn.text = nextString;
	queue.last.next = nn;
	queue.last = nn;
      }
  }

  //Check to see what is first in the queue
  public static void peek (Queue queue)
  {
    System.out.println(queue.first.text);
  }

  //Look at what is first and the queue and pull it off
  public static void pop (Queue queue)
  {
    System.out.println(queue.first.text);
    queue.first = queue.first.next;
  }

  /*
    Main Definition
  */

  public static void main (String [] args)
  {
    Scanner scan = new Scanner (System.in);
    //Read in first line from the user.
    Queue testQueue = new Queue();
    String line = scan.nextLine();
    //Push the words into the queue.
    String [] individual = line.split(" ");
    for (int i = 0; i < individual.length; i++)
      {
	push(testQueue, individual[i]);
      }
    System.out.print(">");
    //Read input from user.
    String doWhat = new String();
    doWhat = scan.next();
    //Continue until user says to quit
    while (!doWhat.equals("!quit"))
      {
	if (doWhat.equals("!pop"))
	  {
	    pop(testQueue);
	  }
	else if (doWhat.equals("!peek"))
	  {
	    peek(testQueue);
	  }
	else
	  {
	    //Command is assumed to be add
	    line = scan.nextLine();
	    //Words added as string need to be split into array
	    String [] series = line.split(" ");
	    //Treat each word as a separate entry into the queue
	    for (int j = 0; j < series.length; j++)
	      {
		push(testQueue, series[j]);
	      }
	  }
	//Prompt user for next entry
	System.out.print(">");
	doWhat = scan.next();
      }
  }
}