package BINARY_TREES.Traversal_Techniques.RecursiveApproach;
import com.sun.source.tree.BinaryTree;

public class LevelOrder_Recursive {
    public static void main(String[] args) {
        BinaryTreeNode root = new BinaryTreeNode(3);

        root.left = new BinaryTreeNode(9);
        root.right = new BinaryTreeNode(20);

        root.right.left = new BinaryTreeNode(15);
        root.right.right = new BinaryTreeNode(7);

        root.LevelOrderTraversal(root);
    }
}