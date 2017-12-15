package sample.designpattern.chain1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chain {
    List<Handler> handlers;
    int i;
    
    public Chain() {
        handlers = new ArrayList<Handler>();
        int i = 0;
    }
    public void addHandler(Handler handler) {
        handlers.add(handler);
    }
    public void removeHandler(Handler handler) {
        handlers.remove(handler);
    }
    public void clearHandlers() {
        handlers.clear();
    }
    public void invoke(Map context) {
        if (i < handlers.size()) {
            Handler handler = handlers.get(i++);
            handler.invoke(this, context);
        }
        else {
            System.out.println("do actual action");
        }
    }
}
