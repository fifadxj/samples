package test;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import sample.utils.HttpUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestHttp {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        List<String> lines = IOUtils.readLines(new FileInputStream("D:\\temp\\ccc\\urls.txt"), "UTF-8");

        final AtomicInteger i = new AtomicInteger(0);
        for (final String line : lines) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //System.out.println(line);
                        HttpResponse resp = HttpUtils.get(line);
                        String html = EntityUtils.toString(resp.getEntity(), Charsets.UTF_8);
                        System.out.println(html);
                        System.out.println(i.incrementAndGet());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        executorService.shutdown();
    }
}
