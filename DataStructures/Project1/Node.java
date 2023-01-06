/** Class which allows for creation of a linked list */
public class Node<T> {
	public T data;
	public Node<T> next;

	public Node() {
		this.next = null;
	}
	
	public Node(T data) {
		this.data = data;
		this.next = null;
	}
}