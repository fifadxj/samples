package test;

public class StackOverFlowTest {
    private long stackLength = 1;

    public void run() {
        stackLength++;
        run();
    }

    public static void main(String[] args) {
        StackOverFlowTest test = new StackOverFlowTest();
        try {
            test.run();
        } catch (Throwable e) {
            System.out.println("stackLength: " + test.stackLength);
            e.printStackTrace();
        }
    }
}
