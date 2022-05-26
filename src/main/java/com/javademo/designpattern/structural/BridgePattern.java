package com.javademo.designpattern.structural;

public class BridgePattern {
    //桥接模式：
    //桥接模式是指将抽象部分与实现部分分离，通过将不同的实现类赋予抽象类实现扩展多种功能
    //例子：特斯拉工厂同一个车间生产model3，modelY，modelS三种车型，用桥接模式表达

    //车辆生产接口
    public interface Vehicle{
        void createVehicle();
    }

    //model3实现类
    public static class Model3 implements Vehicle{

        @Override
        public void createVehicle() {
            System.out.println("我能生产model3！");
        }
    }

    //modelY实现类
    public static class ModelY implements Vehicle{

        @Override
        public void createVehicle() {
            System.out.println("我能生产modelY！");
        }
    }

    //modelS实现类
    public static class ModelS implements Vehicle{

        @Override
        public void createVehicle() {
            System.out.println("我能生产modelS！");
        }
    }

    //抽象类-车间
    public static abstract class WorkShop{
        //车辆实现类对象
        protected Vehicle vehicle;

        //构造函数
        public WorkShop(Vehicle vehicle){
            this.vehicle = vehicle;
        }

        //生产汽车接口
        public abstract void createVehicle();

    }

    //上海车间
    public static class ShanghaiWorkShop extends WorkShop{

        public ShanghaiWorkShop(Vehicle vehicle) {
            super(vehicle);
        }

        @Override
        public void createVehicle() {
            this.vehicle.createVehicle();
        }
    }

    public static void main(String[] args) {
        //model3车间
        WorkShop model3WorkShop = new ShanghaiWorkShop(new Model3());
        model3WorkShop.createVehicle();

        //modelY车间
        WorkShop modelYWorkShop = new ShanghaiWorkShop(new ModelY());
        modelYWorkShop.createVehicle();

        //modelS车间
        WorkShop modelSWorkShop = new ShanghaiWorkShop(new ModelS());
        modelSWorkShop.createVehicle();
    }
}
