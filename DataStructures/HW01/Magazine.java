/**
 * A class which extends ReadingMaterial and allows user input for a type "Magazine" which
 * does not contain an author.
 */
public class Magazine extends ReadingMaterial{
	/**
	 * Magazine Constructor
	 * 
	 * @param title The name of the magazine.
	 * @param cost The cost of the magazine.
	 * @param quantity The number of magazines.
	 */
	public Magazine (String title, double cost, int quantity) {
		super (title, cost, quantity);
	}
	
	public String toString(){
		return super.getTitle() + ": $" + super.getCost();
	}
}
