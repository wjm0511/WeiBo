package com.example.wjm.weibo.model;

import com.example.wjm.weibo.base.BaseModel;

/**
 * Created by Wjm on 2016/7/6.
 */
public class Comment extends BaseModel{
    public static final String COL_ID="id";
    public static final String COL_CONTENT="content";
    public static final String COL_UPTIME="uptime";

    private String id;
    private String content;
    private String uptime;

    public Comment(){}

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }

    public String getUptime(){
        return uptime;
    }

    public void setUptime(String uptime){
        this.uptime=uptime;
    }
}
