/**
 * A class which extends ReadingMaterial and allows user input for a type "Book".
 */
public class Book extends ReadingMaterial{
	private String author;
	
	/**
	 * Book constructor
	 * 
	 * @param title The name of the book
	 * @param author The name of the author of the book
	 * @param cost The cost of the book
	 * @param quantity The number of books
	 */
	public Book (String title, String author, double cost, int quantity) {
		super(title, cost, quantity);
		this.author = author;
	}
	
	public String toString() {
		return author +  ", " + super.getTitle() + ": $" + super.getCost();
	}
}
