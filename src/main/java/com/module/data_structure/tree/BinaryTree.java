package com.module.data_structure.tree;

public class BinaryTree {
    class Node{
        public int data;
        public Node left;
        public Node right;
        public Node parent;

        public Node(int value){
            data = value;
            left = null;
            right = null;
            parent = null;
        }

    }

    private Node root;

    public BinaryTree(){
        root = null;
    }

    public Node find(int key){
        Node current = root;
        while(current != null){
            if(key < current.data) {
                if(current.left == null)return current;
                current = current.left;
            }else if(key >current.data) {
                if (current.right == null) return current;
                current = current.right;
            }else{
                return current;
            }
        }
        return null;
    }

    public void put(int value){
        Node newNode = new Node(value);
        if(null == root)
            root = newNode;
        else{
            Node parent = find(value);
            if(value < parent.data){
                parent.left = newNode;
                newNode.parent = parent;
            }else{
                parent.right = newNode;
                newNode.parent = parent;
            }
        }
    }

    public boolean remove(int value){
        Node temp = find(value);
        if(temp == null && temp.data != value)
            return false;

        if(temp.right == null && temp.left == null) {
            if (temp == null)
                root = null;
            else if (temp.parent.data < temp.data)
                temp.parent.right = null;
            else
                temp.parent.left = null;
            return true;
        }

        else if(temp.left != null && temp.right != null){
            Node successor = findSuccessor(temp);
            successor.left = temp.left;
            temp.left.parent = successor;

//            if(successor)
        }
        return false;
    }

    public Node findSuccessor(Node n){
        if(n.right == null)
            return n;
        Node current = n.right;
        Node parent = n.right;
        while(current != null){
            parent = current;
            current = current.left;
        }
        return parent;
    }

    public Node getRoot(){
        return root;
    }

    /**
     * 中序遍历
     * @param localRoot
     */
    public void inOrder(Node localRoot){
        if(localRoot != null) {
            inOrder(localRoot.left);
            System.out.println(localRoot.data + "  ");
            inOrder(localRoot.right);
        }
    }

    public void preOrder(Node localRoot){
        if(localRoot != null){
            System.out.println(localRoot.data + "  ");
            preOrder(localRoot.left);
            preOrder(localRoot.right);
        }
    }

    public void postOrder(Node localRoot){
        if(localRoot != null){
            postOrder(localRoot.left);
            postOrder(localRoot.right);
            System.out.println(localRoot.data + "  ");
        }
    }
}
