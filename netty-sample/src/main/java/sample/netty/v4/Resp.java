package sample.netty.v4;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resp {
    private long reqId;
    private byte[] body;
}
