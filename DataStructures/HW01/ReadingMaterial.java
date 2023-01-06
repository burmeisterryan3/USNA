/**
 * An abstract class which performs the basic operations of varying reading materials and include
 * properties such as title, cost, and stock.
 */
public abstract class ReadingMaterial implements Comparable<ReadingMaterial> {
	private String title;
	private double cost;
	private int stock;
	
	/**
	 * ReadingMaterial constructor
	 * 
	 * @param title The name of the reading material
	 * @param cost The cost of the reading material
	 * @param stock The number of total reading material
	 */
	public ReadingMaterial (String title, double cost, int stock) {
		this.title = title;
		this.cost = cost;
		this.stock = stock;
	}
	
	/**
	 * @return Returns the title of the reading material
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return Returns the cost of the reading material
	 */
	public double getCost() {
		return cost;
	}
	
	
	/**
	 * @return Returns the stock of the reading material
	 */
	public int getStock() {
		return stock;
	}
	
	public abstract String toString();
	
	/**
	 * Attempts to decrement the stock of a particular reading material by one.  If the stock of this book
	 * results in a negative number as a result of the purchase, an OutOfStockException is thrown and the
	 * number is incremented back to zero.
	 * 
	 * @throws OutOfStockException Thrown if the stock of the reading material is less than zero
	 */
	public void buy() throws OutOfStockException {
		this.stock--;
		if (stock < 0){
			stock++;
			throw new OutOfStockException();
		}
		
	}
	
	public int compareTo (ReadingMaterial rm) {
		int returnVal = 1;
		if (this.cost < rm.cost) {
			returnVal = -1;
		} else if (this.cost == rm.cost) {
			returnVal = 0;
		}
		return returnVal;
	}
}
