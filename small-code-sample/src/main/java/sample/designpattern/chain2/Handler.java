package sample.designpattern.chain2;

import java.util.Map;

public interface Handler {
    void before(Map context);
    void after(Map context);
}
