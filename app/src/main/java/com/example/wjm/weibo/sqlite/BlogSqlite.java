package com.example.wjm.weibo.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.example.wjm.weibo.base.BaseSqlite;
import com.example.wjm.weibo.model.Blog;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by Wjm on 2016/7/6.
 */
public class BlogSqlite extends BaseSqlite {
    public BlogSqlite(Context context){
        super(context);
    }

    protected String tableName(){
        return "blogs";
    }

    protected String[] tableColumns(){
        String[] columns={
                Blog.COL_ID,
                Blog.COL_FACE,
                Blog.COL_CONTENT,
                Blog.COL_COMMENT,
                Blog.COL_AUTHOR,
                Blog.COL_UPTIME,
                Blog.COL_PICTURE
        };
        return columns;
    }

    protected String createSql(){
        return "CREATE TABLE "+tableName()+" ("+
                Blog.COL_ID+" INTEGER PRIMARY KEY,"+
                Blog.COL_FACE+" TEXT, "+
                Blog.COL_CONTENT+" TEXT, "+
                Blog.COL_COMMENT+" TEXT, "+
                Blog.COL_AUTHOR+" TEXT, "+
                Blog.COL_UPTIME+" TEXT, "+
                Blog.COL_PICTURE+" TEXT"+
                ");";
    }

    protected String upgradeSql(){
        return "DROP TABLE IF EXISTS "+tableName();
    }

    public boolean updateBlog(Blog blog){
        ContentValues values=new ContentValues();
        values.put(Blog.COL_ID,blog.getId());
        values.put(Blog.COL_FACE,blog.getFace());
        values.put(Blog.COL_CONTENT,blog.getContent());
        values.put(Blog.COL_COMMENT,blog.getComment());
        values.put(Blog.COL_AUTHOR,blog.getAuthor());
        values.put(Blog.COL_UPTIME,blog.getUptime());
        values.put(Blog.COL_PICTURE,blog.getPicture());

        String whereSql=Blog.COL_ID+"=?";
        String[] whereParams=new String[]{blog.getId()};

        try{
            if(this.exists(whereSql,whereParams))
                this.update(values,whereSql,whereParams);
            else
                this.create(values);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  ArrayList<Blog> getAllBlogs(){
        ArrayList<Blog> blogList=new ArrayList<>();
        try{
            ArrayList<ArrayList<String>> rList=this.query(null,null);
            for(int i=0;i<rList.size();i++){
                ArrayList<String> rRow=rList.get(i);
                Blog blog=new Blog();
                blog.setId(rRow.get(0));
                blog.setFace(rRow.get(1));
                blog.setContent(rRow.get(2));
                blog.setComment(rRow.get(3));
                blog.setAuthor(rRow.get(4));
                blog.setUptime(rRow.get(5));
                blog.setPicture(rRow.get(6));
                blogList.add(blog);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return blogList;
    }
}
