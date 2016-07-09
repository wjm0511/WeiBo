package com.example.wjm.weibo.model;

import com.example.wjm.weibo.base.BaseModel;

/**
 * Created by Wjm on 2016/7/6.
 */
public class Blog extends BaseModel {

    public static final String COL_ID="id";
    public static final String COL_FACE="face";
    public static final String COL_DESC="desc";
    public static final String COL_TITLE="title";
    public static final String COL_AUTHOR="author";
    public static final String COL_CONTENT="content";
    public static final String COL_COMMENT="comment";
    public static final String COL_UPTIME="uptime";
    public static final String COL_PICTURE="picture";

    private String id;
    private String face;
    private String desc;
    private String title;
    private String author;
    private String content;
    private String comment;
    private String uptime;
    private String picture;

    public Blog(){}

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getFace(){
        return face;
    }

    public void setFace(String face){
        this.face=face;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc=desc;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment=comment;
    }

    public String getUptime(){
        return uptime;
    }

    public void setUptime(String uptime){
        this.uptime=uptime;
    }

    public String getPicture(){
        return picture;
    }

    public void setPicture(String picture){
        this.picture=picture;
    }
}
