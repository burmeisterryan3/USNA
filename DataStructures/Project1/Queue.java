/** A queue of objects of type T. */
public interface Queue<T> {
  /** Returns the number of items in the queue. */
  public int size();
 
  /** Adds a new item to the end of the queue. */
  public void enqueue(T element);
 
  /** Returns and removes the item at the front of the queue. */
  public T dequeue();
 
  /** Returns but does NOT remove the item at the front of the queue. */
  public T peek();
}