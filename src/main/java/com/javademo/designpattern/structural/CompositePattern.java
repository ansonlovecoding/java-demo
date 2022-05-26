package com.javademo.designpattern.structural;

import java.util.ArrayList;
import java.util.List;

public class CompositePattern {
    //组合模式：
    //组合模式是指将对象组合成树形结构来表示"整体/部分"层次关系，允许用户以相同方式处理对象和组合对象，简单来说对象内包含对象组合
    //例子：汽车包括suv，轿车，皮卡，货车，用组合模式来表达

    //汽车
    public static class Vehicle{

        //车型
        private String model;

        //组合
        private List<Vehicle> submodel;

        public Vehicle(String model){
            this.model = model;
            this.submodel = new ArrayList<>();
        }

        public void addModel(Vehicle model){
            this.submodel.add(model);
        }

        //输出信息接口
        public void info(){
            System.out.println(this.model);
            for (int i = 0; i < this.submodel.size(); i++) {
                Vehicle subVehicle = this.submodel.get(i);
                System.out.println("--"+subVehicle.model);
                for (int j = 0; j < subVehicle.submodel.size(); j++) {
                    Vehicle subVehicle2 = subVehicle.submodel.get(j);
                    System.out.println("----"+subVehicle2.model);
                }
            }
        };

    }

    public static void main(String[] args) {

        Vehicle vehicle = new Vehicle("车辆");
        Vehicle vehicle1 = new Vehicle("汽车");
        Vehicle vehicle2 = new Vehicle("摩托");
        vehicle.addModel(vehicle1);
        vehicle.addModel(vehicle2);
        Vehicle vehicle11 = new Vehicle("SUV");
        Vehicle vehicle12 = new Vehicle("轿车");
        Vehicle vehicle13 = new Vehicle("皮卡");
        Vehicle vehicle14 = new Vehicle("货车");
        vehicle1.addModel(vehicle11);
        vehicle1.addModel(vehicle12);
        vehicle1.addModel(vehicle13);
        vehicle1.addModel(vehicle14);
        Vehicle vehicle21 = new Vehicle("踏板车");
        Vehicle vehicle22 = new Vehicle("机动车");
        vehicle2.addModel(vehicle21);
        vehicle2.addModel(vehicle22);
        vehicle.info();

    }
}
