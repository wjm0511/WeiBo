package com.example.wjm.weibo.model;

import com.example.wjm.weibo.base.BaseModel;

/**
 * Created by Wjm on 2016/7/5.
 */
public class Notice extends BaseModel{
    public static final String COL_ID="id";
    public static final String COL_MESSAGE="message";

    private String id;
    private String message;

    public Notice(){}

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message=message;
    }
}
