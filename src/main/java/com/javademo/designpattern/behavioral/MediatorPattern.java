package com.javademo.designpattern.behavioral;

public class MediatorPattern {
    //中介者模式：
    //中介者模式是指用一个中介对象来封装一系列的对象交互，这样各对象不必显式调用其他对象
    //例子：车机相当于中介，用户通过操作车机控制空调和音乐，用中介者模式来表达

    //中介者抽象类
    public abstract static class Mediator{
        //根据事件命令执行不同操作
        public abstract void doEvent(String eventType);
    }

    //车机中介者
    public static class VehicleMediator extends Mediator{

        @Override
        public void doEvent(String eventType) {
            switch (eventType){
                case "aircon":
                    System.out.println("打开空调！");
                    break;
                case "music":
                    System.out.println("打开音乐！");
                    break;
                case "one key comfort":
                    System.out.println("打开空调！");
                    System.out.println("打开音乐！");
                    break;
                default:
                    break;
            }
        }
    }


    //零件接口
    public abstract static class Component{
        public abstract void onEvent(Mediator mediator, String eventType);
    }

    //车机系统
    public static class VehicleSystem extends Component{

        @Override
        public void onEvent(Mediator mediator, String eventType) {
            mediator.doEvent(eventType);
        }
    }

    public static void main(String[] args) {
        Mediator mediator = new VehicleMediator();
        VehicleSystem vehicleSystem = new VehicleSystem();
        vehicleSystem.onEvent(mediator, "one key comfort");
        vehicleSystem.onEvent(mediator, "aircon");
    }
}
