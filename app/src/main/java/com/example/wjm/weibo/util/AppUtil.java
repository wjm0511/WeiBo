package com.example.wjm.weibo.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wjm.weibo.base.BaseMessage;
import com.example.wjm.weibo.base.BaseModel;
import com.example.wjm.weibo.model.Customer;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by Wjm on 2016/6/30.
 */
public class AppUtil {
    //md5 加密
    public static String md5(String str){
        MessageDigest algorithm=null;
        try{
            algorithm=MessageDigest.getInstance("MD5");

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        if(algorithm!=null){
            algorithm.reset();
            algorithm.update(str.getBytes());
            byte[] bytes=algorithm.digest();
            StringBuilder hexString=new StringBuilder();
            for(byte b:bytes){
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString();
        }
        return "";
    }

    //首字母大写
    public static String ucFirst(String str){
        if(str!=null&&str!=""){
            str=str.substring(0,1).toUpperCase()+str.substring(1);
        }
        return str;
    }

    //为EntityUtil.toString添加gzip解压功能
    public static String gzipToString(final HttpEntity entity,final String defaultChatset)
        throws IOException,ParseException{
        if(entity==null){
            throw new IllegalArgumentException("Http entity may not be null");
        }
        InputStream inStream=entity.getContent();
        if(inStream==null){
            return "";
        }
        //gzip开始
        if(entity.getContentEncoding().getValue().contains("gzip"))
            inStream=new GZIPInputStream(inStream);
        //gzip结束
        if(entity.getContentLength()>Integer.MAX_VALUE)
            throw new IllegalArgumentException("Http entity too large to be bufferd in memory");

        int i=(int)entity.getContentLength();
        if(i<0)
            i=4096;
        String charset= EntityUtils.getContentCharSet(entity);
        if(charset==null)
            charset=defaultChatset;
        if (charset==null)
            charset= HTTP.DEFAULT_CONTENT_CHARSET;
        Reader reader=new InputStreamReader(inStream,charset);
        CharArrayBuffer buffer=new CharArrayBuffer(i);
        try{
            char[] tmp=new char[1024];
            int l;
            while ((l=reader.read(tmp))!=-1)
                buffer.append(tmp,0,l);
        }finally {
            reader.close();
        }
        return buffer.toString();
    }

    //为EntityUtil.toString添加gzip解压功能
    public static String gzipToString(final HttpEntity entity) throws IOException,ParseException {
        return gzipToString(entity,null);
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("com.example.wjm.weibo.sp.global", Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(Service service){
        return service.getSharedPreferences("com.example.wjm.weibo.sp.global", Context.MODE_PRIVATE);
    }

    //////////////////////////////////////////////////////////////////
    //业务逻辑

    //获取Session Id
    public static String getSessionId(){
        Customer customer=Customer.getInstance();
        return customer.getSid();
    }

    // 获取Message
    public static BaseMessage getMessage(String jsonStr) throws Exception{
        BaseMessage message=new BaseMessage();
        JSONObject jsonObject=null;
        try{
            jsonObject=new JSONObject(jsonStr);
            if(jsonObject!=null){
                message.setCode(jsonObject.getString("code"));
                message.setMessage(jsonObject.getString("message"));
                message.setResult(jsonObject.getString("result"));
            }
        }catch (JSONException e){
            throw new Exception("Json format error");
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }

    //model数组转换为Map列表
    public static List<? extends Map<String,?>> dataToList(List<? extends BaseModel> data,String[] fields){
        ArrayList<HashMap<String,?>> list=new ArrayList<>();
        for(BaseModel item:data){
            list.add((HashMap<String,?>)dataToMap(item,fields));
        }
        return list;
    }

    public static Map<String,?> dataToMap(BaseModel data,String[] fields){
        HashMap<String,Object> map=new HashMap<>();
        try{
            for(String fieldName:fields){
                Field field=data.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                map.put(fieldName,field.get(data));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    //判断int是否为空
    public static boolean isEmptyInt(int v){
        Integer t=new Integer(v);
        return t==null?true:false;
    }

    //获取毫秒数
    public static long getTimeMillis(){
        return System.currentTimeMillis();
    }

    //获取耗费内存
    public static long getUsedMemery(){
        long total=Runtime.getRuntime().totalMemory();
        long free=Runtime.getRuntime().freeMemory();
        return total-free;
    }

    //获取并创建位图
    public static Bitmap createBitmap(String path,int width,int height){
        try{
            File f=new File(path);
            if(f.exists()==false)
                return null;
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inJustDecodeBounds=true;
            //这里很关键，inJustDecodeBounds为true时将不会为图片分配内存
            BitmapFactory.decodeFile(path,opts);
            int srcWidth=opts.outWidth; //获取蹄片原始宽度
            int srcHeight=opts.outHeight; // 获取图片原始高度
            int destWidth=0;
            int destHeight=0;
            //缩放的比例
            double ratio=0.0;
            if(srcWidth<width||srcHeight<height){
                ratio=0.0;
                destWidth=width;
                destHeight=height;
            }else if(srcWidth>srcHeight){
                ratio=(double)srcWidth/width;
                destWidth=width;
                destHeight=(int)(srcHeight/ratio);
            }else {
                ratio=(double)srcHeight/height;
                destHeight=height;
                destWidth=(int)(srcWidth/ratio);
            }

            BitmapFactory.Options newOpts=new BitmapFactory.Options();
            newOpts.inSampleSize=(int)ratio+1;
            newOpts.inJustDecodeBounds=false; //将图片读入内存
            newOpts.outHeight=destHeight;
            newOpts.outWidth=destWidth;
            //获取缩放后的图片
            return BitmapFactory.decodeFile(path,newOpts);
        }catch (Exception e){
            return null;
        }
    }
}
