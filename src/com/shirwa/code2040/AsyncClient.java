package com.shirwa.code2040;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    Custom HttpClient class that utilizes threads to make HTTP requests off the main thread.
 */

public class AsyncClient {

    private HttpClient mClient;

    public AsyncClient() {
        mClient = HttpClientBuilder.create().build();
    }

    public static interface Callback {
        void onResponse(String response);

        void onError(Exception e);
    }

    private String processResponse(HttpResponse response) throws Exception {
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != 200) {
            throw new Exception(status.getStatusCode() + ": " + status.getReasonPhrase());
        }
        String returnStr = EntityUtils.toString(response.getEntity());
        EntityUtils.consumeQuietly(response.getEntity());
        return returnStr;
    }

    public void get(String url, final Callback callback) {
        final HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "application/json");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = mClient.execute(get);
                    String responseStr = processResponse(response);
                    callback.onResponse(responseStr);
                } catch (Exception e) {
                    if (callback != null) callback.onError(e);
                }
            }
        }).start();
    }

    public void post(String url, Map<String, String> parameters, final Callback callback) {
        final HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");

        final List<BasicNameValuePair> httpParams = new ArrayList<BasicNameValuePair>();
        for (Map.Entry<String, String> param : parameters.entrySet())
            httpParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    post.setEntity(new UrlEncodedFormEntity(httpParams, "UTF-8"));
                    HttpResponse response = mClient.execute(post);
                    String responseStr = processResponse(response);
                    callback.onResponse(responseStr);
                } catch (Exception e) {
                    if (callback != null) callback.onError(e);
                }
            }
        }).start();
    }

    public void post(String url, final String body, final Callback callback) {
        final HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    post.setEntity(new StringEntity(body, "UTF-8"));
                    HttpResponse response = mClient.execute(post);
                    String responseStr = processResponse(response);
                    callback.onResponse(responseStr);
                } catch (Exception e) {
                    if (callback != null) callback.onError(e);
                }
            }
        }).start();
    }
}