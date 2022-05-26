package com.javademo.designpattern.structural;

import java.util.ArrayList;
import java.util.List;

public class FilterPattern {
    //过滤器模式：
    //过滤器模式是允许使用不同标准来过滤一组对象得出不同的结果，再以逻辑运算组合起来
    //例子：特斯拉工厂有一批汽车，需要筛选出一批红色model3送给经销商，用过滤器模式表达

    //汽车类
    public static class Vehicle{

        //型号
        private String model;

        //颜色
        private String color;

        public Vehicle(String model, String color){
            this.model = model;
            this.color = color;
        }
    }

    //过滤器接口
    public interface Criteria{
        List<Vehicle> meetCriteria(List<Vehicle> vehicles);
    }

    //型号过滤器
    public static class ModelCriteria implements Criteria{

        //要过滤的型号
        private String model;

        public ModelCriteria(String model){
            this.model = model;
        }

        @Override
        public List<Vehicle> meetCriteria(List<Vehicle> vehicles) {
            List<Vehicle> criteriaVehicles = new ArrayList<>();
            for (Vehicle vehicle:vehicles
                 ) {
                if (vehicle.model.equals(this.model)){
                    criteriaVehicles.add(vehicle);
                }
            }
            return criteriaVehicles;
        }
    }

    //颜色过滤器
    public static class ColorCriteria implements Criteria{

        //要过滤的型号
        private String color;

        public ColorCriteria(String color){
            this.color = color;
        }

        @Override
        public List<Vehicle> meetCriteria(List<Vehicle> vehicles) {
            List<Vehicle> criteriaVehicles = new ArrayList<>();
            for (Vehicle vehicle:vehicles
            ) {
                if (vehicle.color.equals(this.color)){
                    criteriaVehicles.add(vehicle);
                }
            }
            return criteriaVehicles;
        }
    }

    //组合过滤器
    public static class ColorAndModelCriteria implements Criteria{

        //型号过滤器
        private Criteria modelCriteria;

        //颜色过滤器
        private Criteria colorCriteria;

        //组合过滤器
        public ColorAndModelCriteria(Criteria modelCriteria, Criteria colorCriteria){
            this.modelCriteria = modelCriteria;
            this.colorCriteria = colorCriteria;
        }

        @Override
        public List<Vehicle> meetCriteria(List<Vehicle> vehicles) {
            List<Vehicle> modelList = modelCriteria.meetCriteria(vehicles);
            List<Vehicle> colorList = colorCriteria.meetCriteria(modelList);
            return colorList;
        }
    }

    public static void main(String[] args) {

        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle("model3", "red"));
        vehicleList.add(new Vehicle("modelY", "red"));
        vehicleList.add(new Vehicle("modelS", "red"));
        vehicleList.add(new Vehicle("model3", "blue"));

        //model3过滤器
        Criteria model3Criteria = new ModelCriteria("model3");
        //红色过滤器
        Criteria redCriteria = new ColorCriteria("red");
        //组合过滤器
        Criteria redModel3Criteria = new ColorAndModelCriteria(model3Criteria, redCriteria);
        //过滤数据
        List<Vehicle> redModel3List = redModel3Criteria.meetCriteria(vehicleList);
        System.out.println(redModel3List.size());

    }

}
