public interface Map<K,V> {
  /** Retrieves the value associated with this key, or null if not found. */
  public V get(K key);

  /** Assigns a value to be associated with the given key.
   * If the key is not already in the map, it gets added.
   * Otherwise, if it's already in the map, its associated value is modified.
   */
  public void set(K key, V value);

  /** Returns the number of distinct keys in this map. */
  public int size();
}