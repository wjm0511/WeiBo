package com.example.wjm.weibo.util;

import android.util.Log;

import com.example.wjm.weibo.base.C;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Wjm on 2016/6/30.
 */
public class AppClient {
    //压缩
    private final static int CS_NONE=0;
    private final static int CS_GZIP=1;

    private String apiUrl;
    private HttpParams httpParams;
    private HttpClient httpClient;
    private int timeoutConnection=10000;
    private int timeoutSocket=10000;
    private int compress=CS_NONE;

    private String charset= HTTP.UTF_8;

    public AppClient(String url,String charset,int compress){
        initClient(url);
        this.charset=charset;
        this.compress=compress;
    }

    public AppClient(String url){
        initClient(url);
    }

    private void initClient(String url){
        //初始化API URL
        this.apiUrl= C.api.base+url;
        String apiSid=AppUtil.getSessionId();
        if (apiSid!=null&&apiSid.length()>0){
            this.apiUrl+="?sid="+apiSid;
        }
        //设置client超时
        httpParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParams,timeoutSocket);
        //初始化client
        httpClient=new DefaultHttpClient(httpParams);
    }

    public void useWap(){
        HttpHost proxy=new HttpHost("10.0.0.172",80,"http");
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,proxy);
    }

    public String get() throws Exception{
        try{
            HttpGet httpGet=headFilter(new HttpGet(this.apiUrl));
            Log.w("AppClient.get.url",this.apiUrl);
            //发送get请求
            HttpResponse httpResponse=httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String httpResult=resultFilter(httpResponse.getEntity());
                Log.w("AppClient.get.result",httpResult);
                return httpResult;
            }else {
                return null;
            }
        }catch (ConnectTimeoutException e){
            throw new Exception(C.err.network);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String post(HashMap urlParams) throws Exception{
        HttpPost httpPost=headFilter(new HttpPost(this.apiUrl));
        List<NameValuePair> postParams=new ArrayList<NameValuePair>();
        //得到post参数
        Iterator it=urlParams.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            postParams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString()));
        }
        //设置字符集
        if(this.charset!=null){
            httpPost.setEntity(new UrlEncodedFormEntity(postParams,this.charset));
        }else{
            httpPost.setEntity(new UrlEncodedFormEntity(postParams));
        }

        Log.w("AppClient.post.url",this.apiUrl);
        Log.w("AppClient.post.data",postParams.toString());

        try{
            //发送post请求
            HttpResponse httpResponse=httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                String httpResult=resultFilter(httpResponse.getEntity());
                Log.w("AppClient.post.result",httpResult);
                return httpResult;
            }
        }catch (ConnectTimeoutException e){
            throw new Exception(C.err.network);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            httpPost.abort();
        }
        return null;
    }

    public String post(HashMap urlParams,List<NameValuePair> files) throws Exception {
        String httpResult = null;

        //得到post参数
        HttpPost httpPost = headFilter(new HttpPost(this.apiUrl));
        List<NameValuePair> postParams = new ArrayList<>();
        Iterator it = urlParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            postParams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
        }

        MultipartEntity mpEntity = new MultipartEntity();
        StringBody stringBody;
        FileBody fileBody;
        File targetFile;
        String filePath;
        FormBodyPart fbp;

        for (NameValuePair queryParam : postParams) {
            stringBody = new StringBody(queryParam.getValue(), Charset.forName("UTF-8"));
            fbp = new FormBodyPart(queryParam.getName(), stringBody);
            mpEntity.addPart(fbp);
        }

        for (NameValuePair param : files) {
            filePath = param.getValue();
            targetFile = new File(filePath);
            fileBody = new FileBody(targetFile, "application/octet-stream");
            fbp = new FormBodyPart(param.getName(), fileBody);
            mpEntity.addPart(fbp);
        }

        httpPost.setEntity(mpEntity);

        Log.w("AppClient.post.file.url", this.apiUrl);
        //Log.w("AppClient.post.file.data", postParams.toString());

        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpResult = EntityUtils.toString((httpResponse.getEntity()));
        } catch (ConnectTimeoutException e) {
            throw new Exception(C.err.network);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.abort();
        }

        //Log.w("AppClient.post.file.result",httpResult);

        return httpResult;
    }

    private HttpGet headFilter(HttpGet httpGet){
        switch (this.compress){
            case CS_GZIP:
                httpGet.addHeader("Accept-Encoding","gzip");
                break;
            default:
                break;
        }
        return httpGet;
    }

    private HttpPost headFilter(HttpPost httpPost){
        switch (this.compress){
            case CS_GZIP:
                httpPost.addHeader("Accept-Encoding","gzip");
                break;
            default:
                break;
        }
        return httpPost;
    }

    private String resultFilter(HttpEntity entity){
        String result=null;
        try{
            switch (this.compress){
                case CS_GZIP:
                    result=AppUtil.gzipToString(entity);
                    break;
                default:
                    result=EntityUtils.toString(entity);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
