package sample.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sample.redis.config.Cache;
import sample.redis.config.standalone.v2.UseRedisV2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RedisSample {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(UseRedisV2.class);
        final Cache cache = context.getBean(Cache.class);
//        cache.put("a", "hello", 100);
//        String a = cache.get("a");
//        System.out.println(a);

//        System.out.println("tid:" + Thread.currentThread().getId());


        ExecutorService executorService = Executors.newFixedThreadPool(100);
        final A a = new A();
        final CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++)
         {
             //Thread.sleep(10);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!cache.lock("aaa", Thread.currentThread().getId() + "", 1000)) {
//                            //System.out.println("lock failed");
                            a.b.incrementAndGet();
                            return;
                        } else {
                            System.out.println("lock success");
                        }
                        try {
                            a.a = a.a + 1;
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            boolean unlock = cache.unlock("aaa", Thread.currentThread().getId() + "");
                            if (!unlock) {
                                System.out.println("unlock failed");
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        Thread.sleep(1500);
        cache.lock("aaa", Thread.currentThread().getId() + "", 1000);

        latch.await();
        System.out.println(a.a);
        System.out.println(a.b.get());
    }
}

class A {
    int a = 0;
    AtomicInteger b = new AtomicInteger(0);
}