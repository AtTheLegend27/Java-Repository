public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

    /* Creates an empty BST. Super() calls the constructor for BinaryTree (not in scope). */
    public BinarySearchTree() {
        super();
    }

    /* Creates a BST with root as ROOT. */
    public BinarySearchTree(TreeNode root) {
        super(root);
    }

    /* Returns true if the BST contains the given KEY. */
    public boolean contains(T key) {
        // TODO: YOUR CODE HERE: an extra helper method might be useful
        TreeNode temp = this.root;
        if (this.root == null){
            return false;
        } else {
            while (temp != null) {
                if (key.compareTo(temp.item) == 0) {
                    return true;
                }
                else if (key.compareTo(temp.item) > 0) {
                    temp = temp.right;
                } else if (key.compareTo(temp.item) < 0) {
                    temp = temp.left;
                }
            }
        }
        return false;
    }

    /* Adds a node for KEY iff KEY isn't in the BST already. */
    public void add(T key) {
        // TODO: YOUR CODE HERE: an extra helper method might be useful
        TreeNode temp = this.root;
        if (this.root == null){
            this.root = new TreeNode(key);
            return;
        }
        while(temp != null){
            if (key.compareTo(temp.item) == 0) {
                break;
            } else if (key.compareTo(temp.item) > 0){
                if (temp.right == null) {
                    temp.right = new TreeNode(key);
                    break;
                } else {
                    temp = temp.right;
                }
            } else if (key.compareTo(temp.item) < 0) {
                if (temp.left == null){
                    temp.left = new TreeNode(key);
                    break;
                } else {
                    temp = temp.left;
                }
            }
        }
        return;
    }

    /* Deletes a node from the BST. 
     * Even though you do not have to implement delete, you 
     * should read through and understand the basic steps.
    */
    public T delete(T key) {
        TreeNode parent = null;
        TreeNode curr = root;
        TreeNode delNode = null;
        TreeNode replacement = null;
        boolean rightSide = false;

        while (curr != null && !curr.item.equals(key)) {
            if (curr.item.compareTo(key) > 0) {
                parent = curr;
                curr = curr.left;
                rightSide = false;
            } else {
                parent = curr;
                curr = curr.right;
                rightSide = true;
            }
        }
        delNode = curr;
        if (curr == null) {
            return null;
        }

        if (delNode.right == null) {
            if (root == delNode) {
                root = root.left;
            } else {
                if (rightSide) {
                    parent.right = delNode.left;
                } else {
                    parent.left = delNode.left;
                }
            }
        } else {
            curr = delNode.right;
            replacement = curr.left;
            if (replacement == null) {
                replacement = curr;
            } else {
                while (replacement.left != null) {
                    curr = replacement;
                    replacement = replacement.left;
                }
                curr.left = replacement.right;
                replacement.right = delNode.right;
            }
            replacement.left = delNode.left;
            if (root == delNode) {
                root = replacement;
            } else {
                if (rightSide) {
                    parent.right = replacement;
                } else {
                    parent.left = replacement;
                }
            }
        }
        return delNode.item;
    }
}
