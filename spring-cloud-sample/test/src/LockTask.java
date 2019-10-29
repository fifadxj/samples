import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTask implements Runnable {
    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " finish lock");
        try {
            System.out.println("running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "finish unlock");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockTask lockTask = new LockTask();
        Thread t1 = new Thread(lockTask, "t1");
        Thread t2 = new Thread(lockTask, "t2");
        t1.start();
        Thread.sleep(10);
        t2.start();
    }

}
