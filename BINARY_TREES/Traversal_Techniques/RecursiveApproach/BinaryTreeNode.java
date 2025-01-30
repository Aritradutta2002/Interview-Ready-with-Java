package BINARY_TREES.Traversal_Techniques.RecursiveApproach;

import java.util.*;

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

    /*      PRE ORDER TRAVERSAL STARTS     */
    public void PreorderTraversal(BinaryTreeNode root) {
        if (root != null) {
            System.out.print(root.data + " ");
            PreorderTraversal(root.left);
            PreorderTraversal(root.right);
        }
    }
    /*      IN ORDER TRAVERSAL STARTS     */
    public void InorderTraversal(BinaryTreeNode root) {
        if(root != null) {
            InorderTraversal(root.left);
            System.out.print(root.data + " ");
            InorderTraversal(root.right);
        }
    }

    /*      POST ORDER TRAVERSAL STARTS     */
    public void PostorderTraversal(BinaryTreeNode root) {
        if(root != null) {
            PostorderTraversal(root.left);
            PostorderTraversal(root.right);
            System.out.print(root.data + " ");
        }
    }

    /*      LEVEL ORDER TRAVERSAL STARTS     */
    public void LevelOrderTraversal(BinaryTreeNode root) {
        Queue <BinaryTreeNode> mq = new ArrayDeque<>();
        mq.add(root);
        while(mq.size() > 0) {
            int size = mq.size();
            for(int i = 0; i < size; i++) {
                root = mq.remove();
                System.out.print(root.data + " ");

                if(root.left != null) {
                    mq.add(root.left);
                }

                if(root.right != null) {
                    mq.add(root.right);
                }
            }
            System.out.println();
        }
    }
}
