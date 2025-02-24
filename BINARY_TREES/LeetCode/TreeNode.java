package BINARY_TREES.LeetCode;
/*
 *   Author  : Aritra Dutta
 *   Created : Friday, 31.01.2025  06:43 pm
 */

public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {

    }
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}