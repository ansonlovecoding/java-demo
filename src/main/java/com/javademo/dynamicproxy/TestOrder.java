package com.javademo.dynamicproxy;

import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestOrder {

    public static void main(String[] args) throws IOException {
        //生成代理对象
        Customer customer = (Customer) OrderCenter.getProxy(new MacDonaldCustomer());
        //执行接口
        customer.order("汉堡");

        //将动态代理的类输出到项目当前文件夹查看
        String proxyName = OrderCenter.getInstance(null).proxyName;
        byte[] bytes = ProxyGenerator.generateProxyClass(proxyName, new Class[]{Customer.class});
        FileOutputStream outputStream = new FileOutputStream("./"+proxyName+".class");
        outputStream.write(bytes);
        outputStream.close();

        //输出内容
//        生成的动态代理类名：com.sun.proxy.$Proxy0
//        我是订单分配中心，
//        我要点：汉堡
//        汉堡的订单已分配至厨房！
    }
}
