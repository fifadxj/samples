package sample.designpattern.chain2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Chain {
    List<Handler> handlers;
    
    public Chain() {
        handlers = new ArrayList<Handler>();
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
        for (int i = 0; i < handlers.size(); i++) {
            Handler handler = handlers.get(i);
            handler.before(context);
        }
        
        System.out.println("do actual action");
        
        for (int i = handlers.size() - 1; i >= 0; i--) {
            Handler handler = handlers.get(i);
            handler.after(context);
        }
    }
}
