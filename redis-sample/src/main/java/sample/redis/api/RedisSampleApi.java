package sample.redis.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sample.redis.config.Cache;

@RestController
public class RedisSampleApi {
    @Autowired
    private Cache cache;

    private long i = 0;

    @RequestMapping(path = "/incBy", method = RequestMethod.GET)
    public long incBy(@RequestParam("num") long num) {
        if (!cache.lock("aaa", Thread.currentThread().getId() + "", 1000)) {
            return -1;
        }


        try {
            i = i + num;
            System.out.println(i);
        } finally {
            cache.unlock("aaa", Thread.currentThread().getId() + "");
        }

        return i;
    }
}
