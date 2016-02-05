package com.mushroom.hui.biz.tools;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * TODO 通过http请求json数据的接口
 * Created by yihui on 16/2/1.
 */
public class HttpApiUtil {

    public String getHTML() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://www.baidu.com/");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);
        httpclient.close();
        System.out.println(html);
        return html;
    }
}
