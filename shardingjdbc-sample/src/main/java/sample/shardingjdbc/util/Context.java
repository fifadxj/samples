package sample.shardingjdbc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by za-daixiaojun on 2018/1/11.
 */
public class Context {
    public static ThreadLocal<List<String>> SQL_EXECUTE_LIST = new ThreadLocal<List<String>>() {
        @Override public List<String> initialValue() {
            return new ArrayList<String>();
        }
    };
}
