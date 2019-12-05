package sample.springboot.gc;

/**
 * -Xmx20m -Xms20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class GcTest {
    private static final int _1M = 1024 * 1024;
    public static void main(String[] args) {
        byte[] a1, a2, a3, a4, a5;
        a1 = new byte[_1M * 9];
        a2 = new byte[_1M * 9];
    }
}
