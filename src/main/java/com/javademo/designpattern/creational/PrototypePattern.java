package com.javademo.designpattern.creational;

public class PrototypePattern {
    //原型模式：
    //原型模式是使用原型实例指定要创建对象的类型，通过复制这个原型来创建新对象
    //例子：假设特斯拉的车架制造都是通过一个原型浇筑成型的，用原型的模式表达出来

    //车架类，实现clone接口
    public static class Frame implements Cloneable{
        //车架号
        private int serialNumber;

        public Frame(int serialNumber){
            this.serialNumber = serialNumber;
        }

        public void info(){
            System.out.println("我的车架号是"+this.serialNumber);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    //原型类
    public static class Prototype{

        private static Frame myFrame = new Frame(123456);

        public Frame getFrame() throws CloneNotSupportedException {
            return (Frame) myFrame.clone();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Prototype prototype = new Prototype();
        Frame frame = prototype.getFrame();
        frame.info();
    }
}
