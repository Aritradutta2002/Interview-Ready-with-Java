package BINARY_TREES;
/*
 *   Author  : Aritra Dutta
 *   Created : Monday, 30.12.2024  11:23 pm
 */

class BinaryTreeNode{
    public int data;
    public BinaryTreeNode left;
    public BinaryTreeNode right;

    public BinaryTreeNode(int data) {
        this.data = data;
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
}

public class Structure_BinaryTree {
    public static void main(String[] args) {

        // Initialize and allocate memory for tree nodes

        BinaryTreeNode root = new BinaryTreeNode(1);
        root.left = new BinaryTreeNode(2);
        root.right = new BinaryTreeNode(3);
    }
}
