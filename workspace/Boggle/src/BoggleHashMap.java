import java.util.LinkedList;
import java.util.Queue;

public class BoggleHashMap {
	
	private class mapElement{
		String word;
		private mapElement(String word){ this.word = word; }
	}
	
	int numItems;
	int mapCapacity;
	float loadFactor = (float).5; /* numItems/mapCapacity <= .5 */
	mapElement[] wordMap;
	
	public BoggleHashMap(int mapCapacity) {
		if (mapCapacity <= 1) { this.mapCapacity = 2; } /* load factor must not be 1 with first insertion */
		else { this.mapCapacity = mapCapacity; }
		this.wordMap = new mapElement[mapCapacity];
	}
	
	/** Resize the map if the load factor would be >= .5 on the next insertion */
	private mapElement[] resize(mapElement[] wordMap) {
		mapCapacity *= 2;
		mapElement[] newMap = new mapElement[mapCapacity];
		for (int i = 0; i < wordMap.length; ++i) {
			if (wordMap[i] != null) { /* is there a word at this location? If so, add it to the new resized map */
				mapElement me = new mapElement(wordMap[i].word);
				int key = Math.abs(wordMap[i].word.hashCode()) % mapCapacity;
				for (int j = key; true; ++j){ /* no test condition... load factor = .5, always null indices in map */
					if (j == newMap.length) { j = 0; } /* at end of the array, continue traversing at index 0 */
					if (newMap[j] == null) {
						newMap[j] = me;
						break;
					}
				}
			}
		}
		return newMap;
	}
	
	/** Insert Strings into the hashtable if not already contained within the map */
	public void insert(String word) {
		if (this.find(word)) { return; } /* is word already within hashtable? */
		//if we will exceed the load factor with this insertion resize map
		if ((float)(numItems)/mapCapacity == this.loadFactor) { wordMap = this.resize(wordMap); }
		mapElement me = new mapElement(word);
		int key = Math.abs(word.hashCode()) % mapCapacity;
		for (int i = key; true; ++i) { /* no test condition... load factor = .5, always null indices in map */
			if (i == wordMap.length) { i = 0; } /* at end of the array, continue traversing at index 0 */
			if (wordMap[i] == null) {
				wordMap[i] = me;
				numItems++;
				break;
			}
		}
	}
	
	/** Tells you whether the String 'word' appears within the hashtable */
	public boolean find(String word) {
		int key = Math.abs(word.hashCode()) % mapCapacity;
		for (int i = key; true; ++i) {
			if (i == wordMap.length) { i = 0; } /* at end of the array, continue traversing at index 0 */
			if (wordMap[i] == null) { return false; }
			else if (wordMap[i].word.equals(word)) { return true; }		
		}
	}
	
	/** Returns a Queue filled with all Strings in the hashtable */
	public Queue<String> traverse() {
		Queue<String> wordList = new LinkedList<String>();
		for (int i = 0; i < wordMap.length; ++i) 
			if (wordMap[i] != null) { wordList.add(wordMap[i].word); }
		return wordList;
	}
}