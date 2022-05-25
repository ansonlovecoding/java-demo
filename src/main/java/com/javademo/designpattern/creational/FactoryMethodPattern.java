package com.javademo.designpattern.creational;

public class FactoryMethodPattern {
    //工厂方法：
    //定义了一个创建对象的接口，但由子类决定要实例化哪个类。工厂方法和简单工厂的区别是工厂方法是把实例化操作推迟到子类的方法中
    //例子代码：假设特斯拉的model3,modelY,modelS三种车型都是通过一个工厂进行分配到三个车间进行生产，用工厂方法的模式表达出来

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

    //工厂类
    public static abstract class TeslaFactory{
        //抽象接口，不同车间继承该接口生产不同车辆
        abstract public Vehicle createVehicle();
    }

    //Model3车间
    public static class Model3Factory extends TeslaFactory{

        @Override
        public Vehicle createVehicle() {
            return new Model3();
        }
    }

    //ModelY车间
    public static class ModelYFactory extends TeslaFactory{

        @Override
        public Vehicle createVehicle() {
            return new ModelY();
        }
    }

    //ModelS车间
    public static class ModelSFactory extends TeslaFactory{

        @Override
        public Vehicle createVehicle() {
            return new ModelS();
        }
    }

    public static void main(String[] args) {
        TeslaFactory factory = new ModelYFactory();
        Vehicle vehicle = factory.createVehicle();
        if (vehicle != null){
            vehicle.info();
        }
    }
}
