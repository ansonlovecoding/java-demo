package com.javademo.designpattern.j2ee;

public class TransferObjectPattern {
    //传输对象模式：
    //传输模式用于从客户端向服务器一次性传递带有多个属性的数据，传输对象也被称为数值对象
    //例子：客户端向服务端提交一个数据对象，用传输对象模式表达

    //数据对象
    public static class Data{
        private String name;

        public Data(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    //服务类
    public static class Service{
        public void receive(Data data){
            System.out.println("收到数据："+data.getName());
        }
    }

    //客户端
    public static class Client{
        private Service service;

        public Client(){
            this.service = new Service();
        }

        public void submit(Data data){
            this.service.receive(data);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.submit(new Data("终于搞完了设计模式！"));
    }
}
