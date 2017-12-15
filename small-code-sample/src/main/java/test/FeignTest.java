package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.*;
import feign.Feign.Builder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class FeignTest {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        Builder builder = Feign.builder().encoder(new GsonEncoder()).decoder(new GsonDecoder());
        builder = builder.logger(new Slf4jLogger()).logLevel(Logger.Level.FULL);
        TradeApi tradeApi = builder.target(TradeApi.class, "http://127.0.0.1:80");

        Req req = new Req();

        Resp resp = tradeApi.cal(req, "xxx");
        System.out.println(gson.toJson(resp));
    }
}

interface TradeApi {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /test?token={token}")
    Resp cal(Req req, @Param("token") String token);
}

@Getter
@Setter
class Req {

}

class Resp {

}
