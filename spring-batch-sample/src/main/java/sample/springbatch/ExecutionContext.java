package sample.springbatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutionContext {
    private final Map<String, Object> map;

    /**
     * Default constructor. Initializes a new execution context with an empty
     * internal map.
     */
    public ExecutionContext() {
        map = new ConcurrentHashMap<String, Object>();
    }

    public Object get(String key) {
        return map.get(key);
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }
}
