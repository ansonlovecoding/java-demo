package com.javademo.designpattern.behavioral;

import java.util.ArrayList;
import java.util.List;

public class NullObjectPattern {
    //空对象模式：
    //空对象模式是指用一个空的对象取代NULL对象，反映的是一个不做任何动作的关系
    //例子：展厅里有10台车，当车子售出时就给原来的车位放一个箱子（空对象），用空对象模式表达

    //汽车类
    public abstract static class Vehicle{
        protected String serialNumber;
        public abstract boolean isNil();
        public abstract String getSerialNumber();
    }

    //真实汽车
    public static class RealVehicle extends Vehicle{

        public RealVehicle(String serialNumber){
            this.serialNumber = serialNumber;
        }

        @Override
        public boolean isNil() {
            return false;
        }

        @Override
        public String getSerialNumber() {
            return serialNumber;
        }
    }

    //箱子
    public static class Box extends Vehicle{

        @Override
        public boolean isNil() {
            return true;
        }

        @Override
        public String getSerialNumber() {
            return "该汽车已卖出";
        }
    }

    public static void main(String[] args) {
        List<Vehicle> vehicleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Vehicle vehicle = new RealVehicle(i+"");
            vehicleList.add(vehicle);
        }
        //卖出第四部车
        vehicleList.remove(3);
        vehicleList.add(3, new Box());
        for (Vehicle vehicle:vehicleList){
            System.out.println(vehicle.getSerialNumber());
        }

    }
}
