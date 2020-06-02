package com.module.data_structure.list;

import java.io.Serializable;
import java.util.*;

/**
 * Java：http://www.java2s.com/Code/Java
 * mina中的循环队列：
 * http://www.java2s.com/Code/Java/Collections-Data-Structure/Acircularqueuefrommina.htm
 */
public class CircularQueue<E> extends AbstractList<E> implements List<E>,
        Queue<E>, Serializable {
    private static final long serialVersionUID =1L;
    private static final int DEFAULT_CAPACITY = 4;
    private final int initCapacity;
    private volatile Object[] items;
    private int mask;
    private int first = 0;
    private int last = 0;
    private boolean full;
    private int shrinkThreshold;

    public CircularQueue(){
        this(DEFAULT_CAPACITY);
    }

    public CircularQueue(int initCapacity){
        int actualCapacity = normalizeCapacity(initCapacity);
        this.items = new Object[actualCapacity];
        this.mask = actualCapacity - 1;
        this.initCapacity = actualCapacity;
        this.shrinkThreshold = 0;
    }

    private static int normalizeCapacity(int initCapacity){
        int actualCapacity = 1;
        while(actualCapacity < initCapacity){
            actualCapacity <<= 1;
            if(actualCapacity < 0){
                actualCapacity = 1 << 30;
                break;
            }
        }
        return actualCapacity;
    }

    public int capacity(){
        return this.items.length;
    }

    @Override
    public void clear(){
        if(!isEmpty()){
            Arrays.fill(this.items, null);
            this.first = 0;
            this.last = 0;
            this.full = false;

        }
    }

    private int getRealIndex(int index){
        return (first + index) & mask;
    }

    private void increaseSize(){
        last = (last + 1) & mask;
        full = first == last;
    }

    private void decreaseSize(){
        first = (first + 1) & mask;
        full = false;
    }

    private void checkIndex(int idx){
        if(idx < 0 || idx >= size())
            throw new IndexOutOfBoundsException(String.valueOf(idx));
    }

    private void expandIfNeeded(){
        if(full){
            //expand queue
            final int oldLen = items.length;
            final int newLen = oldLen << 1;
            Object[] tmp = new Object[newLen];

            if(first < last)
                System.arraycopy(items, first, tmp, 0, last - first);
            else{
                System.arraycopy(items, first, tmp, 0, oldLen - first);
                System.arraycopy(items, 0, tmp, oldLen - first, last);
            }

            first = 0;
            last = oldLen;
            items = tmp;
            mask = tmp.length - 1;
            if(newLen >>> 3 > initCapacity)
                shrinkThreshold = newLen >>> 3;
        }
    }

    private void shrinkIfNeeded(){
        int size = size();
        if(size <= shrinkThreshold){
            //shrink queue
            final int oldLen = items.length;
            int newLen = normalizeCapacity(size);
            if(size == newLen)
                newLen <<= 1;
            if(newLen >= oldLen)
                return;

            if(newLen < initCapacity){
                if(oldLen == initCapacity)
                    return;
                else
                    newLen = initCapacity;
            }

            Object[] tmp = new Object[newLen];

            //Copy only when there is something to copy
            if(size > 0){
                if(first < last)
                    System.arraycopy(items, first, tmp, 0, last - first);
                else{
                    System.arraycopy(items, first, tmp, 0, oldLen - first);
                    System.arraycopy(items, 0, tmp, oldLen - first, last);
                }
            }

            first = 0;
            last = size;
            items = tmp;
            mask = tmp.length - 1;
            shrinkThreshold = 0;
        }

    }

    @Override
    public boolean isEmpty(){
        return (this.first == this.last) && !this.full;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return (E)items[getRealIndex(index)];
    }

    @Override
    public int size() {
        if(full)
            return capacity();
        if(last >= first)
            return last - first;
        else
            return last - first + capacity();
    }

    @Override
    public boolean add(E o){
        return offer(o);
    }

    @Override
    public boolean offer(E e) {
        if(e == null)
            throw new NullPointerException("item");
        expandIfNeeded();
        items[last] = e;
        increaseSize();
        return true;
    }

    @Override
    public E set(int index, E o){
        checkIndex(index);

        int realIdx = getRealIndex(index);
        Object old = items[realIdx];
        items[realIdx] = o;
        return (E)old;
    }

    @Override
    public void add(int index, E o){
        if(index == size()){
            offer(o);
            return;
        }

        checkIndex(index);
        expandIfNeeded();

        int realIndex = getRealIndex(index);

        //make a room for a new element
        if(first < last) {
            System.arraycopy(items, realIndex, items, realIndex + 1, last - realIndex);
        }else{
            if(realIndex >= first){
                System.arraycopy(items, 0, items, 1, last);
                items[0] = items[items.length - 1];
                System.arraycopy(items, realIndex, items, realIndex + 1, items.length - realIndex -1);
            }else{
                System.arraycopy(items, realIndex, items, realIndex + 1, last - realIndex);
            }
        }

        items[realIndex] = o;
        increaseSize();
    }

    @Override
    public E remove(int index){
        if(index == 0)
            return poll();

        checkIndex(index);

        int realIndex = getRealIndex(index);
        Object removed = items[realIndex];

        //Remove a room for the removed element
        if(first < last)
            System.arraycopy(items, first, items, first + 1, realIndex - first);
        else{
            if (realIndex >= this.first) {
                System.arraycopy(items, first, items, first + 1, realIndex - first);
            } else {
                System.arraycopy(items, 0, items, 1, realIndex);
                items[0] = items[items.length - 1];
                System.arraycopy(items, first, items,
                        first + 1, items.length - first - 1);
            }
        }

        items[first] = null;
        decreaseSize();

        shrinkIfNeeded();
        return (E)removed;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public E poll() {
        if(isEmpty())
            return null;

        Object ret = items[first];
        items[first] = null;
        decreaseSize();

        if(first == last)
            first = last = 0;
        shrinkIfNeeded();
        return (E)ret;
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return peek();
    }

    @Override
    public E peek() {
        if(isEmpty())
            return null;
        return (E)items[first];
    }

    @Override
    public String toString() {
        return "first=" + first + ", last=" + last + ", size="
                + size() + ", mask = " + mask;
    }
}
