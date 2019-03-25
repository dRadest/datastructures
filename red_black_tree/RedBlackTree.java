public class RedBlackTree {
	/**
	 * Values for node color
	 */
	public enum Color {RED, BLACK}
	
	// private variable for null node
	private final Node nil = new Node(-1);
	
	// class to represent tree node
	private class Node{
		int value = -1;
		Color color = Color.BLACK;
		Node left = nil, right = nil, parent = nil;
		// parameterized constructor
		Node(int val){
			this.value = val;
		}
	}
	
	// root node initialized to nil
	private Node root = nil;
	
	/**
	 * Empty constructor initializes root to nil
	 */
	public RedBlackTree() {
		root = nil;
	}
	
	/**
	 * Prints tree in in-order fashion from root
	 */
	public void printInorder() {
		printTree(root);
		System.out.println();
	}
	
	/*
	 * Prints nodes in in-order fashion
	 * @param z starting node
	 */
    private void printTree(Node z) {
        if (z == nil) {
            return;
        }
        printTree(z.left);
        System.out.print(z.value + " " + z.color + " ");
        printTree(z.right);
    }
    
    /**
     * Inserts node with value v into the tree
     * @param v value to insert
     */
    public void insertByValue(int v) {
    	Node inNode = new Node(v);
    	insert(inNode);
    }
    
    /*
     * Inserts node in the tree
     * @param node node to insert
     */
    private void insert(Node z) {
        Node temp = root;
        if (root == nil) { // empty tree
            root = z;
            z.color = Color.BLACK;
            z.parent = nil;
        } else {
            z.color = Color.RED;
            while (true) {
                if (z.value < temp.value) { // node z belongs in left subtree
                    if (temp.left == nil) { // insert node z
                        temp.left = z;
                        z.parent = temp;
                        break;
                    } else { // move left
                        temp = temp.left;
                    }
                }else if (z.value == temp.value) { // node z already in the tree
                	return; 
                } else { // (node.value > temp.value) node z belongs in right subtree
                    if (temp.right == nil) { // insert node z
                        temp.right = z;
                        z.parent = temp;
                        break;
                    } else { // move right
                        temp = temp.right;
                    }
                }
            }
            // fix any violations
            fixup(z);
        }
    }
    
    // takes as argument the newly inserted node and fixes any violations
    private void fixup(Node z) {
        while (z.parent.color == Color.RED) { // while there is a red-red violation
            Node uncle = nil;
            if (z.parent == z.parent.parent.left) { // parent is left child
                uncle = z.parent.parent.right;

                if (uncle != nil && uncle.color == Color.RED) { // case 1: uncle is RED
                	// re-color parent, grandparent and uncle
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    uncle.color = Color.BLACK;
                    // move up the tree
                    z = z.parent.parent;
                    continue;
                } 
                // else, uncle is BLACK
                if (z == z.parent.right) { // case 2: z, p, g form a triangle
                	// rotate z's parent in opposite direction of z
                    z = z.parent;
                    // double rotation needed
                    leftRotate(z);
                } 
                // if above code hasn't executed,
                // case 3: z, p, g form a line
                z.parent.color = Color.BLACK;
                z.parent.parent.color = Color.RED;
                // single rotation needed
                rightRotate(z.parent.parent);
            } else { // parent is right child
                uncle = z.parent.parent.left;
                 if (uncle != nil && uncle.color == Color.RED) { // case 1: uncle is RED
                	// re-color parent, grandparent and uncl
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    uncle.color = Color.BLACK;
                    // move up the tree
                    z = z.parent.parent;
                    continue;
                }
                // else, uncle is BLACK
                if (z == z.parent.left) { // case 2: z, p, g form a triangle
                	// rotate z's parent in opposite direction of z
                    z = z.parent;
                    // double rotation needed
                    rightRotate(z);
                }
                // if above code hasn't executed,
                // case 3: z, p, g form a line
                z.parent.color = Color.BLACK;
                z.parent.parent.color = Color.RED;
                // single rotation needed
                leftRotate(z.parent.parent);
            }
        }
        // case 0: z is root, color it BLACK
        root.color = Color.BLACK;
    }
	// left rotate the given node z
	private void leftRotate(Node z) {
        if (z.parent != nil) { // somewhere in the tree
            if (z == z.parent.left) { // node is left child
                z.parent.left = z.right;
            } else { // node is right child
                z.parent.right = z.right;
            }
            z.right.parent = z.parent;
            z.parent = z.right;
            if (z.right.left != nil) {
                z.right.left.parent = z;
            }
            z.right = z.right.left;
            z.parent.left = z;
        } else { // rotating root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
	}
	// right rotate the given node
	private void rightRotate(Node z) {
        if (z.parent != nil) { // somewhere in the tree
            if (z == z.parent.left) { // node is left child
                z.parent.left = z.left; 
            } else { // node is right child
                z.parent.right = z.left;
            }

            z.left.parent = z.parent;
            z.parent = z.left;
            if (z.left.right != nil) {
                z.left.right.parent = z;
            }
            z.left = z.left.right;
            z.parent.right = z;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
	}
	
	/**
	 * Search for a node with given value
	 * @param key value to look for
	 * @return Node with a given value, null if not found
	 */
	public Node findByValue(int key) {
		Node temp = root;
        while (true) {
            if (key < temp.value) { // node in left subtree
                if (temp.left == nil) { // not found
                    return null;
                } else { // move left
                    temp = temp.left;
                }
            }else if (key == temp.value) { // bingo!
            	return temp; 
            } else { // (node.value > temp.value) node in right subtree
                if (temp.right == nil) { // not found
                    return null;
                } else { // move right
                    temp = temp.right;
                }
            }
        }
	}

	public static void main(String[] args) {
		System.out.println("This implementation of red black tree is based on gang of four");
		RedBlackTree rbt = new RedBlackTree();
		/* example #1 
		 * 				
		 * 				root
		 * 				(11)
		 *             /    \
		 *            /      \
		 *           /        \
		 * 		  (3)          (22)
		 *        / \         /    \
		 *     (2)  (8)red  (18)   (26)
		 *         /  \    /
		 *      (6)  (10) (13)red
		 *        \
		 *        (7)red
		 *        
		 * */
		rbt.insertByValue(11);
		rbt.insertByValue(22);
		rbt.insertByValue(26);
		rbt.insertByValue(2);
		rbt.insertByValue(3);
		rbt.insertByValue(6);
		rbt.insertByValue(18);
		rbt.insertByValue(8);
		rbt.insertByValue(10);
		rbt.insertByValue(3); // duplicate
		rbt.insertByValue(7);
		rbt.insertByValue(13);
		rbt.printInorder();
				
		/* example #2 
		 * 
		 * 			root
		 * 			(13)
		 *         /    \
		 * 	   (8)red   (19)
		 *     /    \      \
		 *  (5)   (10)     (23)red
		 *       /   \
		 *  (9)red   (12)red
		 *  
		 * */
		rbt = new RedBlackTree();
		rbt.insertByValue(13); 
		rbt.insertByValue(19); 
		rbt.insertByValue(8); 
		rbt.insertByValue(12); 
		rbt.insertByValue(5); 
		rbt.insertByValue(10); 
		rbt.insertByValue(9); 
		rbt.insertByValue(23); 
	  
		rbt.printInorder(); 
		
	}
}
