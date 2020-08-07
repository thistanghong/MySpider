package org.example;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URI;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        getRedirect("https://www.bt5156.com/");

    }

    public static void getRedirect(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        HttpGet httpGet = new HttpGet(url);

        try {
            CloseableHttpResponse respone = httpClient.execute(httpGet, context);
            HttpHost host = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URI uri = URIUtils.resolve(httpGet.getURI(), host, redirectLocations);
            System.out.println("Final HTTP location: " + uri.toASCIIString());
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        finally {

        }

    }
}
