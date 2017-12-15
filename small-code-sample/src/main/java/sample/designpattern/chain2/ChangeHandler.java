package sample.designpattern.chain2;

import java.util.Map;

public class ChangeHandler implements Handler {
    public void before(Map context) {
        String text = (String) context.get("text");
        context.put("text", text.toUpperCase());
    }
    public void after(Map context) {
        String text = (String) context.get("text");
        context.put("text", text.toLowerCase());
    }
}
