package org.example;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.*;

public class DownLoadFile {
    /**
     * 根据 URL 和网页类型生成需要保存的网页的文件名，去除 URL 中非文件名字符
     */
    public String getFilenameByUrl(String url, String contentType) {
        //移除http
        url = url.substring(7);
        //text/html类型
        if(contentType.indexOf("html") != -1) {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
            return url;
        }
        //如 application/pdf 类型
        else {
            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }
    /**
     * 保存网页字符数组到本地文件，filePath 为要保存文件的相对位置
     */
    private void saveToLocal(byte[] data, String filePath) {
        try {
            DataOutputStream dOut = new DataOutputStream(new FileOutputStream(new File(filePath)));
            for(int i = 0; i < data.length; i++) {
                dOut.write(data[i]);
            }
            dOut.flush();
            dOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 下载 URL 指定的网页
     */
    public String downloadFile(String url) {
        String filePath = null;
        //设置何时重新连接
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if (i >= 5) {
                    //超过最大重连接次数
                    return false;
                }
                if (e instanceof InterruptedIOException) {
                    //超时
                    return false;
                }
                if (e instanceof ConnectTimeoutException) {
                    //连接拒绝
                    return false;
                }
                if(e instanceof SSLException) {
                    //SSL 握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if(idempotent) {
                    //如果请求被认为是幂等的，重试
                    return true;
                }
                return false;
            }
        };
        //保持连接策略
        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if(keepAlive == -1) {
                    //保持连接存活5秒，如果保持存活值没有被服务器显示设置
                    keepAlive = 5000;
                }
                return keepAlive;
            }
        };
        //生成 HttpClient 对象并设置参数
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRetryHandler(myRetryHandler)
                .setKeepAliveStrategy(keepAliveStrategy)
                .build();
        //设置连接超时 5s
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        //执行 HTTP GET 请求
        try{
            HttpResponse response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.out.println("Method failed: " + response.getStatusLine());
                filePath = null;
            }
            //处理 http 相应内容
            byte[] entityToByte = EntityUtils.toByteArray(response.getEntity());
            filePath = "D:\\document\\bigdata\\all" + getFilenameByUrl(url, response.getFirstHeader("Content-Type").getValue());
            saveToLocal(entityToByte, filePath);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            httpGet.releaseConnection();
        }
        return filePath;
    }
}
