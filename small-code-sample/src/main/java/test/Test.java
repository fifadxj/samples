package test;

import org.apache.commons.io.IOUtils;
import sample.datastructure.DigitTransform;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by za-daixiaojun on 2017/12/20.
 */
public class Test {
    public static volatile boolean flag = false;
    public static int n = 0;

    public static void main(String[] args) throws Exception {
        //lock();
        sync();
    }

    public static void lock() throws Exception {
        final CountDownLatch latch = new CountDownLatch(2);
        final Lock lock = new ReentrantLock();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    lock.lock();
                    try {
                        n++;
                    } finally {
                        lock.unlock();
                    }
                }
                latch.countDown();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    lock.lock();
                    try {
                        n++;
                    } finally {
                        lock.unlock();
                    }
                }
                latch.countDown();
            }
        });
        t.start();
        t2.start();

        long start = System.currentTimeMillis();
        latch.await();
        System.out.println(n);
        System.out.println("cost " + (System.currentTimeMillis() - start) + " ms");
    }

    public static void sync() throws Exception {
        final CountDownLatch latch = new CountDownLatch(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    synchronized (Test.class) {
                        n++;
                    }
                }
                latch.countDown();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    synchronized (Test.class) {
                        n++;
                    }
                }
                latch.countDown();
            }
        });
        t.start();
        t2.start();

        long start = System.currentTimeMillis();
        latch.await();
        System.out.println(n);
        System.out.println("cost " + (System.currentTimeMillis() - start) + " ms");
    }
}
