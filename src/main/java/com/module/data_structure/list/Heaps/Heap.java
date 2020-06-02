package com.module.data_structure.list.Heaps;

public interface Heap {
    /**
     *
     * @return
     * @throws EmptyHeapException
     */
    HeapElement getElement() throws EmptyHeapException;

    /**
     *
     * @param element
     */
    void insertElement(HeapElement element);

    /**
     *
     * @param elementIndex
     */
    void deleteElement(int elementIndex);
}
