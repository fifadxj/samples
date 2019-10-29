package test;

import org.apache.commons.io.IOUtils;
import sample.datastructure.DigitTransform;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Created by za-daixiaojun on 2017/12/20.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("123");
            }
        });
        t.start();

        t = new Thread(() -> System.out.print("456"));
        t.start();
    }
}
