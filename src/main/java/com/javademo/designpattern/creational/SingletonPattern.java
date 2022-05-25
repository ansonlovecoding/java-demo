package com.javademo.designpattern.creational;

public class SingletonPattern {
    //单例：
    //单例模式提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象，实际上是使用了静态对象，实例化了一次，后面统一访问该实例
    //例子代码：假设特斯拉的model3,modelY,modelS三种车型都是通过一个工厂生产，用单例模式表达出来

    //车型
    enum VEHICLETYPE {
        MODEL3,
        MODELY,
        MODELS
    }

    //车辆接口
    public interface Vehicle{
        void info();
    }

    //model3类
    public static class Model3 implements Vehicle {
        @Override
        public void info() {
            System.out.println("我是model3车型！");
        }
    }

    //modelY类
    public static class ModelY implements Vehicle {
        @Override
        public void info() {
            System.out.println("我是modelY车型！");
        }
    }

    //modelS类
    public static class ModelS implements Vehicle {
        @Override
        public void info() {
            System.out.println("我是modelS车型！");
        }
    }

    //工厂
    public static class TeslaFactory{

        //单例对象
        private static TeslaFactory instance = new TeslaFactory();

        //统一访问单例对象入口
        public static TeslaFactory getInstance() {
            return instance;
        }

        //让构造函数为隐式，不让外界调用
        private TeslaFactory(){}

        //统一制造车辆接口
        public Vehicle createVehicle(VEHICLETYPE vehicletype){
            Vehicle vehicle = null;
            if (vehicletype == VEHICLETYPE.MODEL3){
                vehicle = new Model3();
            }else if (vehicletype == VEHICLETYPE.MODELY){
                vehicle = new ModelY();
            }else if (vehicletype == VEHICLETYPE.MODELY){
                vehicle = new ModelS();
            }
            return vehicle;
        }
    }

    public static void main(String[] args) {
        TeslaFactory factory = TeslaFactory.getInstance();
        Vehicle vehicle = factory.createVehicle(VEHICLETYPE.MODEL3);
        if (vehicle != null){
            vehicle.info();
        }
    }
}
