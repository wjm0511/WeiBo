package com.example.wjm.weibo.list;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.base.BaseList;
import com.example.wjm.weibo.base.BaseUi;
import com.example.wjm.weibo.model.Blog;
import com.example.wjm.weibo.util.AppCache;
import com.example.wjm.weibo.util.AppFilter;

import java.util.ArrayList;

/**
 * Created by Wjm on 2016/7/6.
 */
public class BlogList extends BaseList {
    private BaseUi ui;
    private LayoutInflater inflater;
    private ArrayList<Blog> blogList;

    public final class BlogListItem{
        public ImageView face;
        public TextView content;
        public TextView uptime;
        public TextView comment;
        public ImageView picture;
    }

    public BlogList(BaseUi ui,ArrayList<Blog> blogList){
        this.ui=ui;
        this.inflater=LayoutInflater.from(this.ui);
        this.blogList=blogList;
    }

    public int getCount(){
        return blogList.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position,View view,ViewGroup parent){
        BlogListItem blogItem=null;
        if(view==null){
            view=inflater.inflate(R.layout.tpl_list_blog,null);
            blogItem=new BlogListItem();
            blogItem.face=(ImageView)view.findViewById(R.id.tpl_list_blog_image_face);
            blogItem.content=(TextView)view.findViewById(R.id.tpl_list_blog_text_content);
            blogItem.picture=(ImageView)view.findViewById(R.id.tpl_list_blog_text_picture);
            blogItem.uptime=(TextView)view.findViewById(R.id.tpl_list_blog_text_uptime);
            blogItem.comment=(TextView)view.findViewById(R.id.tpl_list_blog_text_comment);
            view.setTag(blogItem);
        }else {
            blogItem=(BlogListItem)view.getTag();
        }

        //填充数据
        blogItem.uptime.setText(blogList.get(position).getUptime());

        //填充HTML数据
        blogItem.content.setText(AppFilter.getHtml(blogList.get(position).getContent()));
        blogItem.comment.setText(AppFilter.getHtml(blogList.get(position).getComment()));

        //加载头像
        String faceUrl=blogList.get(position).getFace();
        if(faceUrl!=null&&faceUrl.length()>0){
            Bitmap faceImage= AppCache.getImage(faceUrl);
            if(faceImage!=null)
                blogItem.face.setImageBitmap(faceImage);
        }else {
            blogItem.face.setImageBitmap(null);
        }

        //加载微博图片
        String picUrl=blogList.get(position).getPicture();
        if(picUrl!=null&&picUrl.length()>0){
            Bitmap picImage=AppCache.getCachedImage(ui.getContext(),picUrl);
            if(picImage!=null){
                blogItem.picture.setImageBitmap(picImage);
                blogItem.picture.setVisibility(View.VISIBLE);
            }
            else{
                blogItem.picture.setImageBitmap(null);
                blogItem.picture.setVisibility(View.GONE);
            }
        }

        return view;
    }
}
