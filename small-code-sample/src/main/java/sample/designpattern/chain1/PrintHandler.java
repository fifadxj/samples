package sample.designpattern.chain1;

import java.util.Map;

public class PrintHandler implements Handler {
    public void invoke(Chain chain, Map context) {
        String text = (String) context.get("text");
        System.out.println("start: " + text);
        chain.invoke(context);
        text = (String) context.get("text");
        System.out.println("end: " + text);
    }

}
