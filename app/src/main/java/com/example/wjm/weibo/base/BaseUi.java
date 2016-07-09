package com.example.wjm.weibo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.util.AppCache;
import com.example.wjm.weibo.util.AppUtil;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Wjm on 2016/7/4.
 */
public class BaseUi extends Activity {
    protected BaseApp app;
    protected BaseHandler handler;
    protected BaseTaskPool taskPool;
    protected boolean showLoadBar=false;
    protected boolean showDebugMsg=true;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //调试信息
        debugMemory("onCreate");
        //异步任务处理
        this.handler=new BaseHandler(this);
        //初始化任务池
        this.taskPool=new BaseTaskPool(this);
        //初始化应用
        this.app=(BaseApp)this.getApplicationContext();
        //关闭StrictMode???
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }

    public void onResume(){
        super.onResume();
        //调试信息
        debugMemory("onResume");
    }

    public void onPause(){
        super.onPause();
        debugMemory("onPause");
    }

    public void onStart(){
        super.onStart();
        debugMemory("onStart");
    }

    public void onStop(){
        super.onStop();
        debugMemory("onStop");
    }

    /////////////////////////////////////////////////////////////////////
    //工具方法
    public void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void overlay(Class<?> classObj,Bundle params){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void overlay (Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setClass(this, classObj);
        startActivity(intent);
    }

    public void forward(Class<?> classObj){
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(this,classObj);
        startActivity(intent);
        this.finish();
    }

    public void forward(Class<?> classObj,Bundle params){
        Intent intent=new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivity(intent);
        this.finish();
    }

    public Context getContext(){
        return this;
    }

    public BaseHandler getHandler(){
        return handler;
    }

    public void setHandler(BaseHandler handler){
        this.handler=handler;
    }

    public LayoutInflater getLayout(){
        return (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getLayout(int layoutId){
        return getLayout().inflate(layoutId,null);
    }

    public View getLayout(int layoutId,int itemId){
        return getLayout(layoutId).findViewById(itemId);
    }

    public BaseTaskPool getTaskPool(){
        return this.taskPool;
    }

    public void showLoadBar(){
        this.findViewById(R.id.main_load_bar).setVisibility(View.VISIBLE);
        this.findViewById(R.id.main_load_bar).bringToFront();
        showLoadBar=true;
    }

    public void hideLoadBar(){
        if(showLoadBar){
            this.findViewById(R.id.main_load_bar).setVisibility(View.GONE);
            showLoadBar=false;
        }
    }

    public void openDialog(Bundle params){
        new BaseDialog(this,params).show();
    }

    public void loadImage(final String url){
        taskPool.addTask(0, new BaseTask() {
            public void onComplete() {
                AppCache.getCachedImage(getContext(), url);
                sendMessage(BaseTask.LOAD_IMAGE);
            }
        }, 0);
    }

    //////////////////////////////////////////////////////////////////
    //逻辑方法
    public void doFinish(){
        this.finish();
    }

    public void doLogout(){
        BaseAuth.setLogin(false);
    }

    public void doEditText(){
        Intent intent=new Intent();
        intent.setAction(C.intent.action.EDITTEXT);
        this.startActivity(intent);
    }

    public void doEditText(Bundle data){
        Intent intent=new Intent();
        intent.setAction(C.intent.action.EDITTEXT);
        intent.putExtras(data);
        this.startActivity(intent);
    }

    public void doEditBlog(){
        Intent intent=new Intent();
        intent.setAction(C.intent.action.EDITBLOG);
        this.startActivity(intent);
    }

    public void doEditBlog(Bundle data){
        Intent intent=new Intent();
        intent.setAction(C.intent.action.EDITBLOG);
        intent.putExtras(data);
        startActivity(intent);
    }

    public void sendMessage(int what){
        Message msg=new Message();
        msg.what=what;
        handler.sendMessage(msg);
    }

    public void sendMessage(int what,String data){
        Bundle bundle=new Bundle();
        bundle.putString("data",data);
        Message msg=new Message();
        msg.what=what;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void sendMessage(int what,int taskId,String data){
        Bundle bundle=new Bundle();
        bundle.putInt("task", taskId);
        bundle.putString("data", data);
        Message msg=new Message();
        msg.what=what;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void doTaskAsync(int taskId,int delayTime){
        taskPool.addTask(taskId, new BaseTask() {
            public void onComplete() {
                sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null);
            }

            public void onError(String error) {
                sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
            }
        }, delayTime);
    }

    public void doTaskAsync(int taskId,BaseTask baseTask,int delayTime){
        taskPool.addTask(taskId, baseTask, delayTime);
    }

    public void doTaskAsync(int taskId,String taskUrl,HashMap<String,String> taskArgs){
        showLoadBar();
        taskPool.addTask(taskId, taskUrl, taskArgs, null, new BaseTask() {
            public void onComplete(String httpResult) {
                sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
            }

            public void onError(String error) {
                sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
            }
        }, 0);
    }

    public void doTaskAsync(final int taskId,String taskUrl,HashMap<String,String> taskArgs,
                            List<NameValuePair> taskFiles){
        showLoadBar();
        taskPool.addTask(taskId,taskUrl,taskArgs,taskFiles,new BaseTask(){
            public void onComplete(String httpResult){
                sendMessage(BaseTask.TASK_COMPLETE,this.getId(),httpResult);
            }
            public void onError(String error){
                sendMessage(BaseTask.NETWORK_ERROR,this.getId(),null);
            }
        },0);
    }

    public void onTaskComplete(int taskId,BaseMessage message){

    }

    public void onTaskComplete(int taskId){

    }

    public void onNetworkError(int taskId){

    }

    ////////////////////////////////////////////////////////////////
    //调试信息
    public void debugMemory(String tag){
        if(this.showDebugMsg){
            Log.w(this.getClass().getSimpleName(),tag+":"+ AppUtil.getUsedMemery());
        }
    }


    ////////////////////////////////////////////////////////////////
    //公共类
    public class BitmapViewBinder implements SimpleAdapter.ViewBinder{
        public boolean setViewValue(View view,Object data,String textRepresentation){
            if((view instanceof ImageView)&&(data instanceof Bitmap)){
                ImageView iv=(ImageView)view;
                Bitmap bm=(Bitmap)data;
                iv.setImageBitmap(bm);
                return true;
            }
            return false;
        }
    }

}
