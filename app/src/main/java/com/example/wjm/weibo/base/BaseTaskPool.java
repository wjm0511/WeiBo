package com.example.wjm.weibo.base;

import android.content.Context;

import com.example.wjm.weibo.util.AppClient;
import com.example.wjm.weibo.util.HttpUtil;

import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wjm on 2016/6/30.
 */
public class BaseTaskPool {
    //定义任务线程池
    static private ExecutorService taskPool;

    private Context context;

    public BaseTaskPool(BaseUi ui){
        this.context=ui.getBaseContext();
        taskPool= Executors.newCachedThreadPool();
    }

    public void addTask(int taskId,String taskUrl,HashMap<String,String> taskArgs,
                        List<NameValuePair> taskFiles,BaseTask baseTask,int delayTime){
        baseTask.setId(taskId);
        try {
            taskPool.execute(new TaskThread(context,taskUrl,taskArgs,taskFiles,baseTask,delayTime));
        }catch (Exception e){
            taskPool.shutdown();
        }
    }

    public void addTask(int taskId,String taskUrl,BaseTask baseTask,int delayTime){
        baseTask.setId(taskId);
        try {
            taskPool.execute(new TaskThread(context,taskUrl,baseTask,delayTime));
        }catch (Exception e){
            taskPool.shutdown();
        }
    }

    public void addTask(int taskId,BaseTask baseTask,int delayTime){
        baseTask.setId(taskId);
        try {
            taskPool.execute(new TaskThread(context,baseTask,delayTime));
        }catch (Exception e){
            taskPool.shutdown();
        }
    }

    //任务线程
    private class TaskThread implements Runnable{
        private Context context;
        private String taskUrl;
        private HashMap<String,String> taskArgs;
        private List<NameValuePair> taskFiles;
        private BaseTask baseTask;
        private int delayTime=0;

        public TaskThread(Context context,String taskUrl, BaseTask baseTask,int delayTime){
            this.context=context;
            this.taskUrl=taskUrl;
            this.baseTask=baseTask;
            this.delayTime=delayTime;
        }

        public TaskThread(Context context, BaseTask baseTask,int delayTime){
            this.context=context;
            this.baseTask=baseTask;
            this.delayTime=delayTime;
        }

        public TaskThread(Context context,String taskUrl,HashMap<String,String> taskArgs,
                          List<NameValuePair> taskFiles,BaseTask baseTask,int delayTime){
            this.context=context;
            this.taskUrl=taskUrl;
            this.taskArgs=taskArgs;
            this.taskFiles=taskFiles;
            this.baseTask=baseTask;
            this.delayTime=delayTime;
        }

        public void run(){
            try{
                baseTask.onStart();
                String httpResult=null;
                //设置延迟时间
                if (this.delayTime>0){
                    Thread.sleep(this.delayTime);
                }
                try {
                    if(this.taskUrl!=null){
                        //初始化app客户端
                        AppClient client=new AppClient(this.taskUrl);
                        if(HttpUtil.WAP_INT==HttpUtil.getNetType(context)){
                            client.useWap();
                        }
                        //http get
                        if(taskArgs==null){
                            httpResult=client.get();
                        }
                        //http post
                        else{
                            if(taskFiles!=null){
                                httpResult=client.post(this.taskArgs,this.taskFiles);
                            }else{
                                httpResult=client.post(this.taskArgs);
                            }
                        }
                    }
                    if(httpResult!=null){
                        baseTask.onComplete(httpResult);
                    }else{
                        baseTask.onComplete();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    baseTask.onError(e.getMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    baseTask.onStop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
