package com.example.wjm.weibo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.base.BaseAuth;
import com.example.wjm.weibo.base.BaseMessage;
import com.example.wjm.weibo.base.BaseService;
import com.example.wjm.weibo.base.BaseUi;
import com.example.wjm.weibo.base.C;
import com.example.wjm.weibo.model.Customer;
import com.example.wjm.weibo.service.NoticeService;

import java.util.HashMap;

/**
 * Created by Wjm on 2016/6/30.
 */
public class UiLogin extends BaseUi {
    private EditText mEditName;
    private EditText mEditPass;
    private CheckBox mCheckBox;
    private SharedPreferences settings;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //检查是否已经登录
        if(BaseAuth.isLogin()){
            this.forward(UiIndex.class);
        }

        this.setContentView(R.layout.ui_login);

        //记住密码
        mEditName=(EditText)findViewById(R.id.app_login_edit_name);
        mEditPass=(EditText)findViewById(R.id.app_login_edit_pass);
        mCheckBox=(CheckBox)findViewById(R.id.app_login_check_remember);
        settings=this.getPreferences(Context.MODE_PRIVATE);
        if (settings.getBoolean("remember",false)){
            mCheckBox.setChecked(true);
            mEditName.setText(settings.getString("username", ""));
            mEditPass.setText(settings.getString("password",""));
        }

        //记住CheckBox
        mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=settings.edit();
                if(mCheckBox.isChecked()){
                    editor.putBoolean("remember",true);
                    editor.putString("username",mEditName.getText().toString());
                    editor.putString("password",mEditPass.getText().toString());
                }else{
                    editor.putBoolean("remember",false);
                    editor.putString("username","");
                    editor.putString("password","");
                }
                editor.commit();
            }
        });

        //登录
        OnClickListener mOnClickListener=new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.app_login_btn_submit:
                        doTaskLogin();
                        break;
                }
            }
        };

        findViewById(R.id.app_login_btn_submit).setOnClickListener(mOnClickListener);
    }

    private void doTaskLogin(){
        app.setLong(System.currentTimeMillis());
        if(mEditName.length()>0&&mEditPass.length()>0){
            HashMap<String,String> urlParams=new HashMap<>();
            urlParams.put("name",mEditName.getText().toString());
            urlParams.put("pass",mEditPass.getText().toString());
            try {
                this.doTaskAsync(C.task.login,C.api.login,urlParams);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    //异步任务回调方法
    public void onTaskComplete(int taskId,BaseMessage message){
        super.onTaskComplete(taskId, message);
        switch(taskId){
            case C.task.login:
                Customer customer=null;
                //登录逻辑
                try{
                    customer=(Customer)message.getResult("Customer");
                    //登录成功
                    if(customer.getName()!=null){
                        BaseAuth.setCustomer(customer);
                        BaseAuth.setLogin(true);
                    }
                    //登录失败
                    else{
                        BaseAuth.setCustomer(customer);
                        BaseAuth.setLogin(false);
                        toast(this.getString(R.string.msg_loginfail));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                //登录完成
                long startTime=app.getLong();
                long loginTime=System.currentTimeMillis()-startTime;
                Log.w("LoginTime",Long.toString(loginTime));

                //转向Index界面
                if(BaseAuth.isLogin()){
                    BaseService.start(this,NoticeService.class);
                    forward(UiIndex.class);
                }
                break;
        }
    }

    public void onNetworkError(int taskId){
        super.onNetworkError(taskId);
    }


    /////////////////////////////////////////////////////////////////////
    //其他方法
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            doFinish();
        return super.onKeyDown(keyCode, event);
    }


}
