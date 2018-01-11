package sample.shardingjdbc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.shardingjdbc.resp.SqlResp;
import sample.shardingjdbc.util.Context;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by za-daixiaojun on 2018/1/9.
 */
@Slf4j
@Service
public class SqlService {
    @Autowired
    private DataSource dataSource;

    public SqlResp update(String sql) throws SQLException {
        SqlResp resp = new SqlResp();
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ) {
            int effectRows = preparedStatement.executeUpdate();
            resp.getEffectRows().add(effectRows);
        }

        resp.getExecuteSqlDetails().addAll(Context.SQL_EXECUTE_LIST.get());

        return resp;
    }

    public SqlResp update(String[] sqls) throws SQLException {
        SqlResp resp = new SqlResp();

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            for (String sql : sqls) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                int effectRows = preparedStatement.executeUpdate();
                resp.getEffectRows().add(effectRows);
            }

            conn.commit();

        } catch (Throwable t) {
            conn.rollback();
            throw t;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        resp.getExecuteSqlDetails().addAll(Context.SQL_EXECUTE_LIST.get());

        return resp;
    }

    public SqlResp query(String sql) throws SQLException {
        SqlResp resp = new SqlResp();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ) {

            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String name = resultSetMetaData.getColumnName(i);
                resp.getColumnNames().add(name);
            }

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    row.add(value.toString());
                }
                resp.getRows().add(row);

            }
            rs.close();
        }

        resp.getExecuteSqlDetails().addAll(Context.SQL_EXECUTE_LIST.get());

        return resp;
    }
}
