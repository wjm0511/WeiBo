package com.example.wjm.weibo.base;

import com.example.wjm.weibo.model.Customer;

/**
 * Created by Wjm on 2016/7/4.
 */
public class BaseAuth {
    public static boolean isLogin(){
        Customer customer=Customer.getInstance();
        if(customer.getLogin()==true)
            return true;
        return false;
    }

    public static void setLogin(Boolean status){
        Customer customer=Customer.getInstance();
        customer.setLogin(status);
    }

    public static void setCustomer(Customer mc){
        Customer customer=Customer.getInstance();
        customer.setId(mc.getId());
        customer.setSid(mc.getSid());
        customer.setName(mc.getName());
        customer.setSign(mc.getSign());
        customer.setFace(mc.getFace());
    }

    public static Customer getCustomer(){
        return Customer.getInstance();
    }
}
