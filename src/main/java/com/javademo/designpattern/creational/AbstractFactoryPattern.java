package com.javademo.designpattern.creational;

public class AbstractFactoryPattern {
    //抽象工厂：
    //抽象工厂通过抽象类创建其他工厂，再通过一组工厂创建一组相关对象
    //例子代码：假设特斯拉的model3,modelY,modelS三种车型都是通过一个车架工厂进行分配到三个车间进行生产，再进行喷色，所以存在车型和颜色两个类
    //用抽象工厂的模式表达出来

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

    //颜色接口
    public interface Color{
        void info();
    }

    //白色类
    public static class White implements Color {
        @Override
        public void info() {
            System.out.println("我是白色！");
        }
    }

    //黑色类
    public static class Black implements Color {
        @Override
        public void info() {
            System.out.println("我是黑色！");
        }
    }

    //红色类
    public static class Red implements Color {
        @Override
        public void info() {
            System.out.println("我是红色！");
        }
    }

    //工厂类
    public static abstract class TeslaFactory{
        //抽象接口，不同车间继承该接口生产不同车辆
        abstract public Vehicle createVehicle();
        //抽象接口，不同车间继承该接口给车辆喷色
        abstract public Color createColor();
    }

    //Model3车间
    public static class Model3Factory extends TeslaFactory {

        @Override
        public Vehicle createVehicle() {
            return new Model3();
        }

        @Override
        public Color createColor() {
            return new White();
        }
    }

    //ModelY车间
    public static class ModelYFactory extends TeslaFactory {

        @Override
        public Vehicle createVehicle() {
            return new ModelY();
        }

        @Override
        public Color createColor() {
            return new Black();
        }
    }

    //ModelS车间
    public static class ModelSFactory extends TeslaFactory {

        @Override
        public Vehicle createVehicle() {
            return new ModelS();
        }

        @Override
        public Color createColor() {
            return new Red();
        }
    }

    public static void main(String[] args) {
        TeslaFactory factory = new ModelYFactory();
        Vehicle vehicle = factory.createVehicle();
        Color color = factory.createColor();
        if (vehicle != null){
            vehicle.info();
        }
        if (color != null){
            color.info();
        }
    }
}
