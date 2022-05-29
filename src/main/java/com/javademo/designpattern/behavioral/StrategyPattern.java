package com.javademo.designpattern.behavioral;

import java.util.ArrayList;
import java.util.List;

public class StrategyPattern {
    //策略模式：
    //策略模式定义了一系列的算法/策略进行封装，使算法/策略和客户端相互独立，可以随时替换使用的算法
    //例子：经销商跟工厂拿车会经常变更需求，可以用策略模式表达

    //汽车类
    public static class Vehicle{
        //车型
        private String modelName;
        //颜色
        private String color;

        public Vehicle(String modelName, String color){
            this.modelName = modelName;
            this.color = color;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    //策略接口
    public interface Strategy{
        List<Vehicle> filter(List<Vehicle> vehicleList);
    }

    //策略A，筛选model3 红色
    public static class StrategyA implements Strategy{

        @Override
        public List<Vehicle> filter(List<Vehicle> vehicleList) {
            List<Vehicle> filterList = new ArrayList<>(vehicleList.size());
            for (Vehicle vehicle:vehicleList
                 ) {
                if (vehicle.getModelName().equals("model3") && vehicle.getColor().equals("red")){
                    filterList.add(vehicle);
                }
            }
            return filterList;
        }
    }

    //策略B，筛选modelY 蓝色
    public static class StrategyB implements Strategy{

        @Override
        public List<Vehicle> filter(List<Vehicle> vehicleList) {
            List<Vehicle> filterList = new ArrayList<>(vehicleList.size());
            for (Vehicle vehicle:vehicleList
            ) {
                if (vehicle.getModelName().equals("modelY") && vehicle.getColor().equals("blue")){
                    filterList.add(vehicle);
                }
            }
            return filterList;
        }
    }

    public static void main(String[] args) {
        List<Vehicle> vehicleList = new ArrayList<>(5);
        vehicleList.add(new Vehicle("model3", "red"));
        vehicleList.add(new Vehicle("model3", "white"));
        vehicleList.add(new Vehicle("modelY", "red"));
        vehicleList.add(new Vehicle("modelY", "blue"));
        vehicleList.add(new Vehicle("modelS", "red"));
        Strategy model3RedStrategy = new StrategyA();
        List<Vehicle> model3RedList = model3RedStrategy.filter(vehicleList);
        Strategy modelYBlueStrategy = new StrategyB();
        List<Vehicle> modelYBlueList = modelYBlueStrategy.filter(vehicleList);
    }
}
