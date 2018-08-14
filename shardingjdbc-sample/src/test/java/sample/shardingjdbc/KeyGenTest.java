package sample.shardingjdbc;

import com.google.common.primitives.Longs;
import io.shardingjdbc.core.keygen.DefaultKeyGenerator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class KeyGenTest {
    public static void main(String[] args) throws Exception {
        Long id = 216735132445315072L;
        id = 216903673456988160L;
        id = 216903774837514240L;
        System.out.println(Long.toBinaryString(id));


        InetAddress address = InetAddress.getByName("10.152.62.25");
        byte[] ipAddressByteArray = address.getAddress();
        long i = ((long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF)));
        System.out.println(i);

        address = InetAddress.getByName("10.152.62.26");
        ipAddressByteArray = address.getAddress();
        i = ((long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF)));
        System.out.println(i);

        address = InetAddress.getLocalHost();


        Enumeration<NetworkInterface> nets = NetworkInterface
                .getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            if (null != netint.getHardwareAddress()) {
                List<InterfaceAddress> list = netint.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : list) {
                    String localip=interfaceAddress.getAddress().toString();
                    System.out.println(localip);
                }
            }
    }
}
