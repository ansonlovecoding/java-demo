package com.javademo.designpattern.creational;

public class SimpleFactoryPattern {
    //简单工厂：
    //在简单工厂模式中，我们在创建对象时不会对客户端暴露创建逻辑，并且是通过使用一个共同的接口来指向新创建的对象，简单来讲，通过同一个类来创建不同的对象
    //例子代码：假设特斯拉的model3,modelY,modelS三种车型都是通过一个工厂生产，用简单工厂的模式表达出来

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
    public static class Model3 implements Vehicle{
        @Override
        public void info() {
            System.out.println("我是model3车型！");
        }
    }

    //modelY类
    public static class ModelY implements Vehicle{
        @Override
        public void info() {
            System.out.println("我是modelY车型！");
        }
    }

    //modelS类
    public static class ModelS implements Vehicle{
        @Override
        public void info() {
            System.out.println("我是modelS车型！");
        }
    }

    //工厂类
    public static class TeslaFactory{
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
        TeslaFactory factory = new TeslaFactory();
        Vehicle vehicle = factory.createVehicle(VEHICLETYPE.MODEL3);
        if (vehicle != null){
            vehicle.info();
        }
    }
}
