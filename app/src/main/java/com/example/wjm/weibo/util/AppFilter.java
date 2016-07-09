package com.example.wjm.weibo.util;

import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.example.wjm.weibo.R;

/**
 * Created by Wjm on 2016/7/6.
 */
public class AppFilter {
    public static Spanned getHtml(String text){
        return Html.fromHtml(text);
    }

    public static void setHtml(TextView tv,String text){
        if(tv.getId()== R.id.tpl_list_blog_text_content||
                tv.getId()==R.id.tpl_list_blog_text_comment)
            tv.setText(AppFilter.getHtml(text));
        else
            tv.setText(text);
    }
}
