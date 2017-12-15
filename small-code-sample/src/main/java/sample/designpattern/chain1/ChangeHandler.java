package sample.designpattern.chain1;

import java.util.Map;

public class ChangeHandler implements Handler {
    public void invoke(Chain chain, Map context) {
        String text = (String) context.get("text");
        System.out.println("======= touppercase ==========");
        context.put("text", text.toUpperCase());
        chain.invoke(context);
        System.out.println("======= tolowercase ==========");
        context.put("text", text.toLowerCase());
    }

}
