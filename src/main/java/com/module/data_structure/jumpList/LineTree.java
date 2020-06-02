package com.module.data_structure.jumpList;


public class LineTree {
    private int[] ints = new int[8];
    private Entry[] lineTree = new Entry[ints.length << 1];

    public LineTree() {
        for (int i = 0; i < 8; i++) {
            ints[i] = i + 1;
        }
    }

    public void build(int index, int left, int right) {
        lineTree[index].leftBound = left;
        lineTree[index].rightBound = right;
        if(left == right){
            lineTree[index].sum = ints[left];
            return;
        }
        int middle = (left + right) >> 1;
        build(index * 2, left, middle);//构造左子树
        build(index * 2 + 1, middle + 1, right);//构造右子树
        lineTree[index].sum = lineTree[index * 2].sum + lineTree[index * 2 + 1].sum;
    }

    class Entry {
        int sum;
        int leftBound;
        int rightBound;
        Entry(int sum, int leftBound, int rightBound){
            this.sum = sum;
            this.leftBound = leftBound;
            this.rightBound = rightBound;
        }
    }

    public static void main(String[] args){
//        LineTree lineTree = new LineTree();
        System.out.println("hello world");
    }

}
