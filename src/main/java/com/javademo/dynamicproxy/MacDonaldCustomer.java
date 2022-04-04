package com.javademo.dynamicproxy;

public class MacDonaldCustomer implements Customer{

    //麦当劳顾客点餐
    //利用动态代理在麦当劳顾客点餐之后通过代理（订单分发中心）把订单发送到不同后厨
    @Override
    public void order(String goodName) {
        System.out.println("我要点："+goodName);
    }
}
