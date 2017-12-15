package sample.designpattern.chain2;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Chain chain = new Chain();
        chain.addHandler(new PrintHandler());
        chain.addHandler(new ChangeHandler());
        chain.addHandler(new PrintHandler());
        Map context = new HashMap();
        context.put("text", "hello world");
        chain.invoke(context);
    }
}
