public class AlphaMap implements Map<Integer, String>{
	private class Node {
		int key;
		String value;
		Node left = null;
		Node right = null;
 
		public Node(int key, String value) {
			this.key = key;
			this.value = value;
		}
	}
 
	private Node root = null;
	private int size = 0;
	
	@Override
	public String get(Integer key) {
		return getHelper(key, this.root);
	}
	
	private String getHelper(Integer key, Node uu) {
		if (uu == null) return null;
		else if (uu.key == key) return uu.value;
		else if (key < uu.key) return getHelper(key, uu.left);
		else return getHelper(key, uu.right);
	}

	@Override
	public void set(Integer key, String value) {
		root = setHelper(key, value, this.root);
	}
	
	private Node setHelper(Integer key, String value, Node uu) {
            if (uu == null) {
                if (value == null) return null;
                this.nn += 1;
                return new Node(key, value);
            } else {
                if (key < uu.key) uu.left = setHelper(key, value, uu.left);
                else if (key > uu.key) uu.right = setHelper(key, value, uu.right);
                else { /* key == uu.key */
                    if (uu.value == null && value != null) ++this.size;
                    else if (uu.value != null && value== null) --this.size;
                    uu.value = value;
                }
            }
            return uu;
	}

	@Override
	public int size() {
		return size;
	}
}
