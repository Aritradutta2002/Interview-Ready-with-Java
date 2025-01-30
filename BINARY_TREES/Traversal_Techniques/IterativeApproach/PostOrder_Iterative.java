package BINARY_TREES.Traversal_Techniques.IterativeApproach;
import java.util.*;
/*
 *   Author  : Aritra Dutta
 *   Created : Thursday, 30.01.2025  09:28 am
 */

public class PostOrder_Iterative {
    public static void main(String[] args) {
        StructureBinaryTree root = new StructureBinaryTree(1);
        root.left = new StructureBinaryTree(4);
        root.left.left = new StructureBinaryTree(4);
        root.left.right = new StructureBinaryTree(2);

        System.out.println(postorder(root));
    }

    static List < Integer > postorder(StructureBinaryTree root) {

        List < Integer > result = new ArrayList < > ();
        if (root == null) {
            return result;
        }
        Stack < StructureBinaryTree > stack = new Stack < > ();
        stack.push(root);
        StructureBinaryTree prev = null;

        while (!stack.isEmpty()) {
            StructureBinaryTree current = stack.peek();
            if (prev == null || prev.left == current || prev.right == current) {
                if (current.left != null)
                    stack.push(current.left);
                else if (current.right != null)
                    stack.push(current.right);
                else {
                    stack.pop();
                    result.add(current.data);
                }

        /* go up the tree from left node, if the
        child is right push it onto stack otherwise
        process parent and pop stack */

            } else if (current.left == prev) {
                if (current.right != null)
                    stack.push(current.right);
                else {
                    stack.pop();
                    result.add(current.data);
                }

        /* go up the tree from right node and after
        coming back from right node process parent
        and pop stack */

            } else if (current.right == prev) {
                stack.pop();
                result.add(current.data);
            }

            prev = current;
        }
        return result;
    }
}