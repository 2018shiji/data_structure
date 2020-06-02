package AQS;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TReentrantLock {
    static ReentrantLock reentrantLock = new ReentrantLock(true);
    static volatile Thread[] threads = new Thread[1];

    public static void main(String[] args){
        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(() -> doTask(), "线程" + i);
            threads[i].start();
        }
    }

    private static void doTask() {
        for(int i = 0; i < 3; i++) {
            try {
                reentrantLock.lock();
                System.out.println(Thread.currentThread().getName() + "获取了锁");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
