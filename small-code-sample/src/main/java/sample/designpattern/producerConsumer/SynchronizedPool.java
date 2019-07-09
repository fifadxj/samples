package sample.designpattern.producerConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SynchronizedPool<E> {
    private List<E> tasks = new ArrayList<>();
    private int max;

    public SynchronizedPool(int max) {
        this.max = max;
    }

    synchronized public void produce(E task) throws InterruptedException {
        while (tasks.size() == max) {
            this.wait();
        }

        this.tasks.add(task);
        this.notifyAll();
    }

    synchronized public void consume() throws InterruptedException {
        while (tasks.size() == 0) {
            this.wait();
        }

        E task = this.tasks.get(0);
        System.out.println(task);
        tasks.remove(0);
        this.notifyAll();
    }

    static class Producer implements Runnable {
        private String name;
        private SynchronizedPool<String> pool;

        public Producer(String name, SynchronizedPool<String> pool) {
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
        private SynchronizedPool<String> pool;

        public Consumer(String name, SynchronizedPool<String> pool) {
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
        SynchronizedPool<String> pool = new SynchronizedPool<>(5);

        for (int i = 0; i < 10; i++) {
            new Thread(new Consumer(i+"", pool)).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Producer(i+"", pool)).start();
        }
    }
}
