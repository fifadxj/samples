package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

/**
 * Created by za-daixiaojun on 2017/12/20.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        long start = System.currentTimeMillis();
        try {
            socket.connect(new InetSocketAddress("192.164.1.2", 1111), 60000);
        } finally {
            System.out.println(System.currentTimeMillis() - start);
        }


    }
}
