package sample.designpattern.chain1;

import java.util.Map;

public interface Handler {
    void invoke(Chain chain, Map context);
}
