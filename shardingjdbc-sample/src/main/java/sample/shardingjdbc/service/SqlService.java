package sample.shardingjdbc.service;

import io.shardingjdbc.core.routing.type.hint.DataNodeHintManager;
import io.shardingjdbc.core.routing.type.hint.DataNodeSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sample.shardingjdbc.resp.SqlResp;
import sample.shardingjdbc.util.Context;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by za-daixiaojun on 2018/1/9.
 */
@Slf4j
@Service
@org.springframework.cloud.context.config.annotation.RefreshScope
public class SqlService {
    @Resource(name = "shardingJdbcDataSource2")
    private DataSource dataSource;

    @Value("${totalTableCount}")
    private Integer totalTableCount;

    @Value("${totalDatabaseCount}")
    private Integer totalDatabaseCount;

    public SqlService() {
        System.out.println("===");
    }

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
            if (conn != null) {
                conn.rollback();
            }
            throw t;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        resp.getExecuteSqlDetails().addAll(Context.SQL_EXECUTE_LIST.get());

        return resp;
    }

    public SqlResp query(String sql, final Integer tableIndex) throws SQLException {
        if (tableIndex != null) {
            DataNodeHintManager.setDataNodeSelector(new DataNodeSelector() {
                @Override
                public String selectDataSourceName() {
                    Integer tableCountOfEachDataSource = totalTableCount / totalDatabaseCount;
                    Integer dataSourceIndex = tableIndex / tableCountOfEachDataSource;
                    String routedDataSourceName = "ds_" + dataSourceIndex;

                    return routedDataSourceName;
                }

                @Override
                public String selectTableName(String logicalTableName) {
                    String routedTableName = logicalTableName + "_" + tableIndex;

                    return routedTableName;
                }
            });
        }

        SqlResp resp = new SqlResp();

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

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
                    if (value == null) {
                        value = "";
                    }
                    row.add(value.toString());
                }
                resp.getRows().add(row);

            }
            rs.close();
        }  finally {
            if (conn != null) {
                conn.close();
            }
        }

        resp.getExecuteSqlDetails().addAll(Context.SQL_EXECUTE_LIST.get());

        return resp;
    }
}
