package com.module.data_structure.list.Heaps;

/**
 * Minimum Priority Queue
 * It is a part of heap data structure
 * A heap is a specific tree based data structure
 * in which all the nodes of tree are in a specific order.
 * that is the children are arranged in some
 * respect of their parents, can either be greater
 * or less than the parent. This makes it a min priority queue
 * or max priority queue.
 * <p>
 * <p>
 * Functions: insert, delete, peek, isEmpty, print, heapSort, sink
 */
public class MinPriorityQueue {
    private int[] heap;
    private int capacity;
    private int size;

    MinPriorityQueue(int c){
        this.capacity = c;
        this.size = 0;
        this.heap = new int[c + 1];
    }

    // inserts the key at the end and rearranges it so that the binary heap is in appropriate order
    public void insert(int key){
        if(isFull())return;
        heap[size + 1] = key;
        int k = size + 1;
        while(k > 1){
            if(heap[k] < heap[k / 2]){
                int temp = heap[k];
                heap[k] = heap[k/2];
                heap[k/2] = temp;
            }
            k /= 2;
        }
        size++;
    }



    public int peek(){
        return heap[1];
    }

    public boolean isEmpty(){
        if(size == 0)return true;
        return false;
    }

    public boolean isFull(){
        if(size == capacity)return true;
        return false;
    }
}
