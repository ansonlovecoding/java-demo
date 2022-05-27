package com.javademo.designpattern.behavioral;

public class ChainOfResponsibilityPattern {
    //责任链模式：
    //责任链模式下，多个接收者连接成链，当接收对象时链上的每个对象都有机会接收或传递给下一个接收者
    //例子：车辆生产链包括浇筑车架，喷漆，组装零件，调试，用责任链模式来表达

    //生产抽象类
    public abstract static class Procedure{

        //下一个步骤
        protected Procedure nextProcedure;

        public void setNextProcedure(Procedure nextProcedure){
            this.nextProcedure = nextProcedure;
        }

        public void startProcedure(){
            info();

            if (this.nextProcedure != null){
                this.nextProcedure.startProcedure();
            }
        }

        protected abstract void info();
    }

    //车架类
    public static class Frame extends Procedure{

        @Override
        protected void info() {
            System.out.println("制作车架！");
        }
    }

    //颜色类
    public static class Color extends Procedure{

        @Override
        protected void info() {
            System.out.println("开始喷漆！");
        }
    }

    //零件类
    public static class Component extends Procedure{

        @Override
        protected void info() {
            System.out.println("组装零件！");
        }
    }

    //测试类
    public static class QA extends Procedure{

        @Override
        protected void info() {
            System.out.println("开始调试！");
        }
    }

    public static void main(String[] args) {
        Procedure frame = new Frame();
        Procedure color = new Color();
        Procedure component = new Component();
        Procedure qa = new QA();
        frame.setNextProcedure(color);
        color.setNextProcedure(component);
        component.setNextProcedure(qa);
        frame.startProcedure();
    }
}
