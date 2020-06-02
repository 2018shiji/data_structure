package com.module.data_structure.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenericTree {
    private class Node{
        int data;
        List<Node> child = new ArrayList<>();
    }

    private Node root;
    private int size;

    public GenericTree(){
        Scanner scn = new Scanner(System.in);
        root = create_treeG(null, 0, scn);
    }

    private Node create_treeG(Node node, int childIndex, Scanner scn){
        if(node == null)
            System.out.println("Enter root's data");
        else
            System.out.println("Enter data of parent of index " + node.data + "  " + childIndex);
        node = new Node();
        node.data = scn.nextInt();
        System.out.println("number of children");
        int number = scn.nextInt();
        for(int i = 0; i < number; i++){
            Node child = create_treeG(node, i, scn);
            size++;
            node.child.add(child);
        }
        return node;
    }

    public void display(){
        display_1(root);
    }

    private void display_1(Node parent){
        System.out.println(parent.data + "=>");
        for(int i = 0; i < parent.child.size(); i++)
            System.out.println(parent.child.get(i).data + "  ");
        System.out.println(".");
        for(int i = 0; i < parent.child.size(); i++)
            display_1(parent.child.get(i));
    }

    public int size2call(){
        return size2(root);
    }
    private int size2(Node root){
        int sz = 0;
        for(int i = 0; i < root.child.size(); i++)
            sz += size2(root.child.get(i));
        return sz + 1;
    }

    public int maxcall(){
        int maxi = root.data;
        return max(root, maxi);
    }
    private int max(Node root, int maxi){
        if(maxi < root.data)
            maxi = root.data;
        for(int i = 0; i < root.child.size(); i++)
            maxi = max(root.child.get(i), maxi);
        return maxi;
    }

    public boolean findcall(int info){
        return find(root, info);
    }
    private boolean find(Node node, int info){
        if(node.data == info)
            return true;
        for(int i = 0; i < node.child.size() ; i++){
            if(find(node.child.get(i), info))
                return true;
        }
        return false;
    }
}
