package sample.netty.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Callback {
    private long reqId;
    private int timeout = 5;
    byte[] resp = null;

    public Callback(long id, int timeout) {
        this.reqId = id;
        this.timeout = timeout;
    }

    public byte[] getResp() {
        synchronized (this) {
            if (resp != null) {
                return resp;
            }

            else {
                try {
                    this.wait(timeout * 1000);
                } catch (Exception e) {
                    log.error("[" + reqId + "]Waiting for getting response catch exception.", e);
                }
            }
        }
        return resp;
    }

    public void setResp(byte[] resp) {
        this.resp = resp;
        synchronized (this) {
            try {
                this.notifyAll();
            } catch (Exception e) {
                log.error("[" + reqId + "]set response catch exception.", e);
            }
        }
    }

}
