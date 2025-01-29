package BINARY_TREES.Traversal_Techniques.RecursiveApproach;
/*
 *   Author  : Aritra Dutta
 *   Created : Tuesday, 31.12.2024  12:17 pm
 */

public class Preorder_Recursive {
    public static void main(String[] args) {
        // Create the binary tree
        BinaryTreeNode root = new BinaryTreeNode(1);
        root.left = new BinaryTreeNode(4);
        root.left.left = new BinaryTreeNode(4);
        root.left.right = new BinaryTreeNode(2);

        // Call Preorder Traversal
        root.PreorderTraversal(root);  // Output should be: 1 2 4 5 3
    }
}
