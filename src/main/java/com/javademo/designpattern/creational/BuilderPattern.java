package com.javademo.designpattern.creational;

import java.util.ArrayList;
import java.util.List;

public class BuilderPattern {
    //建造者
    //建造者模式，又称生成器模式，是指封装一个对象的构造过程，使用多个简单的对象一步一步构造一个复杂的对象，其也可以允许按步骤构造
    //例子：构造一辆特斯拉汽车，其需要车架，玻璃，内饰组成，使用建造者模式来表示

    //抽象类-零件
    public static abstract class Component{
        abstract public void info();
    }

    //车架
    public static class Frame extends Component{

        @Override
        public void info() {
            System.out.println("我是车架");
        }
    }

    //玻璃
    public static class Glass extends Component{

        @Override
        public void info() {
            System.out.println("我是玻璃");
        }
    }

    //内饰
    public static class Interior extends Component{

        @Override
        public void info() {
            System.out.println("我是内饰");
        }
    }

    //车辆
    public static class Vehicle{

        //组件列表
        private List<Component> components;

        //构造函数
        public Vehicle(){
            this.components = new ArrayList<>();
        }

        //添加组件
        public void addComponent(Component component){
            this.components.add(component);
        }

        //输出组件
        public void showComponent(){
            for (Component component:this.components
                 ) {
                component.info();
            }
        }

    }

    //生成器
    public static class VehicleBuilder{

        public Vehicle buildVehicle(){
            Vehicle vehicle = new Vehicle();
            vehicle.addComponent(new Frame());
            vehicle.addComponent(new Glass());
            vehicle.addComponent(new Interior());
            return vehicle;
        }
    }

    public static void main(String[] args) {
        VehicleBuilder builder = new VehicleBuilder();
        Vehicle vehicle = builder.buildVehicle();
        vehicle.showComponent();
    }
}
