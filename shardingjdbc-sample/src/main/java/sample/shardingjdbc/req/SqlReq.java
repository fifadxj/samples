package sample.shardingjdbc.req;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by za-daixiaojun on 2018/1/9.
 */
@Getter
@Setter
public class SqlReq {
    private String sql;
    private Integer tableIndex;
}
