package com.javademo.designpattern.behavioral;

import java.util.ArrayList;
import java.util.List;

public class MementoPattern {
    //备忘录模式：
    //备忘录模式是指在不违反封装的情况下获得对象的内部状态，从而在需要时可以将对象恢复到最初状态
    //例子：车辆空调温度调节时可以进行撤回，用备忘录模式表达

    //空调类
    public static class Aircon{

        //温度
        private double temperature;

        public Aircon(double temperature){
            this.temperature = temperature;
        }

        public double getTemperature() {
            return temperature;
        }
    }

    //备忘录
    public static class CareTaker{

        //存储空调的历史状态
        private static List<Aircon> airconList = new ArrayList<>();

        public void add(Aircon aircon){
            airconList.add(aircon);
        }

        public Aircon get(int index){
            return airconList.get(index);
        }

    }

    //车机系统
    public static class VehicleSystem{

        private double temperature;

        private CareTaker careTaker;

        private int settingTemperatureTimes;

        public VehicleSystem(double temperature){
            this.careTaker = new CareTaker();
            this.temperature = temperature;
            this.settingTemperatureTimes = 0;
            this.careTaker.add(new Aircon(temperature));
            System.out.println("初始温度："+temperature);
        }

        //调节温度
        public void setTemperature(double temperature) {
            temperature = temperature;
            settingTemperatureTimes++;
            careTaker.add(new Aircon(temperature));
            System.out.println("设置温度："+temperature);
        }

        //恢复上一个温度
        public void getLastTemperature(){
            if (settingTemperatureTimes<1){
                System.out.println("已经是最初状态！");
            }else {
                Aircon aircon = careTaker.get(settingTemperatureTimes-1);
                temperature = aircon.temperature;
                settingTemperatureTimes--;
                System.out.println("恢复到上次温度："+temperature);
            }

        }

    }

    public static void main(String[] args) {
        VehicleSystem vehicleSystem = new VehicleSystem(22.0);
        vehicleSystem.setTemperature(18.0);
        vehicleSystem.setTemperature(19.0);
        vehicleSystem.setTemperature(14.0);
        vehicleSystem.getLastTemperature();
        vehicleSystem.getLastTemperature();
        vehicleSystem.getLastTemperature();
        vehicleSystem.getLastTemperature();
        vehicleSystem.getLastTemperature();
    }
}
