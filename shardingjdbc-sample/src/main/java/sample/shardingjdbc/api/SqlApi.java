package sample.shardingjdbc.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.shardingjdbc.req.SqlReq;
import sample.shardingjdbc.resp.SqlResp;
import sample.shardingjdbc.service.SqlService;

/**
 * Created by za-daixiaojun on 2018/1/9.
 */
@RestController
@Slf4j
public class SqlApi {
    @Autowired
    private SqlService sqlService;

    @RequestMapping("/query")
    SqlResp select(@RequestBody SqlReq req) throws Exception {
        SqlResp resp = null;
        try {

            resp = sqlService.query(req.getSql(), req.getTableIndex());
            //resp.printf();
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            resp = new SqlResp();
            resp.setSuccess(false);
        }

        return resp;
    }

    @RequestMapping("/update")
    SqlResp update(@RequestBody SqlReq req) throws Exception {
        SqlResp resp = null;
        try {
            String[] sqls = req.getSql().trim().split(";");
            resp = sqlService.update(sqls);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            resp = new SqlResp();
            resp.setSuccess(false);
        }

        return resp;
    }
}
