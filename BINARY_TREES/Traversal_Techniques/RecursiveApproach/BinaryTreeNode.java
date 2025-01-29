package BINARY_TREES.Traversal_Techniques.RecursiveApproach;
/*
 *   Author  : Aritra Dutta
 *   Created : Wednesday, 29.01.2025  08:50 pm
 */
public class BinaryTreeNode {
    int data;
    BinaryTreeNode left;
    BinaryTreeNode right;

    public BinaryTreeNode(int data) {
        this.data = data;
        left = null;
        right = null;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public BinaryTreeNode getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeNode left) {
        this.left = left;
    }

    public BinaryTreeNode getRight() {
        return right;
    }

    public void setRight(BinaryTreeNode right) {
        this.right = right;
    }

    // Corrected Preorder Traversal method
    public void PreorderTraversal(BinaryTreeNode root) {
        if (root != null) {
            System.out.print(root.data + " ");
            PreorderTraversal(root.left);
            PreorderTraversal(root.right);
        }
    }

    public void InorderTraversal(BinaryTreeNode root) {
        if(root != null) {
            InorderTraversal(root.left);
            System.out.print(root.data + " ");
            InorderTraversal(root.right);
        }
    }


    public void PostorderTraversal(BinaryTreeNode root) {
        if(root != null) {
            PostorderTraversal(root.left);
            PostorderTraversal(root.right);
            System.out.print(root.data + " ");
        }
    }

    public void LevelOrderTraversal(BinaryTreeNode root) {

    }
}
