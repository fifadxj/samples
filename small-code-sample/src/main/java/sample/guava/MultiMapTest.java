package sample.guava;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
public class MultiMapTest {
    public static void main(String[] args) {
        Multimap<Integer, String> map = HashMultimap.create();
        map.put(1, "application2");
        map.put(1, "application1");
        System.out.println(map);
    }
}
