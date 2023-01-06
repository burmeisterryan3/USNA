public class AlphaMap implements Map<Integer, String> {
 
  private class Node {
    Integer key;
    String value;
    int balance;
    Node left = null;
    Node right = null;
    
    private Node(Integer key, String value) {
    	this.key = new Integer(key);
    	this.value = new String(value);
    }
    
    private void balance() {
    	if (this.left == null && this.right == null) this.balance = 0;
    	else if (this.left == null) this.balance = Math.abs(this.right.balance) + 1;
    	else if (this.right == null) this.balance = -1*Math.abs(this.left.balance) - 1;
    	else this.balance = this.right.balance - this.left.balance;
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
			  uu.balance();
			  if (uu.balance == -2) {
				  if (uu.left.balance == -1) uu = rotateRightAt(uu);
				  else if (uu.left.balance == 1) uu = doubleRotateWithLeftTree(uu);
			  }
		  } else if (key > uu.key) {
			  uu.right = setHelper(key, value, uu.right);
			  uu.balance();
			  if (uu.balance == 2) {
				  if (uu.right.balance == 1) uu = rotateLeftAt(uu);
				  else if (uu.right.balance == -1) uu = doubleRotateWithRightTree(uu);
			  }
		  } else { /* key == uu.key */
			  if (uu.value == null && value != null) size++;
			  else if (uu.value != null && value == null) size--;
			  uu.value = value;
		  }
		  uu.balance();
		  return uu;
	  }
  }
  
  private Node rotateRightAt(Node uu) {
	  Node temp = uu.left;
	  uu.left = temp.right;
	  temp.right = uu;
	  uu.balance();
	  return temp;
  }
  
  private Node rotateLeftAt(Node uu) {
	  Node temp = uu.right;
	  uu.right = temp.left;
	  temp.left = uu;
	  uu.balance();
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