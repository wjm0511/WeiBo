package com.example.wjm.weibo.util;

import android.content.Context;
import android.content.res.Resources;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.model.Customer;

/**
 * Created by Yao on 2016/7/6.
 */
public class UIUtil {

    public static String getCustomerInfo(Context context,Customer customer){
        Resources resources=context.getResources();
        StringBuffer sb=new StringBuffer();
        sb.append(resources.getString(R.string.customer_blog));
        sb.append(" ");
        sb.append(customer.getBlogcount());
        sb.append(" | ");
        sb.append(resources.getString(R.string.customer_fans));
        sb.append(" ");
        sb.append(customer.getFanscount());
        return sb.toString();
    }
}
