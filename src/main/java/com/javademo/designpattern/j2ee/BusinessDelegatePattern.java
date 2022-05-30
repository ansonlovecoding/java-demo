package com.javademo.designpattern.j2ee;

public class BusinessDelegatePattern {
    //业务代表模式：
    //业务代表模式用于对表示层和业务层的解耦
    //例子：车行有2种汽车运输服务给客户选择，用业务代表模式表达

    //业务接口
    public interface BusinessService{
        void doProcessing();
    }

    //A运输服务
    public static class TransportA implements BusinessService{

        @Override
        public void doProcessing() {
            System.out.println("使用A运输服务");
        }
    }

    //B运输服务
    public static class TransportB implements BusinessService{

        @Override
        public void doProcessing() {
            System.out.println("使用B运输服务");
        }
    }

    //业务代表
    public static class BusinessDelegate{
        private BusinessService businessService;
        private String serviceType;

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public void doTask(){
            if (this.serviceType.equalsIgnoreCase("A")){
                businessService = new TransportA();
            }else {
                businessService = new TransportB();
            }
            businessService.doProcessing();
        }
    }

    //客户端/表示层
    public static class Client{
        private BusinessDelegate businessDelegate;

        public Client(BusinessDelegate businessDelegate){
            this.businessDelegate = businessDelegate;
        }

        public void sendVehicle(){
            businessDelegate.doTask();
        }
    }

    public static void main(String[] args) {
        BusinessDelegate delegate = new BusinessDelegate();
        delegate.setServiceType("A");
        Client client = new Client(delegate);
        client.sendVehicle();
    }
}
