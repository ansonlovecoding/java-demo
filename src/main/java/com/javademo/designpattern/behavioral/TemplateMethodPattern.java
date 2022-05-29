package com.javademo.designpattern.behavioral;

import java.util.ArrayList;
import java.util.List;

public class TemplateMethodPattern {
    //模板方法模式：
    //模板方法模式是指定义一个算法/策略框架，而将具体的算法和策略的内容放在子类实现
    //例子：经销商跟工厂拿车会经常变更需求，将需求做成模板，可以用策略模式表达

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

    //策略模板
    public abstract static class StrategyTemplate{
        //匹配车辆
        public abstract boolean matchVehicle(Vehicle vehicle);

        //筛选车辆
        public List<Vehicle> filter(List<Vehicle> vehicleList){
            List<Vehicle> filterList = new ArrayList<>(vehicleList.size());
            for (Vehicle vehicle:vehicleList
            ) {
                if (matchVehicle(vehicle)){
                    filterList.add(vehicle);
                }
            }
            return filterList;
        }
    }

    //策略A，筛选model3 红色
    public static class StrategyA extends StrategyTemplate {

        @Override
        public boolean matchVehicle(Vehicle vehicle) {
            return  (vehicle.getModelName().equals("model3") && vehicle.getColor().equals("red"));
        }
    }

    //策略B，筛选modelY 蓝色
    public static class StrategyB extends StrategyTemplate {

        @Override
        public boolean matchVehicle(Vehicle vehicle) {
            return  (vehicle.getModelName().equals("modelY") && vehicle.getColor().equals("blue"));
        }
    }

    public static void main(String[] args) {
        List<Vehicle> vehicleList = new ArrayList<>(5);
        vehicleList.add(new Vehicle("model3", "red"));
        vehicleList.add(new Vehicle("model3", "white"));
        vehicleList.add(new Vehicle("modelY", "red"));
        vehicleList.add(new Vehicle("modelY", "blue"));
        vehicleList.add(new Vehicle("modelS", "red"));
        StrategyTemplate model3RedStrategy = new StrategyA();
        List<Vehicle> model3RedList = model3RedStrategy.filter(vehicleList);
        StrategyTemplate modelYBlueStrategy = new StrategyB();
        List<Vehicle> modelYBlueList = modelYBlueStrategy.filter(vehicleList);
    }
}
