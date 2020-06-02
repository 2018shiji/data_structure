package com.module.data_structure.tree;


public class AVLTree {
    private Node root;

    private class Node{
        private int key;
        private int balance;
        private int height;
        private Node left, right, parent;

        Node(int k,Node p){
            key = k;
            parent = p;
        }
    }

    public boolean insert(int key){
        if(root == null)
            root = new Node(key, null);
        else{
            Node n = root;
            Node parent;
            while(true){
                if(n.key == key)return false;
                parent = n;
                boolean goLeft = n.key > key;
                n = goLeft ? n.left : n.right;

            }
        }
        return false;
    }

    private void rebalance(Node n){

    }

    private void setBalance(Node... nodes){

    }

    private void reheight(Node node){
        if(node != null){
//            node.height =
        }
    }
}
