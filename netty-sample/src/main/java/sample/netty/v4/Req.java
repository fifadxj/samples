package sample.netty.v4;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Req {
    private long reqId;
    private byte[] body;
}
