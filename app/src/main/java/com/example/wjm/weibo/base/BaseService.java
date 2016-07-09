package com.example.wjm.weibo.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.example.wjm.weibo.util.AppClient;
import com.example.wjm.weibo.util.AppUtil;
import com.example.wjm.weibo.util.HttpUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wjm on 2016/7/5.
 */
public class BaseService extends Service{
    public static final String ACTION_START=".ACTION_START";
    public static final String ACTION_STOP=".ACTION_STOP";
    public static final String ACTION_PING=".ACTION_PING";
    public static final String HTTP_TYPE=".HTTP_TYPE";

    public IBinder onBind(Intent intent){
        return null;
    }

    public void onCreate(){
        super.onCreate();
    }

    public void onStart(Intent intent,int startId){
        super.onStart(intent, startId);
    }

    public void doTaskAsync(final int taskId,final String taskUrl){
        SharedPreferences sp= AppUtil.getSharedPreferences(this);
        final int httpType=sp.getInt(HTTP_TYPE, 0);
        ExecutorService es= Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AppClient client = new AppClient(taskUrl);
                    if (httpType == HttpUtil.WAP_INT)
                        client.useWap();
                    String httpResult = client.get();
                    onTaskComplete(taskId, AppUtil.getMessage(httpResult));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void onTaskComplete(int taskId,BaseMessage message){

    }

    public static void start(Context context,Class<? extends Service> sc){
        //得到全局数据
        SharedPreferences sf=AppUtil.getSharedPreferences(context);
        SharedPreferences.Editor editor=sf.edit();
        editor.putInt(HTTP_TYPE,HttpUtil.getNetType(context));
        editor.commit();
        //开始服务
        String actionName=sc.getName()+ACTION_START;
        Intent i=new Intent(context,sc);
        i.setAction(actionName);
        context.startService(i);
    }

    public static void stop(Context context,Class<? extends Service> cs){
        String actionName=cs.getName()+ACTION_STOP;
        Intent i=new Intent(context,cs);
        i.setAction(actionName);
        context.startService(i);
    }

    public static void ping(Context context,Class<? extends Service> cs){
        String actionName=cs.getName()+ACTION_PING;
        Intent i=new Intent(context,cs);
        i.setAction(actionName);
        context.startService(i);
    }
}
