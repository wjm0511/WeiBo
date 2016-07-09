package com.example.wjm.weibo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Wjm on 2016/6/30.
 */
public class IOUtil {
    private static String TAG=IOUtil.class.getSimpleName();

    //从本地加载图片
    public static Bitmap getBitmapLocal(String url){
        try{
            FileInputStream fis=new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    //从网络加载图片
    public static Bitmap getBitmapRemote(Context context,String url){
        URL myFileUrl=null;
        Bitmap bitmap=null;
        try{
            Log.w(TAG,url);
            myFileUrl=new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        try{
            HttpURLConnection conn=null;
            if(HttpUtil.WAP_INT==HttpUtil.getNetType(context)){
                Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172",80));
                conn=(HttpURLConnection)myFileUrl.openConnection(proxy);
            }else
                conn=(HttpURLConnection)myFileUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setDoInput(true);
            conn.connect();
            InputStream is=conn.getInputStream();
            bitmap=BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
