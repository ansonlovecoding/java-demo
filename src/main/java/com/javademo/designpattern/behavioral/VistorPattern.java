package com.javademo.designpattern.behavioral;

public class VistorPattern {
    //访问者模式：
    //访问者模式主要将数据结构与数据操作分离，这样可以通过不同的访问者来改变元素的执行方法
    //例子：车机系统，不同访问者可以看到不同的车机数据，用访问者模式来表达

    //零件类
    public interface Component{
        void accept(Vistor vistor);
    }

    //车机类
    public static class VehicleSystem implements Component{

        @Override
        public void accept(Vistor vistor) {
            vistor.visit(this);
        }
    }

    //访问者接口
    public interface Vistor{
        void visit(VehicleSystem vehicleSystem);
    }

    //访问者1
    public static class VehicleVistor1 implements Vistor{

        @Override
        public void visit(VehicleSystem vehicleSystem) {
            System.out.println("展示数据一！");
        }
    }

    //访问者2
    public static class VehicleVistor2 implements Vistor{

        @Override
        public void visit(VehicleSystem vehicleSystem) {
            System.out.println("展示数据二！");
        }
    }

    public static void main(String[] args) {
        Vistor vistor1 = new VehicleVistor1();
        Vistor vistor2 = new VehicleVistor2();

        Component vehicleSystem = new VehicleSystem();
        vehicleSystem.accept(vistor1);
        vehicleSystem.accept(vistor2);
    }
}
