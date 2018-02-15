package sample.shardingjdbc.util;

import java.sql.Connection;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

import cn.sccfc.cat.common.CatSwitch;
import cn.sccfc.cat.common.CatUtil;
import cn.sccfc.cat.constant.CatConstants;

/**
 * <h5>对MyBatis进行拦截，添加Cat监控</h5>
 * <p>MyBatis可拦截的目标方法有以下(大致的先后顺序)：</p>
 * <ol>
 *  <li>Executor(update, query, flushStatements, commit,  rollback, getTransaction, close, isClosed)</li>
 *  <li>ParameterHandler(getParameterObject, setParameters)</li>
 *  <li>StatementHandler(prepare, parameterize, batch, update, query)</li>
 *  <li>ResultSetHandler(handleResultSets, handleOutputParameters)</li>
 * </ol>
 * @author yanjiawei
 */
@Intercepts({
        @org.apache.ibatis.plugin.Signature(type = Executor.class, method = "query", args = {
                org.apache.ibatis.mapping.MappedStatement.class, Object.class,
                org.apache.ibatis.session.RowBounds.class, org.apache.ibatis.session.ResultHandler.class,
                org.apache.ibatis.cache.CacheKey.class, BoundSql.class }),
        @org.apache.ibatis.plugin.Signature(type = Executor.class, method = "query", args = {
                org.apache.ibatis.mapping.MappedStatement.class, Object.class,
                org.apache.ibatis.session.RowBounds.class, org.apache.ibatis.session.ResultHandler.class }),
        @org.apache.ibatis.plugin.Signature(type = Executor.class, method = "update", args = {
                org.apache.ibatis.mapping.MappedStatement.class, Object.class }),
        @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.parameter.ParameterHandler.class, method = "setParameters", args = {
                java.sql.PreparedStatement.class }),
        @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "prepare", args = {
                Connection.class})
})
public class CatMyBatisPlugin implements Interceptor {

    private static final ThreadLocal<Transaction> DB_TRANSACTION = new ThreadLocal<Transaction>();
    private static final ThreadLocal<String> METHOD_NAME_LOCAL = new ThreadLocal<String>();

    public Object intercept(Invocation invocation) throws Throwable {
        if (!CatSwitch.isEnable()) {
            return invocation.proceed();
        }

        if (Executor.class.isAssignableFrom(invocation.getTarget().getClass())) {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            // 得到类名.方法名
            String[] strArr = mappedStatement.getId().split("\\.");
            String methodName = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
            METHOD_NAME_LOCAL.set(methodName);
            try {
                Object object = invocation.proceed();
                completeTx(Message.SUCCESS);
                return object;
            } catch (Throwable throwable) {
                CatUtil.logError(throwable);
                completeTx(CatConstants.DB_ERROR);
                throw throwable;
            } finally {
                DB_TRANSACTION.remove();
            }
        }

        if ((StatementHandler.class.isAssignableFrom(invocation.getTarget().getClass()))
                && ("prepare".equals(invocation.getMethod().getName()))) {
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = handler.getBoundSql();
            String sql = removeBreakingWhitespace(boundSql.getSql());
            String sqlMethod = getSqlMethod(sql);
            Connection connection = (Connection) invocation.getArgs()[0];
            String url = connection.getMetaData().getURL();
            try {
                Transaction transaction = (Transaction) DB_TRANSACTION.get();
                if (transaction == null) {
                    String methodName = METHOD_NAME_LOCAL.get();
                    if (null == methodName || "".equals(methodName)) {
                        transaction = Cat.newTransaction(CatConstants.TYPE_SQL, sqlMethod);
                    } else {
                        transaction = Cat.newTransaction(CatConstants.TYPE_SQL, methodName);
                    }
                    DB_TRANSACTION.set(transaction);
                }
                Event dbEvent = Cat.newEvent(CatConstants.TYPE_SQL_DATABASE, url);
                dbEvent.setStatus(Message.SUCCESS);
                dbEvent.complete();
                Event methodEvent = Cat.newEvent(CatConstants.TYPE_SQL_METHOD, sqlMethod);
                methodEvent.setStatus(Message.SUCCESS);
                methodEvent.complete();
                Event sqlEvent = Cat.newEvent(CatConstants.TYPE_SQL_NAME, sql);
                sqlEvent.setStatus(Message.SUCCESS);
                sqlEvent.complete();
            } catch (Throwable e) {
            }
            try {
                return invocation.proceed();
            } catch (Throwable throwable) {
                CatUtil.logError(throwable);
                completeTx(CatConstants.DB_ERROR);
                DB_TRANSACTION.remove();
                METHOD_NAME_LOCAL.remove();
                throw throwable;
            }
        }

        try {
            return invocation.proceed();
        } catch (Throwable e) {
            CatUtil.logError(e);
            completeTx(CatConstants.DB_ERROR);
            DB_TRANSACTION.remove();
            METHOD_NAME_LOCAL.remove();
            throw e;
        }

    }

    private void completeTx(String state) {
        if (DB_TRANSACTION.get() != null) {
            try {
                ((Transaction) DB_TRANSACTION.get()).setStatus(state);
                ((Transaction) DB_TRANSACTION.get()).complete();
            } catch (Throwable t) {
            }
        }
    }

    /**
     * 根据sql获取SqlMethod
     *
     * @param sql
     * @return insert delete update select
     */
    private String getSqlMethod(String sql) {
        int firstIndexOfBlank = sql.indexOf(" ");
        return sql.substring(0, firstIndexOfBlank).toLowerCase();
    }

    private String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
            builder.append(whitespaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

}
