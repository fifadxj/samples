package sample.dubbo.server.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DubboReq implements Serializable {
    private String body;
}
