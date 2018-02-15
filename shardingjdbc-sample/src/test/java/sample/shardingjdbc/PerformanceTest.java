package sample.shardingjdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sample.shardingjdbc.resp.SqlResp;
import sample.shardingjdbc.service.SqlService;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by za-daixiaojun on 2018/1/11.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
@Service
public class PerformanceTest {
    static int n = 10000;
    static ExecutorService executorService = Executors.newFixedThreadPool(1000);
    @Autowired
    SqlService sqlService;

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        PerformanceTest test = context.getBean(PerformanceTest.class);

        long start = System.currentTimeMillis();
        test.insertTest();
        System.out.println("time cost:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        test.updateTest();
        System.out.println("time cost:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        test.selectTest();
        System.out.println("time cost:" + (System.currentTimeMillis() - start));

        executorService.shutdown();
    }


    public void insertTest() throws Exception {
        final CountDownLatch latch = new CountDownLatch(n);
        final AtomicInteger inc = new AtomicInteger(0);

        for (int i = 0; i < n; i++) {
            String sql = "INSERT INTO `user` (`id`, `username`, `password`, `mobile`) VALUES ($i, '$i', '$i', '$i')";

            final String sql2 = sql.replaceAll("\\$i", i+"");

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sqlService.update(sql2);
                        inc.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        System.out.println("success: " + inc.get());
    }

    public void updateTest() throws Exception {
        final CountDownLatch latch = new CountDownLatch(n);
        final AtomicInteger inc = new AtomicInteger(0);

        for (int i = 0; i < n; i++) {
            String sql = "update user set username = '$i$i' where id=$i";

            final String sql2 = sql.replaceAll("\\$i", i+"");

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sqlService.update(sql2);
                        inc.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        System.out.println("success: " + inc.get());
    }

    public void selectTest() throws Exception {
        final CountDownLatch latch = new CountDownLatch(n);
        final AtomicInteger inc = new AtomicInteger(0);

        for (int i = 0; i < n; i++) {
            String sql = "select username from user where id = $i";

            final String sql2 = sql.replaceAll("\\$i", i+"");
            final int ii = i;


            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        long start = System.currentTimeMillis();
                        SqlResp resp = sqlService.query(sql2, null);
                        if (resp.isSuccess() && resp.getRows().size() == 1 && resp.getRows().get(0).get(0).equals(ii+""+ii)) {
                            inc.incrementAndGet();
                        }

                        long duration = System.currentTimeMillis() - start;
//                        if (duration > 1 * 1000) {
//                            System.out.println(duration);
//                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        System.out.println("success: " + inc.get());
    }
}
