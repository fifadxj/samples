package sample.designpattern.chain2;

import java.util.Map;

public class PrintHandler implements Handler {
    public void before(Map context) {
        String text = (String) context.get("text");
        System.out.println("start: " + text);
    }

    public void after(Map context) {
        String text = (String) context.get("text");
        System.out.println("end: " + text);
    }
}
