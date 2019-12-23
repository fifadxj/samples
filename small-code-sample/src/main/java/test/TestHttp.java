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
        int i = 0;
        while (i++<10) {
            HttpResponse resp = HttpUtils.get("http://127.0.0.1:8888/a.html");
            String html = EntityUtils.toString(resp.getEntity(), Charsets.UTF_8);
            System.out.println(html);
        }
    }
}
