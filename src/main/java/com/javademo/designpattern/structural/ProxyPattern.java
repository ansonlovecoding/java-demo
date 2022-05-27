package com.javademo.designpattern.structural;

public class ProxyPattern {
    //代理模式：
    //代理模式为其他对象提供一种代理以控制对这个对象的访问，从而可以在这个对象的访问时添加其他功能
    //例子：特斯拉工厂要求在生成车辆的时候检查机器的各项安全系数是否正常，正常才进行，用代理模式来表示

    //车辆接口
    public interface Vehicle{
        void createVehicle();
    }

    //车间类
    public static class WorkShop implements Vehicle{

        @Override
        public void createVehicle() {
            System.out.println("开始生产车辆！");
        }
    }

    //代理类
    public static class Proxy implements Vehicle{

        private WorkShop workShop;

        public Proxy(WorkShop workShop){
            this.workShop = workShop;
        }

        @Override
        public void createVehicle() {
            System.out.println("安全检查完毕，一切正常，可以进行后续操作！");
            this.workShop.createVehicle();
        }
    }

    public static void main(String[] args) {
        Proxy proxy = new Proxy(new WorkShop());
        proxy.createVehicle();
    }
}
