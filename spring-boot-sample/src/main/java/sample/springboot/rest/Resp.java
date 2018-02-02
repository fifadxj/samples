package sample.springboot.rest;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
public class Resp {
    private String value;
    private String version;
    private String version2;
    private long id;
    private Date date;
    private int amount;
}
