package com.module.data_structure.buffer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class CircularBuffer {
    private char[] buffer;
    public final int buffer_size;
    private int write_index = 0;
    private int read_index = 0;

    private AtomicInteger readable_data = new AtomicInteger(0);

    public CircularBuffer(int buffer_size){
        if(!isPowerOfTwo(buffer_size))
            throw new IllegalArgumentException();
        this.buffer_size = buffer_size;
        buffer = new char[buffer_size];

    }

    public Character readOutCharBuf(){
        Character result = null;

        if(readable_data.get() > 0){
            result =  Character.valueOf(buffer[getTrueIndex(read_index)]);
            readable_data.decrementAndGet();
            read_index++;
        }

        return result;
    }

    public boolean writeToCharBuf(char c){
        boolean result = false;

        if(readable_data.get()< buffer_size){
            buffer[getTrueIndex(write_index)] = c;
            readable_data.incrementAndGet();
            write_index++;
            result = true;
        }

        return result;
    }

    private int getTrueIndex(int i){
        return i % buffer_size;
    }

    private boolean isPowerOfTwo(int i){
        return (i & (i - 1)) == 0;
    }

}
