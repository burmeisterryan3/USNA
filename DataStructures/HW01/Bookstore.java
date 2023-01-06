import java.util.Arrays;

 /**
 * This class contains all of the reading material to be sorted and bought by the program user.
 */
public class Bookstore {	
	private ReadingMaterial[] rm;
	
	/**
	 * Bookstore constructor
	 * 
	 * @param rm The array of ReadingMaterial which contains all of the reading material input by the user.
	 */
	public Bookstore (ReadingMaterial[] rm){
		this.rm = rm;
	}
	
	/**
	 * Attempts to "buy" a selection from the reading material array.
	 * 
	 * @param title The name of the book entered by the user to buy.
	 * @throws OutOfStockException Thrown if there is not a book in the selection with that name.
	 */
	public void buy (String title) throws OutOfStockException{
		boolean cont = true;
		for (int i = 0; i < rm.length && cont; i++){
			if (title.equals(rm[i].getTitle())) {
				rm[i].buy();
				cont = false;
			} else if (i == rm.length-1 && !title.equals(rm[i].getTitle())){
				throw new OutOfStockException();
			}
		}
	}
	
	/**
	 * Prints the entirety of the ReadingMaterial array.
	 */
	public void printAll() {
		for (int i = 0; i < rm.length; i++) {
			System.out.println(rm[i]);
		}
	}
	
	/**
	 * Sorts the ReadingMaterial array.
	 */
	public void sort() {
		Arrays.sort(rm);
	}
}
