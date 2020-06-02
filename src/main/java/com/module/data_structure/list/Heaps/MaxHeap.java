package com.module.data_structure.list.Heaps;

import java.util.ArrayList;
import java.util.List;

public class MaxHeap implements Heap{

    private final List<HeapElement> maxHeap;

    public MaxHeap(List<HeapElement> listElements){
        maxHeap = new ArrayList<>();
        for(HeapElement ele : listElements){
            if(ele != null)
                insertElement(ele);
            else
                System.out.println("null element, not add to heap");
            if(maxHeap.size() == 0)
                System.out.println("no element has been added, empty heap");
        }
    }


    @Override
    public HeapElement getElement() throws EmptyHeapException {
        try {
            return extractMax();
        }catch(Exception e){
            throw new EmptyHeapException("heap is empty");
        }
    }

    @Override
    public void insertElement(HeapElement element) {
        maxHeap.add(element);
        toggleUp(maxHeap.size());
    }

    @Override
    public void deleteElement(int elementIndex) {
        if(maxHeap.isEmpty())
            try{
                throw new EmptyHeapException("Attempt to delete an element from an empty heap");
            }catch(Exception e){
                e.printStackTrace();
            }

        if((elementIndex > maxHeap.size()) || (elementIndex <= 0))
            throw new IndexOutOfBoundsException("Index out of heap range");

        //the last element in heap replaces the ont to be deleted
        maxHeap.set(elementIndex - 1, getElement(maxHeap.size()));
        maxHeap.remove(maxHeap.size());

        // Shall the new element be moved up...
        if (getElementKey(elementIndex) > getElementKey((int) Math.floor(elementIndex / 2))) toggleUp(elementIndex);
        // ... or down ?
        else if (((2 * elementIndex <= maxHeap.size()) && (getElementKey(elementIndex) < getElementKey(elementIndex * 2))) ||
                ((2 * elementIndex < maxHeap.size()) && (getElementKey(elementIndex) < getElementKey(elementIndex * 2))))
            toggleDown(elementIndex);
    }

    public HeapElement getElement(int index){
        if((index <= 0) || (index > maxHeap.size()))
            throw new IndexOutOfBoundsException("Index out of heap range");
        return maxHeap.get(index - 1);
    }

    /**
     * Toggle an element up to its right place as its key is lower than its parent's
     * @param index
     */
    private void toggleUp(int index){
        double key = maxHeap.get(index - 1).getKey();
        while(getElementKey((int)Math.floor(index/2)) < key){
            swap(index, (int)Math.floor(index/2));
            index = (int)Math.floor(index/2);
        }
    }

    /**
     * Toggle an element down to its right place as long as its key is higher than any of its children's
     * @param index
     */
    private void toggleDown(int index){
        double key = maxHeap.get(index - 1).getKey();
        boolean wrongOrder = (key < getElementKey(index * 2) ||
                (key < getElementKey(Math.min(index * 2, maxHeap.size()))));
        while((2 * index < maxHeap.size()) && wrongOrder){
            if((2 * index <maxHeap.size()) && (getElementKey(index * 2 + 1) > getElementKey(index * 2))){
                swap(index, 2 * index + 1);
                index = 2 * index + 1;
            }else{
                swap(index, 2 * index);
                index = 2 * index;
            }
            wrongOrder = (key < getElementKey(index * 2) ||
                    (key < getElementKey(Math.min(index * 2, maxHeap.size()))));
        }
    }

    private HeapElement extractMax(){
        HeapElement result = maxHeap.get(0);
        deleteElement(0);
        return result;
    }

    private void swap(int index1, int index2){
        HeapElement tempEle = maxHeap.get(index1 - 1);
        maxHeap.set(index1 - 1, maxHeap.get(index2 - 1));
        maxHeap.set(index2 - 1, tempEle);
    }

    private double getElementKey(int index){
        return maxHeap.get(index - 1).getKey();
    }

}
