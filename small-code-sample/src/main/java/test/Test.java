package test;

import org.apache.commons.io.IOUtils;
import sample.datastructure.DigitTransform;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Random;

/**
 * Created by za-daixiaojun on 2017/12/20.
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        long value = 259753824552722432L;
//        long value2 = value >> 12 << 12 | 0001;
//        long value3 = value << 52 << 52;
//        System.out.println(value);
//        System.out.println(value2);
//        System.out.println(value3);
//        System.out.println(Long.toBinaryString(value));
//        System.out.println(Long.toBinaryString(value2));
//
//        if (true) return;

 //        String i = new Random().nextInt() + "-VALUE";
//        System.out.println(i);
//        System.out.println(i.hashCode());
//        System.out.println(i.hashCode() % 64 / 16);
//        System.out.println(i.hashCode() % 64);
//
////        i = "1828";
////        System.out.println(i.hashCode());
////        System.out.println(i.hashCode() % 64);
//
//        String logicSQL = IOUtils.toString(Test.class.getResourceAsStream("/hello.txt"));
//        System.out.println(System.getProperty("line.separator"));
//        System.out.println(logicSQL);
//        String format = logicSQL.replaceAll(System.getProperty("line.separator"), " ");
//        System.out.println(format);

        //259753824552722432L
        long orignal = 280007445634592767L;

        String a = Long.toBinaryString(orignal);
        int prefix = 64 - a.length();
        StringBuilder sb = new StringBuilder();
        System.out.println(prefix);
        for (int i = 0; i < prefix; i++) {
            sb.append(0);
        }

        String binary = sb.append(Long.toBinaryString(orignal)).toString();
        System.out.println(binary);

        long sum = 0L;
        for (int i = 0 ; i < binary.length(); i++) {
            //System.out.print(i + ",");
            //char c = binary.charAt(i);
            //System.out.print(c);
            String s = binary.substring(i, i + 1);
            int ii = Integer.valueOf(s);
            //System.out.print(ii + ",");
            //System.out.print(ii * Math.pow(2, i) + ",");
            sum += ii * Math.pow(2, binary.length() - i - 1);
        }
        System.out.println(sum);
//        Long b = Long.valueOf((long)Math.pow(2, 63) - 1);
//        System.out.println(b);
    }
}
