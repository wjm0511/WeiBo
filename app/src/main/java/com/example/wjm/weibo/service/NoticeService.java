package com.example.wjm.weibo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;

import com.example.wjm.weibo.base.BaseMessage;
import com.example.wjm.weibo.base.BaseService;
import com.example.wjm.weibo.base.C;
import com.example.wjm.weibo.model.Notice;
import com.example.wjm.weibo.ui.UiBlogs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wjm on 2016/7/5.
 */
public class NoticeService extends BaseService{
    private static final int ID=1000;
    private static final String NAME=NoticeService.class.getName();

    //NotificationManager展示到达的推送消息
    NotificationManager notiManager;

    private ExecutorService execService;

    //循环的得到Notice
    private boolean runloop=true;

    public IBinder onBind(Intent intent){
        return super.onBind(intent);
    }

    public void onCreate(){
        super.onCreate();
        notiManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        execService= Executors.newSingleThreadExecutor();
    }

    public void onStart(Intent intent,int startId){
        super.onStart(intent,startId);
        if(intent.getAction().equals(NAME+BaseService.ACTION_START))
            startService();
    }

    public void onDestory(){
        runloop=false;
    }

    public void startService(){
        execService.execute(new Runnable() {
            @Override
            public void run() {
                while (runloop){
                    try{
                        //得到Notice
                        doTaskAsync(C.task.notice,C.api.notice);
                        Thread.sleep(30*1000L);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void onTaskComplete(int taskId,BaseMessage message){
        try{
            Notice notice=(Notice)message.getResult("Notice");
            showNotification(notice.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showNotification(String text){
        try{
            PendingIntent pi=PendingIntent.getActivity(this,0,new Intent(this,UiBlogs.class),0);
            Notification noti=new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("通知")
                    .setContentText(text)
                    .setContentIntent(pi)
                    .build();
            notiManager.notify(ID,noti);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
