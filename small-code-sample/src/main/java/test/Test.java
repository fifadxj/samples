package test;

import org.apache.commons.io.IOUtils;

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
        String i = new Random().nextInt() + "-VALUE";
        System.out.println(i);
        System.out.println(i.hashCode());
        System.out.println(i.hashCode() % 64 / 16);
        System.out.println(i.hashCode() % 64);

//        i = "1828";
//        System.out.println(i.hashCode());
//        System.out.println(i.hashCode() % 64);

        String logicSQL = IOUtils.toString(Test.class.getResourceAsStream("/hello.txt"));
        System.out.println(System.getProperty("line.separator"));
        System.out.println(logicSQL);
        String format = logicSQL.replaceAll(System.getProperty("line.separator"), " ");
        System.out.println(format);
    }
}
