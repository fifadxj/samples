package sample.designpattern.producerConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionPool<E> {
    private List<E> tasks = new ArrayList<>();
    private int max;

    private Lock lock;
    private Condition notFull;
    private Condition notEmpty;

    public LockConditionPool(int max) {
        this.max = max;
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    public void produce(E task) throws InterruptedException {
        lock.lock();
        try {
            while (tasks.size() == max) {
                notFull.await();
            }

            this.tasks.add(task);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (tasks.size() == 0) {
                notEmpty.await();
            }

            E task = this.tasks.get(0);
            System.out.println(task);
            tasks.remove(0);
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }

    static class Producer implements Runnable {
        private String name;
        private LockConditionPool<String> pool;

        public Producer(String name, LockConditionPool<String> pool) {
            this.name = name;
            this.pool = pool;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    pool.produce("task-" + name);
                    Thread.sleep(Math.abs(new Random().nextInt() % 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private String name;
        private LockConditionPool<String> pool;

        public Consumer(String name, LockConditionPool<String> pool) {
            this.name = name;
            this.pool = pool;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    pool.consume();
                    Thread.sleep(Math.abs(new Random().nextInt() % 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        LockConditionPool<String> pool = new LockConditionPool<>(5);

        for (int i = 0; i < 10; i++) {
            new Thread(new LockConditionPool.Consumer(i+"", pool)).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new LockConditionPool.Producer(i+"", pool)).start();
        }
    }
}
