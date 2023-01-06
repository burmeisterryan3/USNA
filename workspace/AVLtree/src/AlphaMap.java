public class AlphaMap implements Map<Integer, String> {
 
  private class Node {
    Integer key;
    String value;
    int height;
    Node left = null;
    Node right = null;
    
    private Node(Integer key, String value) {
    	this.key = new Integer(key);
    	this.value = new String(value);
    	this.height = 0;
    }
    
    private int balance() {
    	if (this.right == null && this.left == null) { return 0; }
    	else if (this.right == null) { return -1*this.left.height - 1; }
    	else if (this.left  == null) { return this.right.height + 1; }
    	else return this.right.height - this.left.height;
    }
    
    private void updateHeight() {
    	if (this.right == null && this.left == null) { this.height = 0; }
    	else if (this.right == null) { this.height = this.left.height  + 1; }
    	else if (this.left  == null) { this.height = this.right.height + 1; }
    	else this.height = Math.max(this.right.height, this.left.height) + 1;
    }
  }
 
  Node root;
  int size = 0;
 
  public AlphaMap() {
	  this.root = null;
  }
  
  @Override
  public int size() {
	  return this.size;
  }
 
  @Override
  public String get(Integer key) {
	  return getHelper(key, this.root);
  }
 
  private String getHelper(Integer key, Node uu){
	  if (uu == null) return null;
	  else if (key.compareTo(uu.key) < 0) return getHelper(key, uu.left);
	  else if (key.compareTo(uu.key) > 0) return getHelper(key, uu.right);
	  else return uu.value;
  }
  
  @Override
  public void set(Integer key, String value) {
	  this.root = setHelper(key, value, this.root);
  }
  
  private Node setHelper(Integer key, String value, Node uu) {
	  if (uu == null) {
		  if (value == null) return null;
		  this.size++;
		  return new Node(key, value);
	  } else {
		  if (key < uu.key) {
			  uu.left = setHelper(key, value, uu.left);
			  int balance = uu.balance();
			  if (balance == -2) {
				  int leftBalance = uu.left.balance();
				  if (leftBalance == -1) uu = rotateRightAt(uu);
				  else if (leftBalance == 1) uu = doubleRotateWithLeftTree(uu);
			  }
		  } else if (key > uu.key) {
			  uu.right = setHelper(key, value, uu.right);
			  int balance = uu.balance();
			  if (balance == 2) {
				  int rightBalance = uu.right.balance();
				  if (rightBalance == 1) uu = rotateLeftAt(uu);
				  else if (rightBalance == -1) uu = doubleRotateWithRightTree(uu);
			  }
		  } else { /* key == uu.key */
			  if (uu.value == null && value != null) size++;
			  else if (uu.value != null && value == null) size--;
			  uu.value = value;
		  }
		  uu.updateHeight();
		  return uu;
	  }
  }
  
  private Node rotateRightAt(Node uu) {
	  Node temp = uu.left;
	  uu.left = temp.right;
	  temp.right = uu;
	  uu.updateHeight();
	  temp.updateHeight();
	  return temp;
  }
  
  private Node rotateLeftAt(Node uu) {
	  Node temp = uu.right;
	  uu.right = temp.left;
	  temp.left = uu;
	  uu.updateHeight();
	  temp.updateHeight();
	  return temp;
  }
  
  private Node doubleRotateWithLeftTree(Node uu) {
	  uu.left = rotateLeftAt(uu.left);
	  return rotateRightAt(uu);
  }
  
  private Node doubleRotateWithRightTree(Node uu) {
	  uu.right = rotateRightAt(uu.right);
	  return rotateLeftAt(uu);
  }
}