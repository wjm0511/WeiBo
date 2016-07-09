package com.example.wjm.weibo.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.base.BaseHandler;
import com.example.wjm.weibo.base.BaseMessage;
import com.example.wjm.weibo.base.BaseTask;
import com.example.wjm.weibo.base.BaseUi;
import com.example.wjm.weibo.base.BaseUiAuth;
import com.example.wjm.weibo.base.C;
import com.example.wjm.weibo.list.BlogList;
import com.example.wjm.weibo.model.Blog;
import com.example.wjm.weibo.sqlite.BlogSqlite;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Wjm on 2016/7/4.
 */
public class UiIndex extends BaseUiAuth {

    private ListView blogListView;
    private BlogList blogListAdapter;
    private BlogSqlite blogSqlite;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_index);

        this.setHandler(new IndexHandler(this));

        ImageButton ib=(ImageButton)findViewById(R.id.main_tab_1);
        ib.setImageResource(R.drawable.tab_blog_2);

        //初始化数据库
        blogSqlite=new BlogSqlite(this);
    }

    public void onStart(){
        super.onStart();
        HashMap<String,String> blogParams=new HashMap<>();
        blogParams.put("typeId","0");
        blogParams.put("pageId", "0");
        this.doTaskAsync(C.task.blogList,C.api.blogList,blogParams);
    }

    ////////////////////////////////////////////////////////////////////////
    //异步任务回调方法

    public void onTaskComplete(int taskId,BaseMessage message){
        super.onTaskComplete(taskId,message);

        switch (taskId){
            case C.task.blogList:
                try{
                    final ArrayList<Blog> blogList=(ArrayList<Blog>)message.getResultList("Blog");
                    for(Blog blog:blogList){
                        loadImage(blog.getFace());
                        blogSqlite.updateBlog(blog);
                    }

                    blogListView=(ListView)findViewById(R.id.app_index_list_view);
                    blogListAdapter=new BlogList(this,blogList);
                    blogListView.setAdapter(blogListAdapter);
                    blogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle params=new Bundle();
                            params.putString("blogId",blogList.get(position).getId());
                            overlay(UiBlog.class,params);
                        }
                    });


                }catch (Exception e){
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;
        }
    }

    public void onNetworkError(int taskId){
        super.onNetworkError(taskId);
        toast(C.err.network);
        switch (taskId){
            case C.task.blogList:
                try{
                    final ArrayList<Blog> blogList=blogSqlite.getAllBlogs();
                    for(Blog blog:blogList){
                        loadImage(blog.getFace());
                        blogSqlite.updateBlog(blog);
                    }

                    blogListView=(ListView)findViewById(R.id.app_index_list_view);
                    blogListAdapter=new BlogList(this,blogList);
                    blogListView.setAdapter(blogListAdapter);
                    blogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle params=new Bundle();
                            params.putString("blogId",blogList.get(position).getId());
                            overlay(UiBlog.class,params);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    //其他方法
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            doFinish();
        return super.onKeyDown(keyCode,event);
    }


    //////////////////////////////////////////////////////////////////////////////
    //内部类
    private class IndexHandler extends BaseHandler{
        public IndexHandler(BaseUi ui){
            super(ui);
        }

        public void handleMessage(Message msg){
            super.handleMessage(msg);
            try{
                switch (msg.what){
                    case BaseTask.LOAD_IMAGE:
                        blogListAdapter.notifyDataSetChanged();
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
                toast(e.getMessage());
            }
        }
    }

}
