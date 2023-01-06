/** Class which represents a possible combination of tags based on a given input */
public class XMLPossibility {
	/** Will include self-closing tags, i.e. queue we will return */
	public MyQueue<Tag> runningQueue = new MyQueue<Tag>();
	/** Will not include self-closing tags; Will be used to peek and check if open tag matches closed tag */
	public TagStack referenceStack = new TagStack();

	/** Wrapper which allows the caller to determine how many elements
	 * still need to be added before the repair is finished
	 */
	public int stackSize() {
		return referenceStack.size();
	}
	
	/** Wrapper which allows the caller to determine how many elements
	 * are within the queue of the XMLPossibility
	 */
	public int queueSize() {
		return runningQueue.size();
	}
	
	/** Wrapper which allows the caller to determine what would be popped
	 * off the stack if the user were going to add a closed tag
	 */
	public Tag peek() {
		return referenceStack.peek();
	}
	
	/** Getter for the private runningQueue */
	public MyQueue<Tag> getRunningQueue(){
		return runningQueue;
	}
	
	/** Add an open tag to the queue */
	public void open(Tag newTag) {
		runningQueue.enqueue(newTag);
		referenceStack.push(newTag);
	}
	
	/** Add a closed tag to the queue */
	public void close(Tag newTag) {
		if (referenceStack.size() != 0 && referenceStack.peek().getName().equals(newTag.getName())) {
			runningQueue.enqueue(newTag);
			referenceStack.pop();
		} else {
			this.open(new Tag(newTag.getName(), true, false));
			this.close(newTag);
		}
	}
	
	/** Add any closed tags which may have been missed */
	public void addClosingTags(){
		while (referenceStack.size() != 0) {
			runningQueue.enqueue(new Tag(referenceStack.pop().getName(), false, true));
		}
	}
	
	/** Copy the contents of the calling XMLPossibility into a new XMLPossibility
	 * and return this new instance
	 */
	public XMLPossibility copy() {
		XMLPossibility newXmlp = new XMLPossibility();
		for (Node<Tag> iter = runningQueue.head; iter != null; iter = iter.next) {
			if (iter.data.isOpen()) { newXmlp.open(iter.data); }
			else { newXmlp.close(iter.data); }
		}
		return newXmlp;
	}
	
	/** Wrapper for the queue print
	 * Allows user to print the possible combination of tags represented by this object
	 */
	public String toString() {
		return runningQueue.toString();
	}
}
