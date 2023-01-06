/** The TagStack class will order elements in LIFO manner through the implementation of an array */
public class TagStack implements Stack<Tag>{
	private int size;
	private Tag[] tagStack;

	/** Return size of stack */
	public int size() {
		return this.size;
	}

	/** Add element to stack */
	public void push(Tag element) {
		if (0 == size) {
			tagStack = new Tag[1];
			tagStack[size] = element;
			++size;
		} else if (size == tagStack.length) {
			/** Create a place to store elements as they are transferred to new array */
			Tag[] tempTagStack = new Tag[size*2];
			for (int i = 0; i < size; ++i) {
				tempTagStack[i] = tagStack[i];
			}
			/** The Stack pointer should now point to the new array */
			tagStack = tempTagStack;
			tagStack[size] = element;
			++size;
		} else {
			tagStack[size] = element;
			++size;
		}
	}

	/** Remove an element from the stack and return it to the user */
	public Tag pop() {
		if (size != 0) {
			--size;
			return tagStack[size];
		} else
			return null;
	}

	/** Return but do not remove what is first in the stack */
	public Tag peek() {
		if (size != 0)
			return tagStack[size-1];
		else
			return null;
	}
	
	public Tag getElement(int index) {
		return tagStack[index];
	}
}
