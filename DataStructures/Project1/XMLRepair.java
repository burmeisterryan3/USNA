/** This class will repair a given input of XML tags by determining the smallest
 * amount of tags possible given an input with an error in the tag scheme 
 */
public class XMLRepair{
	/** Read from the input stream and repair the tag scheme as necessary
	 * This method will create a queue of XMLPossiblit"ies" which will
	 * contain the different combinations of correct XML tags schemes.  The
	 * method will return the shortest of the different schemes.
	 */
	public MyQueue<Tag> tagRepair(TagScanner ts) {
		MyQueue<XMLPossibility> xmlpQueue = new MyQueue<XMLPossibility>();
		xmlpQueue.enqueue(new XMLPossibility());
		while (ts.hasNext()) {
			Tag newTag = ts.next();
			Node<XMLPossibility> iterator = xmlpQueue.head;
			if (newTag.isOpen()) {
				for (int size = xmlpQueue.size(); size > 0; --size) {
					iterator.data.open(newTag);
					XMLPossibility newXmlp = iterator.data.copy();
					newXmlp.close(new Tag(newTag.getName(), false, true));
					xmlpQueue.enqueue(newXmlp);
					iterator = iterator.next;
				}
			} else if (newTag.isClose()) {
				for (int size = xmlpQueue.size(); size > 0; --size) {
					iterator.data.close(newTag);
					iterator = iterator.next;
				}
			}
		}
		/**Were there any tags left?  Someone forgot to include all of their closed tags */
		XMLPossibility shortestXmlp = xmlpQueue.head.data;
		for (Node<XMLPossibility> iter = xmlpQueue.head; iter != null; iter = iter.next) {
			iter.data.addClosingTags();
			if (iter.data.queueSize() < shortestXmlp.queueSize()) { shortestXmlp = iter.data; } 
		}
		return shortestXmlp.getRunningQueue();
	}

	/** Print the queue with appropriate indentations based on the tag scheme */
	public void printQueue(MyQueue<Tag> printThisQueue) {
		/** Check to see if there is anything to print */
		if (printThisQueue.head == null) return;
		Node<Tag> current = printThisQueue.head;
		String output = printThisQueue.size() + "\n";
		int indentation = 0;
		while (current != null) {
			for (int i = 0; i < indentation; ++i) {
				output += "  ";
			}
			if (current.data.isOpen() && current.next.data.isOpen()) {
				output += current.data + "\n";
				++indentation;
			} else if (current.data.isOpen() && current.next.data.isClose() &&
					   current.next.next != null && current.next.next.data.isOpen()) {
				/** Because current.next.next isOpen then we do not need to shift the indentation back */
				output += current.data + "" + current.next.data + "\n";
				current = current.next;
			} else if (current.data.isOpen() && current.next.data.isClose() && 
					   current.next.next != null && current.next.next.data.isClose()) {
				/**Because current.next.next isClose then we have to shift the indentation back */
				output += current.data + "" + current.next.data + "\n";
				/** Must account for the extra element added to the output string */
				current = current.next;
				--indentation;
			} else if (current.data.isClose() && current.next != null && current.next.data.isOpen()) {
				/** Because current.next isOpen we do not shift the indentation back */
				output += current.data + "\n";
			} else if (current.data.isClose() && current.next != null && current.next.data.isClose()) {
				/** Because current.next isClose we do need to shift the indentation back */
				output += current.data + "\n";
				--indentation;
			} else {
				/** Last element in the queue */
				output += current.data;
			}
			current = current.next;
		}
		System.out.println(output);
	}
	
	public static void main(String args[]) {
		XMLRepair repair = new XMLRepair();
		TagScanner ts = new TagScanner(System.in);
		MyQueue<Tag> xml = repair.tagRepair(ts);
		repair.printQueue(xml);
	}
}