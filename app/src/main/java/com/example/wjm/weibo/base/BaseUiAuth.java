package com.example.wjm.weibo.base;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.demo.DemoMap;
import com.example.wjm.weibo.demo.DemoWeb;
import com.example.wjm.weibo.model.Customer;
import com.example.wjm.weibo.test.TestDemo;
import com.example.wjm.weibo.ui.UiBlogs;
import com.example.wjm.weibo.ui.UiConfig;
import com.example.wjm.weibo.ui.UiIndex;
import com.example.wjm.weibo.ui.UiLogin;

/**
 * Created by Yao on 2016/7/5.
 */
public class BaseUiAuth extends BaseUi {

    private final int MENU_APP_WRITE=0;
    private final int MENU_APP_LOGOUT=1;
    private final int MENU_APP_ABOUT=2;
    private final int MENU_WEIBO_WEB=3;
    private final int MENU_WEIBO_MAP=4;
    private final int MENU_WEIBO_TEST=5;

    protected static Customer customer=null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(!BaseAuth.isLogin()) {
            this.forward(UiLogin.class);
            this.onStop();
        }else{
            customer=BaseAuth.getCustomer();
        }
    }

    public void onStart(){
        super.onStart();
        this.bindMainTab();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_APP_WRITE, 0, R.string.menu_app_write);
        menu.add(0, MENU_APP_LOGOUT, 0, R.string.menu_app_logout);
        menu.add(0, MENU_APP_ABOUT, 0, R.string.menu_app_about);
        menu.add(0, MENU_WEIBO_WEB, 0, R.string.menu_app_web);
        menu.add(0, MENU_WEIBO_MAP, 0, R.string.menu_app_map);
        menu.add(0, MENU_WEIBO_TEST, 0, R.string.menu_app_test);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case MENU_APP_WRITE:
                doEditBlog();
                break;
            case MENU_APP_LOGOUT:
                doLogout();
                forward(UiLogin.class);
                break;
            case MENU_APP_ABOUT:
                String appName=this.getString(R.string.app_name);
                String appVersion=this.getString(R.string.app_version);
                AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle(R.string.menu_app_about)
                        .setMessage(appName+" "+appVersion)
                        .setIcon(R.drawable.face)
                        .setPositiveButton(R.string.btn_cancel,null)
                        .create();
                dialog.show();
                break;
            case MENU_WEIBO_WEB:
                forward(DemoWeb.class);
                break;
            case MENU_WEIBO_MAP:
                forward(DemoMap.class);
                break;
            case MENU_WEIBO_TEST:
                forward(TestDemo.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindMainTab(){
        ImageButton bTabHome=(ImageButton)findViewById(R.id.main_tab_1);
        ImageButton bTabBlog=(ImageButton)findViewById(R.id.main_tab_2);
        ImageButton bTabConfig=(ImageButton)findViewById(R.id.main_tab_3);
        ImageButton bTabWrite=(ImageButton)findViewById(R.id.main_tab_4);
        if(bTabHome!=null&&bTabBlog!=null&&bTabConfig!=null&&bTabWrite!=null){
            View.OnClickListener onClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.main_tab_1:
                            forward(UiIndex.class);
                            break;
                        case R.id.main_tab_2:
                            forward(UiBlogs.class);
                            break;
                        case R.id.main_tab_3:
                            forward(UiConfig.class);
                            break;
                        case R.id.main_tab_4:
                            doEditBlog();
                            break;
                    }
                }
            };
            bTabHome.setOnClickListener(onClickListener);
            bTabBlog.setOnClickListener(onClickListener);
            bTabConfig.setOnClickListener(onClickListener);
            bTabWrite.setOnClickListener(onClickListener);
        }
    }
}
