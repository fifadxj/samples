package sample.utils;

import com.google.common.base.Charsets;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {
    private static final int TIMEOUT_MILLIS = 5 * 1000;
    public static Logger accesslogger = LoggerFactory.getLogger("accessLog");
    private static CloseableHttpClient httpclient;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        //HttpHost proxy = new HttpHost("172.17.138.155", 7000);
        //DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

        CookieStore cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom()
                //.setConnectionManager(connectionManager)
                //.setRoutePlanner(routePlanner)
                .setUserAgent("auto").setRedirectStrategy(new RedirectStrategy() {

                    @Override
                    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        //
                        return false;
                    }

                    @Override
                    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                        //
                        return null;
                    }
                }).setDefaultCookieStore(cookieStore)
                .build();
        BasicClientCookie cookie = new BasicClientCookie("ares.session.id", "bd0074a0-d929-429a-8a5d-8dfcb9a2358c");
        //cookie.setVersion(0);
        cookie.setDomain("10.15.166.222");   //设置范围
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
    }

    private static void setTimeout(HttpRequestBase request) {
        //ConnectTimeout： 链接建立的超时时间；
        //SocketTimeout：响应超时时间，超过此时间不再读取响应；
        //ConnectionRequestTimeout： http clilent中从connetcion pool中获得一个connection的超时时间；
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_MILLIS)
                .setConnectTimeout(TIMEOUT_MILLIS)
                .setConnectionRequestTimeout(TIMEOUT_MILLIS)
                .build();

        request.setConfig(requestConfig);
    }

    public static CloseableHttpResponse get(String url) throws IOException {
        return get(url, null);
    }

    public static CloseableHttpResponse get(String url, Map<String, String> params) throws IOException {
        if (params != null && params.size() > 0) {
            char firstChar = '?';
            if (url.contains("?")) {
                firstChar = '&';
            }

            StringBuilder sb = new StringBuilder(url + firstChar);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey());
                sb.append('=');
                sb.append(entry.getValue());
                sb.append('&');
            }
            sb.deleteCharAt(sb.length() - 1);

            url = sb.toString();
        }

        HttpGet request = new HttpGet(url);
        setTimeout(request);

        String uuid = UUID.randomUUID().toString();
        //accesslogger.info("[" + uuid + "] " + "[request]: " + request.getRequestLine().getUri());
        CloseableHttpResponse response = httpclient.execute(request);
        //accesslogger.info("[" + uuid + "] " + "[response]: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());

        return response;
    }

    public static String getOkResponseText(String url) throws IOException {
        CloseableHttpResponse response = HttpUtils.get(url);
        try {
            HttpUtils.checkStatusOk(response);

            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, Charsets.UTF_8);

            return html;

        } finally {
            response.close();
        }
    }

    public static CloseableHttpResponse post(String url, Map<String, String> params) throws IOException {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost request = new HttpPost(url);
        setTimeout(request);
        request.setEntity(entity);

        String uuid = UUID.randomUUID().toString();
        accesslogger.info("[" + uuid + "] " + "[request]: " + request.getRequestLine().getUri() + " - " + EntityUtils.toString(entity, Charsets.UTF_8));
        CloseableHttpResponse response = httpclient.execute(request);
        accesslogger.info("[" + uuid + "] " + "[response]: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());

        return response;
    }

    public static CloseableHttpResponse post(String url, String body) throws IOException {
        StringEntity entity = new StringEntity(body, Consts.UTF_8);
        HttpPost request = new HttpPost(url);
        setTimeout(request);
        request.setEntity(entity);

        String uuid = UUID.randomUUID().toString();
        accesslogger.info("[" + uuid + "] " + "[request]: " + request.getRequestLine().getUri() + " - " + EntityUtils.toString(entity, Charsets.UTF_8));
        CloseableHttpResponse response = httpclient.execute(request);
        accesslogger.info("[" + uuid + "] " + "[response]: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());

        return response;
    }

    public static void checkStatusOk(CloseableHttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            throw new RuntimeException("Unexpected response status: " + status);
        }
    }
}
