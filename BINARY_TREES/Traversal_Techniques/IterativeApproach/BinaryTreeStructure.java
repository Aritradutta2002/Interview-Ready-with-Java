package BINARY_TREES.Traversal_Techniques.IterativeApproach;

/*
 *   Author  : Aritra Dutta
 *   Created : Wednesday, 29.01.2025  09:21 pm
 */
public class BinaryTreeStructure {
    int data;
    BinaryTreeStructure left;
    BinaryTreeStructure right;

    public BinaryTreeStructure(int data) {
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

    public BinaryTreeStructure getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeStructure left) {
        this.left = left;
    }

    public BinaryTreeStructure getRight() {
        return right;
    }

    public void setRight(BinaryTreeStructure right) {
        this.right = right;
    }
}
