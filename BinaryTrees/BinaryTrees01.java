package BinaryTrees;

import java.util.Scanner;

public class BinaryTrees01 {

    public static class Node {
        int data;
        Node left;
        Node right;

        public Node(int data) {
            this.data = data;
        }
    }

    private Node root;

    public void populate(Scanner scn) {
        System.out.println("Enter the Root Node Value: ");
        int value = scn.nextInt();
        root = new Node(value);
        populate(scn, root);
    }

    private void populate(Scanner scn, Node node) {
        System.out.println("Do you want to add left node to " + node.data + "? (true/false)");
        boolean left = scn.nextBoolean();
        if (left) {
            System.out.println("Enter the left node value for " + node.data + ": ");
            int value = scn.nextInt();
            node.left = new Node(value);
            populate(scn, node.left);
        }

        System.out.println("Do you want to add right node to " + node.data + "? (true/false)");
        boolean right = scn.nextBoolean();
        if (right) {
            System.out.println("Enter the right node value for " + node.data + ": ");
            int value = scn.nextInt();
            node.right = new Node(value);
            populate(scn, node.right);
        }
    }

    public void display() {
        display(this.root, "");
    }

    private void display(Node node, String indent) {
        if (node == null) {
            return;
        }
        System.out.println(indent + node.data);
        display(node.left, indent + "\t");
        display(node.right, indent + "\t");
    }


    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        BinaryTrees01 tree = new BinaryTrees01();
        tree.populate(scn);
        tree.display();
    }
}
