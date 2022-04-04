package com.javademo.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OrderCenter implements InvocationHandler {

    private static OrderCenter instance;

    private Customer customer;

    //动态代理的类名，用于后续输出类
    public String proxyName;

    public OrderCenter(){}

    public OrderCenter(Customer customer){
        this.customer = customer;
    }

    public static OrderCenter getInstance(Customer customer) {
        if (instance == null){
            instance = new OrderCenter();
        }
        if (customer != null){
            instance.customer = customer;
        }
        return instance;
    }

    //生成动态代理对象
    public static Object getProxy(Customer customer){
        OrderCenter orderCenter = getInstance(customer);
        Class<? extends Customer> clazz = customer.getClass();
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), orderCenter);
        System.out.println("生成的动态代理类名："+proxy.getClass().getName());
        orderCenter.proxyName = proxy.getClass().getName();
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是订单分配中心，");
        Object invoke = method.invoke(customer, args);
        System.out.println(args[0] + "的订单已分配至厨房！");
        return invoke;
    }
}
