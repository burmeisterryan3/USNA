//import java.util.Iterator;
import java.util.NoSuchElementException;

/** The MyQueue class will order elements in FIFO manner through the implementation of a linked list
 * The linked list is uses the Node class to connect the data elements
 * For help making this class iterable I referenced:
 * http://cs-fundamentals.com/data-structures/implement-queue-using-linked-list-in-java.php
 */
public class MyQueue<T> implements Queue<T> /*Iterable<T>*/{
	private int size;
	public Node<T> head;
	public Node<T> tail;
	
	/** Return the size of the queue */
	public int size() {
		return this.size;
	}

	/** Add an element */
	public void enqueue(T element) {
		Node<T> newNode = new Node<T>(element);
		if (0 == size){
			head = newNode;
			tail = newNode;
			++size;
		} else {
			tail.next = newNode;
			tail = newNode;
			++size;
		}
	}

	/** Remove an element and return the data of the Node to the caller */
	public T dequeue() {
		if (0 == size) throw new NoSuchElementException("The queue is already empty!");
		else {
			T data = head.data;
			head = head.next;
			--size;
			if (size == 0) tail = null;
			return data;
		}
	}

	/** Return but do not remove what is first in the list */
	public T peek() {
		if (size != 0) return head.data;
		else return null;
	}
	
	/** Allow for the queue to be iterated over each of the elements in the queue... FAILED EPICALLY
	public Iterator<T> iterator() {
		return new MyQueueIterator();
	}
	
	private class MyQueueIterator implements Iterator<T> {
		private Node<T> iter = head;
		private int size = this.size;
		
		public boolean hasNext() {
			return (iter == head || size > 0);
		}

		public T next() {
			T data = iter.data;
			iter = iter.next;
			--size;
			return data;
		}
		
		public void remove() { /** Don't need *//*}
	}
	*/
}
