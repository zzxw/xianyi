package com.tencent.wxcloudrun.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientSslUtils {
    private static final String DEFAULT_CHAR_SET = "UTF-8";
    /**
     * 默认连接超时时间 (毫秒)
     */
    private static final Integer DEFAULT_CONNECTION_TIME_OUT = 2000;
    /**
     * 默认socket超时时间 (毫秒)
     */
    private static final Integer DEFAULT_SOCKET_TIME_OUT = 3000;

    /** socketTimeOut上限 */
    private static final Integer SOCKET_TIME_OUT_UPPER_LIMIT = 10000;

    /** socketTimeOut下限 */
    private static final Integer SOCKET_TIME_OUT_LOWER_LIMIT = 1000;

    private static CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIME_OUT)
                .setConnectTimeout(DEFAULT_CONNECTION_TIME_OUT).build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler()).build();
    }

    private static CloseableHttpClient getHttpClient(Integer socketTimeOut) {
        RequestConfig requestConfig =
                RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(DEFAULT_CONNECTION_TIME_OUT)
                        .build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler()).build();
    }

    public static String doPost(String url, String requestBody) throws Exception {
        return doPost(url, requestBody, ContentType.APPLICATION_JSON);
    }

    public static String doPost(String url, String requestBody, Integer socketTimeOut) throws Exception {
        return doPost(url, requestBody, ContentType.APPLICATION_JSON, null, socketTimeOut);
    }

    public static String doPost(String url, String requestBody, ContentType contentType) throws Exception {
        return doPost(url, requestBody, contentType, null);
    }

    public static String doPost(String url, String requestBody, List<BasicHeader> headers) throws Exception {
        return doPost(url, requestBody, ContentType.APPLICATION_JSON, headers);
    }

    public static String doPost(String url, String requestBody, ContentType contentType, List<BasicHeader> headers)
            throws Exception {
        return doPost(url, requestBody, contentType, headers, getHttpClient());
    }

    public static String doPost(String url, String requestBody, ContentType contentType, List<BasicHeader> headers,
                                Integer socketTimeOut) throws Exception {
        if (socketTimeOut < SOCKET_TIME_OUT_LOWER_LIMIT || socketTimeOut > SOCKET_TIME_OUT_UPPER_LIMIT) {
            throw new Exception();
        }
        return doPost(url, requestBody, contentType, headers, getHttpClient(socketTimeOut));
    }


    /**
     * 通用Post远程服务请求
     * @param url
     * 	请求url地址
     * @param requestBody
     * 	请求体body
     * @param contentType
     * 	内容类型
     * @param headers
     * 	请求头
     * @return String 业务自行解析
     * @throws Exception
     */
    public static String doPost(String url, String requestBody, ContentType contentType, List<BasicHeader> headers,
                                CloseableHttpClient client) throws Exception {

        // 构造http方法,设置请求和传输超时时间,重试3次
        CloseableHttpResponse response = null;
        long startTime = System.currentTimeMillis();
        try {
            HttpPost post = new HttpPost(url);
            if (!CollectionUtils.isEmpty(headers)) {
                for (BasicHeader header : headers) {
                    post.setHeader(header);
                }
            }
            StringEntity entity =
                    new StringEntity(requestBody, ContentType.create(contentType.getMimeType(), DEFAULT_CHAR_SET));
            post.setEntity(entity);
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                throw new Exception();
            }
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } finally {
            releaseResourceAndLog(url, requestBody, response, startTime);
        }
    }

    /**
     * 暂时用于智慧园区业务联调方式
     * @param url 业务请求url
     * @param param 业务参数
     * @return
     * @throws Exception
     */
    public static String doPostWithUrlEncoded(String url,
                                              Map<String, String> param) throws Exception {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        long startTime = System.currentTimeMillis();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<org.apache.http.NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, DEFAULT_CHAR_SET);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                throw new Exception();
            }
            String resultString = EntityUtils.toString(response.getEntity(), DEFAULT_CHAR_SET);
            return resultString;
        } finally {
            releaseResourceAndLog(url, param == null ? null : param.toString(), response, startTime);
        }
    }

    private static void releaseResourceAndLog(String url, String request, CloseableHttpResponse response, long startTime) {
        if (null != response) {
            try {
                response.close();
                recordInterfaceLog(startTime, url, request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String doGet(String url) throws Exception {
        return doGet(url, ContentType.DEFAULT_TEXT);
    }

    public static String doGet(String url, ContentType contentType) throws Exception {
        return doGet(url, contentType, null);
    }

    public static String doGet(String url, List<BasicHeader> headers) throws Exception {
        return doGet(url, ContentType.DEFAULT_TEXT, headers);
    }

    /**
     * 通用Get远程服务请求
     * @param url
     * 	请求参数
     * @param contentType
     * 	请求参数类型
     * @param headers
     * 	请求头可以填充
     * @return String 业务自行解析数据
     * @throws Exception
     */
    public static String doGet(String url, ContentType contentType, List<BasicHeader> headers) throws Exception {
        CloseableHttpResponse response = null;
        long startTime = System.currentTimeMillis();
        try {
            CloseableHttpClient client = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            if (!CollectionUtils.isEmpty(headers)) {
                for (BasicHeader header : headers) {
                    httpGet.setHeader(header);
                }
            }
            if(contentType != null){
                httpGet.setHeader("Content-Type", contentType.getMimeType());
            }
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                throw new Exception();
            }
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } finally {
            releaseResourceAndLog(url, null, response, startTime);
        }
    }

    private static void recordInterfaceLog(long startTime, String url, String request) {
        long endTime = System.currentTimeMillis();
        long timeCost = endTime - startTime;
//        MDC.put("totalTime", String.valueOf(timeCost));
//        MDC.put("url", url);
//        MDC.put("logType", "third-platform-service");
//        log.info("HttpClientSslUtils 远程请求:{} 参数:{} 耗时:{}ms", url, request, timeCost);
    }
}
