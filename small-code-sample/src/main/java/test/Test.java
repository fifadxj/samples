package test;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by za-daixiaojun on 2017/12/20.
 */
public class Test {
    public static volatile boolean flag = false;
    public static int n = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(tableSizeFor(8));
    }

    static final int tableSizeFor(int cap) {
        int MAXIMUM_CAPACITY = 1 << 30;
        System.out.println(MAXIMUM_CAPACITY);
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static long count() {
        long count = 0;
        for (long i = 0; i < 500000000; i++) {
            count++;
        }

        return count;
    }

    public static long count2() {
        long count;
        synchronized (Test.class) {
            count = 0;
            for (long i = 0; i < 500000000; i++) {
                count++;
            }
        }

        return count;
    }


    public static String encode(String str) {
        String prefix = "\\u";
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        if (chars == null || chars.length == 0) {
            return null;
        }
        for (char c : chars) {
            sb.append(prefix);
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    public static String decode(String str) {
        String sg = "\\u";
        int a = 0;
        List<String> list = new ArrayList<>();
        while (str.contains(sg)) {
            str = str.substring(2);
            String substring;
            if (str.contains(sg)) {
                substring = str.substring(0, str.indexOf(sg));
            } else {
                substring = str;
            }
            if (str.contains(sg)) {
                str = str.substring(str.indexOf(sg));
            }
            list.add(substring);
        }
        StringBuffer sb = new StringBuffer();
        if (! (list.size() == 0)){
            for (String string : list) {
                sb.append((char) Integer.parseInt(string, 16));
            }
        }
        return sb.toString();
    }

}
