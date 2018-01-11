package sample.shardingjdbc.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Created by za-daixiaojun on 2018/1/11.
 */
public class SqlLogAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getMessage().startsWith("SQLStatement:")) {
            return;
        }
        Context.SQL_EXECUTE_LIST.get().add(event.getFormattedMessage());
    }
}
