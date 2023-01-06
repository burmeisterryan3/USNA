import java.util.Queue;         // req for Queue
import java.util.LinkedList;    // req for Queue
import java.util.Stack;         // req for Stack

public class Lab02Demo {

  /**
   * Illustrates a stack in use.
   */
  public static void testStack(){
    Stack stack = new Stack();
    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push('a');

    System.out.println("\nStack demo:\n------------------");
    System.out.println("Stack contents: " + stack);
    System.out.println("Stack size    : " + stack.size());
    System.out.println("Popped value  : " + stack.pop());
    System.out.println("Popped value  : " + stack.pop());
    System.out.println("Stack size    : " + stack.size());
    System.out.println("Stack contents: " + stack);
  }

  /**
   * Illustrates a queue in use.
   */
  public static void testQueue(){
    Queue<String> testQueue = new LinkedList<String>();
    testQueue.add("this");
    testQueue.add("is");
    testQueue.add("a");
    testQueue.add("test");

    System.out.println("\nQueue demo:\n------------------");
    System.out.println("Queue contents : " + testQueue);
    System.out.println("Queue size     : " + testQueue.size());
    System.out.println("peek()         : " + testQueue.peek());
    System.out.println("Queue size     : " + testQueue.size());
    System.out.println("poll()         : " + testQueue.poll());
    System.out.println("Queue size     : " + testQueue.size());
    System.out.println("peek()         : " + testQueue.peek());
    System.out.println("Queue size     : " + testQueue.size());
    System.out.println("poll()         : " + testQueue.poll());
    System.out.println("Queue size     : " + testQueue.size());
    System.out.println("Queue contents : " + testQueue);
  }

  // The main() function that is executed.
  public static void main(String[] args) {
    testQueue();
    testStack();
  }
}

