public class AlphaMapLL implements Map<Integer,String> {
  private class Node {
    int key;
    String value;
    Node next = null;
 
    public Node(int key, String value) {
      this.key = key;
      this.value = value;
    }
  }
 
  private Node head = null;
 
  public AlphaMapLL() {
    // nothing to do here; head is default assigned to null.
  }
 
  @Override
  public String get(Integer key) {
    return getFrom(this.head, key);
  }
 
  private String getFrom(Node uu, int needle) {
    if (uu == null) return null;
    else if (needle == uu.key) return uu.value;
    else return getFrom(uu.next, needle);
  }
 
  @Override
  public void set(Integer key, String value) {
    /* Note: There is something subtle going on here.
     * If the key is already in the linked list, you might think
     * that we have to traverse the list, find the node with the key,
     * and change its value. But not so! Since the get method will
     * just return the first value it comes to, we can just store
     * the same key multiple times in the linked list; the most recent
     * storage is what get(key) will return.
     * (Unfortunately, you can't use this trick in a Binary Search Tree!)
     */
    Node newNode = new Node(key, value);
    newNode.next = this.head;
    this.head = newNode;
  }
 
  @Override
  public int size() {
    throw new UnsupportedOperationException("it's hard to avoid over-counting here!");
  }
}