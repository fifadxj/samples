package sample.shardingjdbc.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * logback请求拦截器
 *
 * Created by wangjinfeng on 2016/8/9.
 */
@WebFilter(urlPatterns = "/*")
@Slf4j
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            Context.SQL_EXECUTE_LIST.get().clear();
        }
    }

    @Override
    public void destroy() {
    }

}
