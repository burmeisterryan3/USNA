import java.util.*;

/** A max heap of times, with associated names for each time value. */
public class TimeHeap {
	/** A single (time,name) entry in the heap.
	 * Note that when you are doing heap operations, you will be comparing
	 * the "time" field in each entry.
	 */
	private class Entry {
		public long time;
		public String name;
		public Entry(long time, String name) {
		this.time = time;
		this.name = name;
		}
	}

	Entry[] entries;
	int size;

	/** Creates a new, empty heap. */
	public TimeHeap() {
		/* want an array with ten times
		 * first index is null, so we need 10 additional indices
		 * need extra index for insertion... removal will occur after insertion
		 */
		entries = new Entry[11];
		this.size = 0;
	}

	/** Returns the number of entries in the heap */
  	public int size() {
  		return this.size;
  	}

  	/** Inserts a new time and corresponding name to the heap. 
  	 * You can assume the name is not null. */
  	public void insert(long time, String name) {
  		++size;
  		if (size == entries.length) {
  			Entry[] temp = new Entry[entries.length*2];
  			for (int i = 0; i < size; i++) {
  				temp[i]	= entries[i];
  			}
  			entries = temp;
  		}
  		entries[size] = new Entry(time, name);
  		for (int i = size; i != 1; i /= 2) {
  			if (entries[i/2].time < entries[i].time) {
  				swap(i, i/2);
  			}
  		}
  	}

  	/** Removes the entry with the largest time, and returns the
  	 * corresponding name.
  	 */
  	public String removeMax() {
  		if (this.size() < 1) 
  			throw new NoSuchElementException("heap is empty");
  		String returnName = entries[1].name;
  		entries[1] = entries[this.size];
  		entries[this.size] = null;
  		--size;
  		int i = 1;
  		while (i < this.size/2 + 1) {
  			boolean smallerThanFirstChild = entries[i].time < entries[2*i].time;
  			boolean smallerThanSecondChild = false;
  			/* is there a second child within the bounds of the array */
  			boolean sizeGreatEnough = size >= 2*i + 1;
  			if (sizeGreatEnough) { 
  				smallerThanSecondChild = entries[i].time < entries[2*i + 1].time;
  			}
  			if (smallerThanFirstChild && smallerThanSecondChild) {
  				int largerTimeIndex;
  				if (entries[2*i].time > entries[2*i + 1].time) { 
  					largerTimeIndex = 2*i;
  					i *= 2; /* need to know what to make i prior to swapping */
  				}else { 
  					largerTimeIndex = 2*i + 1;
  					i = 2*i + 1; /* need to know what to make i prior to swapping */
  				}
  				swap(i/2, largerTimeIndex);
  			} else if (smallerThanFirstChild) {
  				swap(i, 2*i);
  				i *= 2;
  			} else if(smallerThanSecondChild) {
  				swap(i, 2*i + 1);
  				i = i*2 + 1;
  			} else { break; }
  		}
  		return returnName;
  	}
  	
  	private void swap(int firstIndex, int secondIndex) {
  		Entry temp = entries[firstIndex];
  		entries[firstIndex] = entries[secondIndex];
  		entries[secondIndex] = temp;
  	}
}