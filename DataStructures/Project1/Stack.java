/** A stack of objects of type T. */
public interface Stack<T> {
  /** Returns the number of items in the stack. */
  public int size();
 
  /** Adds a new item to the top of the stack. */
  public void push(T element);
 
  /** Returns and removes the item at the top of the stack. */
  public T pop();
 
  /** Returns but does NOT remove the item at the top of the stack. */
  public T peek();
}