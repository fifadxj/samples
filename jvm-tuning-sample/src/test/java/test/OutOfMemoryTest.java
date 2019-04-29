package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OutOfMemoryTest {
    private int i = 0;
    private static final int _1MB = 1024 * 1024;
    List list = new ArrayList();
    public void run() {
        while (true) {
            i++;
            list.add(new byte[_1MB]);
        }
    }

    public static void main(String[] args) {
        OutOfMemoryTest test = new OutOfMemoryTest();
        try {
            test.run();
        } catch (Throwable e) {
            System.out.println("loop count: " + test.i);
            e.printStackTrace();
        }
    }
}
