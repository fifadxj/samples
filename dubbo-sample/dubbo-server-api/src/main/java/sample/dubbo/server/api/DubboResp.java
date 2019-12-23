package sample.dubbo.server.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DubboResp implements Serializable {
    private String body;
}
