import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws Exception {
//        Map<String, String> map = new ConcurrentHashMap<String, String>();
//        map.put("a", null);
//        System.out.println();

//        List<Number> list = new ArrayList<>();
//        list.addAll(new ArrayList<Integer>());

        //test1();
        //test2();

        int i = 2147483647;
        System.out.println(i);
    }

    public static void test2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> f = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer i = 0;
                while (i >= 0) {
                    i = i + 1;
                    System.out.print(i);
                    //Thread.sleep(100);
                }
                return i;
            }
        });

        Thread.sleep(1000);
        f.cancel(true);
        System.out.println();
        System.out.println("========");
    }

    public static void test1() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(-3);
        list.add(2);
        Stream<Integer> nameStream = list.stream();

        //list = nameStream.filter(o -> o.intValue() > 0).collect(Collectors.toList());
        list.forEach((o) -> System.out.println(o));
        List<String> newList = nameStream.map(o -> String.valueOf(o.intValue() * o.intValue())).collect(Collectors.toList());
        newList.forEach((o) -> System.out.println(o));
        newList.forEach((o) -> System.out.println(o));
    }
}
