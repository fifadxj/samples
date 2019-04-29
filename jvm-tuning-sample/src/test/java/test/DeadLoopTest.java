package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadLoopTest implements Runnable {

    public void run() {
        int i = 1;
        while (true) {
            if (i % 2 ==0) {
                i++;
            } else {
                i++;
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        DeadLoopTest deadLoopTest = new DeadLoopTest();
        executorService.execute(deadLoopTest);

        DeadLoopTest deadLoopTest2 = new DeadLoopTest();
        executorService.execute(deadLoopTest2);

        DeadLoopTest deadLoopTest3 = new DeadLoopTest();
        executorService.execute(deadLoopTest3);
    }
}
