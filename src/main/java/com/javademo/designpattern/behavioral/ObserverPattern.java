package com.javademo.designpattern.behavioral;

import java.util.ArrayList;
import java.util.List;

public class ObserverPattern {
    //观察者模式：
    //观察者模式定义对象之间的一对多依赖，当一个对象状态改变时，会通知所有依赖它的多个对象
    //例子：汽车引擎故障码会显示到车机系统和亮警告灯，用观察者模式来表达

    //观察者接口
    public interface Observer{
        void update(String noticeStr);
    }

    //车机系统
    public static class VehicleSystem implements Observer{

        @Override
        public void update(String noticeStr) {
            System.out.println("车机收到警告："+noticeStr);
        }
    }

    //提示灯
    public static class WarningLight implements Observer{
        @Override
        public void update(String noticeStr) {
            System.out.println("提示灯收到警告："+noticeStr);
        }
    }

    //主题（被观察者）接口
    public interface Subject{
        //注册观察者
        void registerObserver(Observer observer);

        //移除观察者
        void removeObserver(Observer observer);

        //通知所有观察者
        void notifyObservers(String noticeStr);
    }

    //车辆引擎
    public static class VehicleEngine implements Subject{

        private static List<Observer> observers = new ArrayList<>();

        @Override
        public void registerObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyObservers(String noticeStr) {
            for (Observer observer:observers
                 ) {
                observer.update(noticeStr);
            }
        }
    }

    public static void main(String[] args) {
        Subject subject = new VehicleEngine();
        Observer vehicleSystem = new VehicleSystem();
        Observer warningLight = new WarningLight();
        subject.registerObserver(vehicleSystem);
        subject.registerObserver(warningLight);
        subject.notifyObservers("引擎有故障！");
    }
}
