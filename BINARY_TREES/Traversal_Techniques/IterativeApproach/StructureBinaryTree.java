package BINARY_TREES.Traversal_Techniques.IterativeApproach;
import BINARY_TREES.Traversal_Techniques.RecursiveApproach.BinaryTreeNode;

/*
 *   Author  : Aritra Dutta
 *   Created : Wednesday, 29.01.2025  09:21 pm
 */
public class StructureBinaryTree {
    int data;
    StructureBinaryTree left;
    StructureBinaryTree right;

    public StructureBinaryTree(int data) {
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

    public StructureBinaryTree getLeft() {
        return left;
    }

    public void setLeft(StructureBinaryTree left) {
        this.left = left;
    }

    public StructureBinaryTree getRight() {
        return right;
    }

    public void setRight(StructureBinaryTree right) {
        this.right = right;
    }
}
