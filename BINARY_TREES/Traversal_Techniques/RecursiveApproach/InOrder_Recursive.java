package BINARY_TREES.Traversal_Techniques.RecursiveApproach;
/*
 *   Author  : Aritra Dutta
 *   Created : Wednesday, 29.01.2025  08:56 pm
 */
public class InOrder_Recursive {

    public static void main(String[] args) {
        BinaryTreeNode root = new BinaryTreeNode(1);
        root.left = new BinaryTreeNode(4);
        root.left.left = new BinaryTreeNode(4);
        root.left.right = new BinaryTreeNode(2);

        root.InorderTraversal(root);
    }
}
