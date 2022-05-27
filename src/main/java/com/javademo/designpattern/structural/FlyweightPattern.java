package com.javademo.designpattern.structural;

import java.util.HashMap;

public class FlyweightPattern {
    //享元模式：
    //享元模式利用共享的方式来支持大量细粒度的对象，这些对象一部分内部状态是相似的，简单来说抽取共有的内部状态为对象来达到复用对象的目的
    //例子：特斯拉工厂有model3，modelY，modelS三种车架，相同型号可以直接共用车架无需重新制作，用享元模式表达

    //车辆接口
    public interface Vehicle{
        void info(String serialNumber);
    }

    //车架
    public static class Frame implements Vehicle{

        private String model;

        public Frame(String model){
            this.model = model;
        }

        @Override
        public void info(String serialNumber) {
            System.out.println("车架类型："+this.model);
            System.out.println("车架序列号："+serialNumber);
        }
    }

    //享元类
    public static class FlyweightFactory{

        //享元模式常用Map存放已创建的对象
        private static final HashMap<String, Vehicle> frames = new HashMap<>();

        //获取车架
        public Vehicle getFrame(String model){
            if (!frames.containsKey(model)){
                Vehicle frame = new Frame(model);
                frames.put(model, frame);
            }
            return frames.get(model);
        }

    }

    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();
        Vehicle frame1 = factory.getFrame("model3");
        frame1.info("123456");
        Vehicle frame2 = factory.getFrame("model3");
        frame2.info("789012");
        if (frame1 == frame2){
            System.out.println("两者内存地址一致");
        }
    }

}
