package sample.springboot.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sample.springboot.dto.User;
import sample.springboot.req.QueryUsersReq;
import sample.springboot.resp.QueryUsersResp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
public class UserController {
    @Autowired
    private UserDao userDao;

    private static Map<String, Integer> countMap = new HashMap<>();

    @RequestMapping(value = "updateCount", method = {RequestMethod.GET})
    public synchronized String updateCount(String username) throws InterruptedException {
        Integer count = countMap.get(username);
        StringBuilder sb = new StringBuilder();
        sb.append("old: " + count + "; ");

        if (count == null) {
            count = 1;
        } else {
            count ++;
        }
        countMap.put(username, count);

        sb.append("new: " + count + "; ");

        Thread.sleep(1000 * 60);

        return sb.toString();
    }

    @RequestMapping(value = "queryUsers", method = {RequestMethod.POST})
    public QueryUsersResp queryUsers(@RequestBody QueryUsersReq req) {
        QueryUsersResp resp = new QueryUsersResp();
        User condition = new User();
        condition.setUsername(req.getUsername());
        List<User> users = userDao.queryUsers(condition);
        //resp.setUsers(users);

        return resp;
    }

    @RequestMapping(value = "test", method = {RequestMethod.GET})
    public String test() {
        System.out.println(this.getClass().getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println("QueryUsersResp.classLoader: " + new QueryUsersResp().getClass().getClassLoader());
        return new QueryUsersResp().toString();
    }

    @RequestMapping(value = "batchProcess", method = {RequestMethod.GET})
    public String processLargeFile(@RequestParam("pageSize") long pageSize, @RequestParam("count") long count) throws InterruptedException, IOException {
        for (int i = 0; i < count; i++) {
//            List<Object> list = new ArrayList<>();
//            for (int j = 0; j < age; j++) {
//                FileInputStream input = new FileInputStream("_1M.txt");
//                byte[] bytes = IOUtils.toByteArray(input);
//                byte[] bytes = new byte[1024 * 1024];
//                list.add(bytes);
//            }

            List<User> users = userDao.queryUsersByPage(pageSize);
        }

        return "ok";
    }

    @RequestMapping(value = "highCpu", method = {RequestMethod.GET})
    public String highCpu(@RequestParam("count") long count) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i <Runtime.getRuntime().availableProcessors(); i++) {
            Future<Long> future = executorService.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    long start = System.currentTimeMillis();
                    runDeadLoop(count);
                    long end = System.currentTimeMillis();

                    return end - start;
                }
            });

            futures.add(future);
        }

        StringBuilder sb = new StringBuilder();
        for (Future<Long> future : futures) {
            Long i = future.get();
            sb.append(i + "ms ");
        }

        return sb.toString();
    }

    private void runDeadLoop(long count) {
        int i = 1;
        while (count-- > 0) {
            if (i % 2 ==0) {
                i++;
            } else {
                i++;
            }
        }
    }
}
