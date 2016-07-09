package com.example.wjm.weibo.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Telephony;

/**
 * Created by Wjm on 2016/6/30.
 */
public class HttpUtil {

    public static int WAP_INT=1;
    public static int NET_INT=2;
    public static int WIFI_INT=3;
    public static int NONET_INT=4;

    public static Uri APN_URI=null;

    public static int getNetType(Context context){
        //判断是否存在网络连接
        ConnectivityManager conn=null;
        try{
            conn=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(conn==null){
            return HttpUtil.NONET_INT;
        }
        NetworkInfo info=conn.getActiveNetworkInfo();
        boolean available=info.isAvailable();
        if(!available){
            return HttpUtil.NONET_INT;
        }
        String type=info.getTypeName();
        //判断是否在用WIFI
        if(type=="WIFI"){
            return HttpUtil.WIFI_INT;
        }
        //判断是否在用wap
        /*APN_URI=Uri.parse("content://telephony/carriers/preferapn");
        Cursor uriCursor=context.getContentResolver().query(APN_URI,null,null,null,null);

        if(uriCursor!=null&&uriCursor.moveToFirst()){
            String proxy=uriCursor.getString(uriCursor.getColumnIndex("proxy"));
            String port=uriCursor.getString(uriCursor.getColumnIndex("port"));
            String apn=uriCursor.getString(uriCursor.getColumnIndex("apn"));
            if(proxy!=null&&port!=null&&apn!=null&&apn.equals("cmwap")&&port.equals("80")&&
                    (proxy.equals("10.0.0.172")||proxy.equals("010.000.000.172"))){
                return HttpUtil.WAP_INT;
            }
        }*/
        return HttpUtil.NONET_INT;
    }
}
