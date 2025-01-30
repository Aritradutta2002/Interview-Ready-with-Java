package BINARY_TREES.Traversal_Techniques.IterativeApproach;

import java.io.*;
import java.util.*;

/*
 *   Author  : Aritra Dutta
 *   Created : Thursday, 30.01.2025  09:28 am
 */
public class PreOrder_Iterative {

    public static void main(String[] args) {
        StructureBinaryTree root = new StructureBinaryTree(1);
        root.left = new StructureBinaryTree(4);
        root.left.left = new StructureBinaryTree(4);
        root.left.right = new StructureBinaryTree(2);

        System.out.println(preOrder_Loop(root));
    }

    static List<Integer> preOrder_Loop(StructureBinaryTree root) {
        List<Integer> list = new ArrayList<>();

        if (root == null) {
            return list;
        }

        Stack<StructureBinaryTree> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            root = stack.pop();
            list.add(root.data);

            if (root.right != null) {
                stack.push(root.right);
            }

            if (root.left != null) {
                stack.push(root.left);
            }
        }

        return list;
    }
}
