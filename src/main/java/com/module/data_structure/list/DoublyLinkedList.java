package com.module.data_structure.list;


public class DoublyLinkedList {

    class Node {
        int value;
        Node next;
        Node prev;

        public Node(int value){
            this.value = value;
        }

        public void display(){
            System.out.println(value + " ");
        }
    }

    private Node head;
    private Node tail;

    public DoublyLinkedList(){}
    public DoublyLinkedList(int[] array){
        if(null == array)throw new NullPointerException();
        for(int i : array)
            insertHead(i);
    }

    /**
     *
     * @param x
     */
    public void insertHead(int x){
        Node newNode = new Node(x);
        if(isEmpty())
            tail = newNode;
        else
            head.prev = newNode;
        newNode.next = head;
        head = newNode;
    }

    public void insertTail(int x){
        Node newNode = new Node(x);
        if(isEmpty())
            head = newNode;
        else
            tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
    }

    /**
     *
     * @param value
     */
    public void insertOrdered(int value){
        Node newNode = new Node(value);
        Node current = head;

        while(current != null && value > current.value)//Find the position to insert
            current = current.next;
        if(current == head)
            insertHead(value);
        else if(current == null)
            insertTail(value);

        else{ //Before: 1<--> 2(current) <--> 3
            newNode.prev = current.prev; // 1 <-- newNode
            current.prev.next = newNode; // 1<--> newNode
            newNode.next = current; // 1<--> newNode --> 2(current) <--> 3
            current.prev = newNode; // 1 <--> newNode <--> 2(current) <--> 3
        }
    }

    /**
     *
     * @return
     */
    public Node deleteHead() {
        Node temp = head;
        head = head.next;
        head.prev = null;
        if (head == null)
            tail = null;
        return temp;
    }

    public Node deleteTail(){
        Node temp = tail;
        tail = tail.prev;
        tail.next = null;
        if(tail == null)
            head = null;
        return temp;
    }

    public void delete(int value){
        Node current = head;

        while(current.value != value)
            current = current.next;

        if(current == head)
            deleteHead();
        else if(current == tail)
            deleteTail();
        else{ // Before: 1 <--> 2(current) <--> 3
            current.prev.next = current.next; // 1 --> 3
            current.next.prev = current.prev; // 1 <-- 3
        }
    }


    public boolean isEmpty(){
        return (head == null);
    }

    public void display(){ //Prints contents of the list
        Node current = head;
        while(current != null){
            current.display();
            current = current.next;
        }
        System.out.println();
    }
}
