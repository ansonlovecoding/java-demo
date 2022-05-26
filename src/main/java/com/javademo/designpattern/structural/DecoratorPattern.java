package com.javademo.designpattern.structural;

public class DecoratorPattern {
    //装饰器模式：
    //装饰器模式是指动态的给一个对象添加功能，他可以作为继承的代替，跟桥接模式有一定相似，其本质还是实现与抽象的解耦
    //例子：给车辆输出信息功能添加输出天气功能，用装饰器模式表达

    //车辆接口
    public interface Vehicle{
        void info();
    }

    //音乐系统
    public static class MusicSystem implements Vehicle{

        @Override
        public void info() {
            System.out.println("现在播放周杰伦-双节棍！");
        }
    }

    //天气系统
    public static class WetherSystem implements Vehicle{

        @Override
        public void info() {
            System.out.println("今天天晴，适宜外出活动！");
        }
    }

    //装饰器
    public abstract static class VehicleDecorator implements Vehicle{
        protected Vehicle vehicle;
    }

    //车机系统
    public static class VehicleSystem extends VehicleDecorator{

        public VehicleSystem(Vehicle vehicle){
            this.vehicle = vehicle;
        }

        @Override
        public void info() {
            System.out.println("这里是特斯拉车机系统！");
            this.vehicle.info();
        }

        public void updateFuntion(Vehicle vehicle){
            this.vehicle = vehicle;
        }
    }

    public static void main(String[] args) {
        Vehicle musicSystem = new MusicSystem();
        Vehicle vehicleSystem = new VehicleSystem(musicSystem);
        vehicleSystem.info();
        Vehicle wetherSystem = new WetherSystem();
        vehicleSystem = new VehicleSystem(wetherSystem);
        vehicleSystem.info();

    }
}
