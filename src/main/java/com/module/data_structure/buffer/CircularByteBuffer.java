package com.module.data_structure.buffer;

import java.util.Arrays;

/**
 * 循环ByteBuffer，为一个byte[]数组，长度为capacity，首尾相连，构成一个环形，
 * 按照操作数组的方向写数据和读数据，可以循环使用，可自动扩容，非线程安全
 * https://my.oschina.net/myumen/blog/1073909
 */
public class CircularByteBuffer {
    /** 默认大小为16字节 */
    private final static int DEFAULT_CAPACITY = 16;

    /** 存放数据的字节数组 */
    private byte[] buf = null;

    /** 容量/大小 */
    private int capacity;

    /** 写数据下标，下一个可写的字节对应的数组索引 */
    private int readIndex = 0;

    /** 读数据下标，下一个可读的字节对应的数组索引 */
    private int writeIndex = 0;

    /** 是否为空，为空时，writeIndex和readIndex相等，反过来则不一定 */
    private boolean empty = true;

    public CircularByteBuffer(){
        this.capacity = DEFAULT_CAPACITY;
        this.buf = new byte[capacity];
    }

    public CircularByteBuffer(int cap){
        this.capacity = cap;
        this.buf = new byte[cap];
    }

    public void storeData(byte[] data){
        if(data != null)
            storeData(data, data.length);
    }

    public void storeData(byte[] data, int limit){
        if(data == null || data.length == 0)
            return;
        if(limit <= 0 || limit > data.length)
            throw new IllegalArgumentException("参数错误：参数limit值为" + limit);

        int writeableBytes = getWriteableBytes();
        while(limit > writeableBytes){
            //数据超过可用空间，空间扩展后，直到够用
            extendCapacity();
            writeableBytes = getWriteableBytes();
        }

        if(writeIndex >= readIndex){//存入数据，可用空间为数组尾部和头部的不连续区域
            int arrow = capacity - writeIndex;
            if(limit <= arrow){//尾部空间够用
                System.arraycopy(data, 0, buf, writeIndex, limit);
                writeIndex += arrow;
                writeIndex = writeIndex % capacity;
            }else{//尾部空间不足，分两段存，存头尾
                System.arraycopy(data, 0, buf, writeIndex, arrow);
                writeIndex += arrow;
                writeIndex = writeIndex % capacity;

                //需要存入数组头部的数据长度
                int circle = limit - arrow;
                System.arraycopy(data, arrow, buf, writeIndex, circle);
                writeIndex += circle;
                writeIndex = writeIndex % capacity;
            }
        }else{//可用空间为数组中部连续区域
            System.arraycopy(data, 0, buf, writeIndex, limit);
            writeIndex += limit;
            writeIndex = writeIndex % capacity;
        }

        empty = false;
    }

    /**
     * 提取数据，可根据参数preFetch做预读取
     * @param size
     * @param preFetch
     * @return
     */
    public byte[] fetchData(int size, boolean preFetch){
        int readableBytes = getReadableBytes();
        if(readableBytes == 0)
            return null;

        //最终读取的长度
        int len = size >= readableBytes ? readableBytes : size;
        byte[] target = new byte[len];
        int oldReadIndex = readIndex;

        if(writeIndex > readIndex){//数据连续
            System.arraycopy(buf, readIndex,target,0,len);
            readIndex += len;
            readIndex = readIndex % capacity;
        }else{//数据不连续
            int arrow = capacity - readIndex;
            if(len <= arrow){
                System.arraycopy(buf, readIndex, target,  0, len);
                readIndex += len;
                readIndex = readIndex % capacity;
            }else{
                int circle = len - arrow;
                System.arraycopy(buf, readIndex, target,0, arrow);
                readIndex += arrow;
                readIndex = readIndex % capacity;
                System.arraycopy(buf, readIndex, target, arrow, circle);
                readIndex += circle;
                readIndex = readIndex % capacity;
            }
        }

        //如果是预读取，则需要将readIndex复位，以便下次能继续读取同样的数据
        if(preFetch)
            readIndex = oldReadIndex;

        //读完数据，如果读写指针相同，则表示读空了
        if(readIndex == writeIndex)
            empty = true;

        return target;
    }

    public byte[] fetchData(int size){
        return fetchData(size, false);
    }

    /**
     * 扩容，capacity = capacity * 2
     */
    private void extendCapacity(){
        byte[] storedData = fetchData(getReadableBytes(), true);
        this.capacity *= 2;
        buf = new byte[capacity];
        storeData(storedData);
    }


    /**
     * 得到可用存储空间大小
     * @return 可用于存储的字节数
     */
    private int getWriteableBytes(){
        int writeableBytes = 0;
        if(writeIndex == readIndex)
            writeableBytes = empty ? capacity : 0;
        else
            writeableBytes = writeIndex > readIndex ? (capacity - writeIndex + readIndex) : (readIndex - writeIndex);
    return writeableBytes;
    }

    /**
     * 得到可读取的数据长度
     * @return可读取的字节数
     */
    private int getReadableBytes(){
        int readableBytes = 0;
        if(writeIndex == readIndex)
            readableBytes = empty ? 0 : capacity;
        else
            readableBytes = writeIndex > readIndex ? (writeIndex - readIndex) : (capacity - readIndex + writeIndex);
        return readableBytes;
    }

    public String toString() {
        if (this.buf == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(capacity + 100);
        sb.append("CircleByteBuffer [\n\tbuf=");
        sb.append(Arrays.toString(buf));
        sb.append("\n\twritePointer:\t" + writeIndex);
        sb.append("\n\treadPointer:\t" + readIndex);
        sb.append("\n\twriteableBytes:\t" + getWriteableBytes());
        sb.append("\n\treadableBytes:\t" + getReadableBytes());
        sb.append("\n\tcapacity:\t" + capacity);
        sb.append("\n\tempty:\t" + empty);
        sb.append("\n]");
        return sb.toString();
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return empty;
    }
}
