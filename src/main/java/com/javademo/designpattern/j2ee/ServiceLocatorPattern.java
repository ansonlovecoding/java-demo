package com.javademo.designpattern.j2ee;

import java.util.ArrayList;
import java.util.List;

public class ServiceLocatorPattern {
    //服务定位模式：
    //服务定位模式利用缓存技术，在收到请求时先查找缓存，有缓存的情况则直接返回缓存，没有缓存则想数据库请求
    //例子：通过服务定位器获取服务对象

    //服务接口
    public interface Service{
        String getName();
        void execute();
    }

    public static class Service1 implements Service{

        @Override
        public String getName() {
            return "Service1";
        }

        @Override
        public void execute() {
            System.out.println("运行Service1");
        }
    }

    public static class Service2 implements Service{

        @Override
        public String getName() {
            return "Service2";
        }

        @Override
        public void execute() {
            System.out.println("运行Service2");
        }
    }

    //Service初始类
    public static class InitialService{
        public Service initial(String name){
            if (name.equals("Service1")){
                return new Service1();
            }else {
                return new Service2();
            }
        }
    }

    //缓存类
    public static class Cache{

        private static List<Service> services = new ArrayList<>();

        public Service getService(String name){
            for (Service service:services
                 ) {
                if (service.getName().equals(name)){
                    return service;
                }
            }
            return null;
        }

        public void addService(Service service){
            for (Service tmpService:services
            ) {
                if (tmpService.getName().equals(service.getName())){
                    services.remove(tmpService);
                }
            }
            services.add(service);
        }

    }

    //服务定位器
    public static class ServiceLocator{

        private Cache cache;

        private InitialService initialService;

        public ServiceLocator(){
            this.cache = new Cache();
            this.initialService = new InitialService();
        }

        public Service findService(String name){
            Service service = cache.getService(name);
            if (service == null){
                service = initialService.initial(name);
            }
            return service;
        }
    }

    public static void main(String[] args) {

        ServiceLocator serviceLocator = new ServiceLocator();
        Service service = serviceLocator.findService("Service1");
        service.execute();
    }
}
