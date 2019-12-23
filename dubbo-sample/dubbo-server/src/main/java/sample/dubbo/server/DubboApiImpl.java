package sample.dubbo.server;

import org.springframework.stereotype.Service;
import sample.dubbo.server.api.DubboApi;
import sample.dubbo.server.api.DubboReq;
import sample.dubbo.server.api.DubboResp;

@Service
public class DubboApiImpl implements DubboApi {
    @Override
    public DubboResp service(DubboReq req) {
        String body = req.getBody();
        DubboResp resp = new DubboResp();
        resp.setBody("(" + body + ")");

        if (true) {
            throw new RuntimeException("force error");
        }

        return resp;
    }
}
