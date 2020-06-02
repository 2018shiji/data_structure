package com.module.data_structure.list.Heaps;

public class HeapElement {
    private final double key;
    private final Object additionalInfo;

    public HeapElement(double key, Object info) {
        this.key = key;
        this.additionalInfo = info;
    }

    /**
     * @return the object containing the additional info provided by the user.
     */
    public Object getInfo() {
        return additionalInfo;
    }

    /**
     * @return the key value of the element
     */
    public double getKey() {
        return key;
    }

    // Overridden object methods

    public String toString() {
        return "Key: " + key + " - " + additionalInfo.toString();
    }

    /**
     * @param otherHeapElement
     * @return true if the keys on both elements are identical and the additional info objects
     * are identical.
     */
    public boolean equals(HeapElement otherHeapElement) {
        return (this.key == otherHeapElement.key) && (this.additionalInfo.equals(otherHeapElement.additionalInfo));
    }
}
