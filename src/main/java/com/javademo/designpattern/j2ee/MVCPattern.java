package com.javademo.designpattern.j2ee;

public class MVCPattern {
    //MVC模式：
    //MVC模式将应用程序分为模型-视图-控制器，主要用于应用程序分层开发
    //例子：车机显示天气，用MVC表达

    //天气类-模型
    public static class Wether{
        private double temperature;

        public Wether(double temperature){
            this.temperature = temperature;
        }
    }

    //车机类-视图
    public static class VehicleSystem{
        //输出温度
        public void showTemperature(double temperature){
            System.out.println("现在的温度是："+temperature);
        }
    }

    //控制器
    public static class Controller{

        private Wether wether;

        private VehicleSystem vehicleSystem;

        public void setWether(Wether wether) {
            this.wether = wether;
        }

        public Wether getWether() {
            return wether;
        }

        public void setVehicleSystem(VehicleSystem vehicleSystem) {
            this.vehicleSystem = vehicleSystem;
        }

        public VehicleSystem getVehicleSystem() {
            return vehicleSystem;
        }

        public void showTemperature(){
            vehicleSystem.showTemperature(wether.temperature);
        }
    }

    public static void main(String[] args) {
        Wether wether = new Wether(22.0);
        VehicleSystem vehicleSystem = new VehicleSystem();
        Controller controller = new Controller();
        controller.setWether(wether);
        controller.setVehicleSystem(vehicleSystem);
        controller.showTemperature();
    }
}
