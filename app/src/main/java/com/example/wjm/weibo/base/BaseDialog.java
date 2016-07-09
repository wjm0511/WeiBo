package com.example.wjm.weibo.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wjm.weibo.R;

/**
 * Created by Wjm on 2016/7/4.
 */
public class BaseDialog {
    private TextView mTextMessage;
    private ImageView mImageImage;
    private Dialog mDialog;

    public BaseDialog(Context context,Bundle params){
        mDialog=new Dialog(context, R.style.com_example_wjm_weibo_theme_dialog);
        mDialog.setContentView(R.layout.main_dialog);
        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);

        Window window=mDialog.getWindow();
        WindowManager.LayoutParams wl=window.getAttributes();
        wl.x=0;
        wl.y=0;
        window.setAttributes(wl);

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.setLayout(200, ViewGroup.LayoutParams.WRAP_CONTENT);

        mTextMessage=(TextView)mDialog.findViewById(R.id.cs_main_dialog_text);
        mTextMessage.setTextColor(context.getResources().getColor(R.color.gray));
        mTextMessage.setText(params.getString("text"));

        mImageImage=(ImageView)mDialog.findViewById(R.id.cs_main_dialog_close);
        mImageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void show(){
        mDialog.show();
    }
}
