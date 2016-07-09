package com.example.wjm.weibo.model;

import com.example.wjm.weibo.base.BaseModel;

/**
 * Created by Wjm on 2016/6/30.
 */
public class Customer extends BaseModel{

    public static final String COL_ID="id";
    public static final String COL_SID="sid";
    public static final String COL_NAME="name";
    public static final String COL_PASS="pass";
    public static final String COL_SIGN="sign";
    public static final String COL_FACE="face";
    public static final String COL_FACEURL="faceurl";
    public static final String COL_BLOGCOUNT="blogcount";
    public static final String COL_FANSCOUNT="fanscount";
    public static final String COL_UPTIME="uptime";

    private String id;
    private String sid;
    private String name;
    private String pass;
    private String sign;
    private String face;
    private String faceurl;
    private String blogcount;
    private String fanscount;
    private String uptime;

    //默认没有登录
    private boolean isLogin=false;

    //登录以单例模式
    private static Customer customer=null;

    public static Customer getInstance(){
        if(Customer.customer==null)
            Customer.customer=new Customer();
        return customer;
    }

    public Customer(){}

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getSid(){
        return sid;
    }

    public void setSid(String sid){
        this.sid=sid;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getPass(){
        return pass;
    }

    public void setPass(String pass){
        this.pass=pass;
    }

    public String getSign(){
        return sign;
    }

    public void setSign(String sign){
        this.sign=sign;
    }

    public String getFace(){
        return face;
    }

    public void setFace(String face){
        this.face=face;
    }

    public String getFaceurl(){
        return faceurl;
    }

    public void setFaceurl(String faceurl){
        this.faceurl=faceurl;
    }

    public String getUptime(){
        return uptime;
    }

    public void setUptime(String uptime){
        this.uptime=uptime;
    }

    public String getBlogcount(){
        return blogcount;
    }

    public void setBlogcount(String blogcount){
        this.blogcount=blogcount;
    }

    public String getFanscount(){
        return fanscount;
    }

    public void setFanscount(String fanscount){
        this.fanscount=fanscount;
    }

    public boolean getLogin(){
        return isLogin;
    }

    public void setLogin(boolean isLogin){
        this.isLogin=isLogin;
    }
}
