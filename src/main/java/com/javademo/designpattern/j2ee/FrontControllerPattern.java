package com.javademo.designpattern.j2ee;

public class FrontControllerPattern {
    //前端控制器模式：
    //前端控制器模式是用来提供一个集中的请求处理机制，所有请求都将由一个单一的处理程序处理
    //例子：引擎故障数据通过车机控制系统，展示在不同界面，用前端控制器模式表达

    //故障灯
    public static class WarningLight{
        public void show(){
            System.out.println("故障灯亮起！");
        }
    }

    //汽车电脑
    public static class Computer{
        public void show(){
            System.out.println("汽车中控提示故障！");
        }
    }

    //分发类
    public static class Dispather{

        private WarningLight warningLight;
        private Computer computer;

        public Dispather(WarningLight warningLight, Computer computer){
            this.warningLight = warningLight;
            this.computer = computer;
        }

        public void dispatch(String type){
            if (type.equals("warninglight")){
                warningLight.show();
            }else {
                computer.show();
            }
        }
    }

    //前端控制器
    public static class FrontController{

        private Dispather dispather;

        public FrontController(Dispather dispather){
            this.dispather = dispather;
        }

        //权限判断方法
        private boolean isAuth(){
            System.out.println("权限判断通过！");
            return true;
        }

        //分发请求
        public void dispathRequest(String type){
            if (isAuth()){
                dispather.dispatch(type);
            }
        }
    }

    public static void main(String[] args) {
        WarningLight warningLight = new WarningLight();
        Computer computer = new Computer();
        Dispather dispather = new Dispather(warningLight, computer);
        FrontController frontController = new FrontController(dispather);
        frontController.dispathRequest("warninglight");
    }
}
