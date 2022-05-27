package com.javademo.designpattern.structural;

public class FacadePattern {
    //外观模式：
    //外观模式提供了一个统一的接口，用来访问子系统中的一系列接口，简单来说就是将子系统的一组接口打包在外观类中的接口
    //例子：车辆的一键启动，实际上其做了一系列的操作如，通电，引擎启动，车机启动等，用外观模式来表达

    //车辆类
    public static class Vehicle{

        public void poweredUp(){
            System.out.println("车辆通电！");
        }

        public void startEngine(){
            System.out.println("引擎启动！");
        }

        public void startSystem(){
            System.out.println("运行车机系统！");
        }
    }

    //外观类
    public static class Facade{
        private Vehicle vehicle = new Vehicle();

        public void oneKeyStart(){
            this.vehicle.poweredUp();
            this.vehicle.startEngine();
            this.vehicle.startSystem();
        }
    }

    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.oneKeyStart();
    }
}
