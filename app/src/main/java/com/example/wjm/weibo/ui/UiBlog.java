package com.example.wjm.weibo.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.base.BaseHandler;
import com.example.wjm.weibo.base.BaseMessage;
import com.example.wjm.weibo.base.BaseTask;
import com.example.wjm.weibo.base.BaseUi;
import com.example.wjm.weibo.base.BaseUiAuth;
import com.example.wjm.weibo.base.C;
import com.example.wjm.weibo.list.ExpandList;
import com.example.wjm.weibo.model.Blog;
import com.example.wjm.weibo.model.Customer;
import com.example.wjm.weibo.util.AppCache;
import com.example.wjm.weibo.util.AppUtil;
import com.example.wjm.weibo.util.UIUtil;
import com.example.wjm.weibo.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wjm on 2016/7/6.
 */
public class UiBlog extends BaseUiAuth {

    private String blogId=null;
    private String customerId=null;
    private Button addFansBtn=null;
    private Button commentBtn=null;
    private ImageView faceImage=null;
    private String faceIamgeUrl=null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_blog);

        setHandler(new BlogHandler(this));

        Bundle params=this.getIntent().getExtras();
        blogId=params.getString("blogId");

        //增加粉丝
        addFansBtn=(Button)findViewById(R.id.app_blog_btn_addfans);
        addFansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //准备blog数据
                HashMap<String ,String> urlParams=new HashMap<String, String>();
                urlParams.put("customerId", customerId);
                doTaskAsync(C.task.fansAdd, C.api.fansAdd, urlParams);
            }
        });

        //增加评论
        commentBtn=(Button)findViewById(R.id.app_blog_btn_comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data=new Bundle();
                data.putInt("action",C.action.edittext.COMMENT);
                data.putString("blogId",blogId);
                doEditText(data);
            }
        });

        //准备评论数据
        HashMap<String,String> blogParams=new HashMap<>();
        blogParams.put("blogId",blogId);
        this.doTaskAsync(C.task.blogView,C.api.blogView,blogParams);
    }

    public void onStart(){
        super.onStart();
        //准备评论数据
        HashMap<String,String> commentParams=new HashMap<>();
        commentParams.put("blogId",blogId);
        commentParams.put("pageId","0");
        this.doTaskAsync(C.task.commentList, C.api.commentList, commentParams);
    }

    //异步任务回调方法
    public void onTaskComplete(int taskId,BaseMessage message){
        super.onTaskComplete(taskId,message);
        switch (taskId){
            case C.task.blogView:
                try {
                    //得到blog内容
                    Blog blog = (Blog) message.getResult("Blog");
                    TextView textUptime=(TextView)findViewById(R.id.app_blog_text_uptime);
                    TextView textContent=(TextView)findViewById(R.id.app_blog_text_content);
                    textUptime.setText(blog.getUptime());
                    textContent.setText(blog.getContent());
                    //得到blog图片
                    ImageView textPicture=(ImageView)findViewById(R.id.app_blog_text_picture);
                    String picUrl=blog.getPicture();
                    if(picUrl!=null&&picUrl.length()>0){
                        Bitmap picImage= AppCache.getCachedImage(this,picUrl);
                        if(picImage!=null){
                            textPicture.setImageBitmap(picImage);
                            textPicture.setVisibility(View.VISIBLE);
                        }
                    }
                    //得到Customer信息
                    Customer customer=(Customer)message.getResult("Customer");
                    TextView textCustomerName=(TextView)findViewById(R.id.app_blog_text_customer_name);
                    TextView textCustomerInfo=(TextView)findViewById(R.id.app_blog_text_customer_info);
                    textCustomerName.setText(customer.getName());
                    textCustomerInfo.setText(UIUtil.getCustomerInfo(this, customer));
                    //设置customer ID
                    customerId=customer.getId();
                    //异步加载头像
                    faceImage=(ImageView)findViewById(R.id.app_blog_image_face);
                    faceIamgeUrl=customer.getFaceurl();
                    loadImage(faceIamgeUrl);
                }catch (Exception e){
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;

            case C.task.commentList:
                try{
                    ArrayList<Comment> commentList=(ArrayList<Comment>)message.getResultList("Comment");
                    String[] cols={
                            Comment.COL_CONTENT,
                            Comment.COL_UPTIME
                    };
                    int[] views={
                            R.id.tpl_list_comment_content,
                            R.id.tpl_list_comment_uptime
                    };

                    int[] types={
                            ExpandList.TEXT_VIEW,
                            ExpandList.TEXT_VIEW

                    };

                    ExpandList el=new ExpandList(this, AppUtil.dataToList(commentList,cols),
                            R.layout.tpl_list_comment,cols,views,types);
                    LinearLayout layout=(LinearLayout)findViewById(R.id.app_blog_list_comment);
                    layout.removeAllViews();
                    el.render(layout);

                }catch (Exception e){
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;

            case C.task.fansAdd:
                if(message.getCode().equals("10000")){
                    toast("Add fans ok");
                    HashMap<String,String> cvParams=new HashMap<>();
                    cvParams.put("customerId",customerId);
                    this.doTaskAsync(C.task.customerView,C.api.customerView,cvParams);
                }else{
                    toast("Add fans fail");
                }
                break;

            case C.task.customerView:
                try{
                    final Customer customer=(Customer)message.getResult("Customer");
                    TextView textInfo=(TextView)findViewById(R.id.app_blog_text_customer_name);
                    textInfo.setText(UIUtil.getCustomerInfo(this, customer));
                } catch(Exception e){
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;

        }
    }

    public void onNetworkError (int taskId) {
        super.onNetworkError(taskId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // other methods

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            doFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // inner classes

    private class BlogHandler extends BaseHandler {
        public BlogHandler(BaseUi ui) {
            super(ui);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case BaseTask.LOAD_IMAGE:
                        Bitmap face = AppCache.getImage(faceIamgeUrl);
                        faceImage.setImageBitmap(face);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ui.toast(e.getMessage());
            }
        }
    }

}
