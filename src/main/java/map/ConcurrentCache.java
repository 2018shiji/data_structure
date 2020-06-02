package map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用WeakHashMap做二级缓存
 * 存储过期缓存数据或者低频缓存数据
 */
public class ConcurrentCache<Key, Value> {
    private final int size;
    private final Map<Key, Value> firstCache;
    private final Map<Key, Value> secondCache;

    public ConcurrentCache(int size) {
        this.size = size;
        this.firstCache = new ConcurrentHashMap<>(size);
        this.secondCache = new WeakHashMap<>(size);
    }

    public Value get(Key key) {
        Value value = firstCache.get(key);
        if(value == null){
            synchronized (secondCache){
                value = secondCache.get(key);
            }
            if(value != null) firstCache.put(key, value);
        }
        return value;
    }

    public void put(Key key, Value value) {
        if(firstCache.size() >= size){
            synchronized (secondCache) {
                secondCache.putAll(firstCache);
            }
            firstCache.clear();
        }
        firstCache.put(key, value);
    }

    /**
     * test WeakHashMap
     * @param args
     */
    public static void main(String[] args) {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>(10);

        String key0 = new String("key0");
        String key1 = new String("key1");
        String key2 = new String("key2");

        //存放元素
        weakHashMap.put(key0, "value0");
        weakHashMap.put(key1, "value1");
        weakHashMap.put(key2, "value2");

        weakHashMap.keySet().stream().forEach(item -> System.out.println(item));
        weakHashMap.values().stream().forEach(item -> System.out.println(item));

        weakHashMap.remove(key0);
        weakHashMap.keySet().stream().forEach(item -> System.out.println(item));
        weakHashMap.values().stream().forEach(item -> System.out.println(item));

        /**
         * 这意味着弱键key1再没有被其他对象引用，
         * 调用gc时会回收weakHashMap中与key1对应的键值对
         */
        key1 = null;
        /** 内存回收，这里会回收weakHashMap中与key1对应的键值对 **/
        System.gc();
        try{Thread.sleep(100);}catch(InterruptedException e) {e.printStackTrace();}

        Iterator iterator = weakHashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            System.out.println("key = " + entry.getKey() + "     value = " + entry.getValue());
        }
        System.out.println("after gc, weakHashMap's size = " + weakHashMap.size());
    }

    public void getMapOOM() {
        Map<Integer, Byte[]> map = null;

        /** -Xmx5M 没有OOM **/
        map = new WeakHashMap<>();
        for(int i = 0; i < 10000; i++){
            Integer integer = i;
            map.put(integer, new Byte[i]);
        }

        /** -Xmx5M 发现OOM java.lang.OutOfMemoryError: Java heap space **/
        map = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            Integer integer = i;
            map.put(integer, new Byte[i]);
        }

        /** 如果存放再WeakHashMap中的key都存在强引用，那么WeakHashMap就会退化为HashMap **/
        /** -Xmx5M java.lang.OutOfMemoryError: Java heap space **/
        map = new WeakHashMap<>();
        List list = new ArrayList();
        for(int i = 0; i < 10000; i++){
            Integer integer = i;
            map.put(integer, new Byte[i]);
            list.add(integer);
        }

    }
}
