package com.javademo.designpattern.structural;

public class AdapterPattern {
    //适配器模式：
    //适配器模式是将一个类的接口转换成客户希望的另一个接口，适配器主要起适配和转换的作用
    //例子：特斯拉工厂车间原来是生产model3的，要让它兼容生产modelS，用适配器模式表达

    //model3接口
    public interface Model3{
        void createModel3();
    }

    //modelS接口
    public interface ModelS{
        void createModelS();
    }

    //车间类
    public static class Model3WorkShop implements Model3{

        @Override
        public void createModel3() {
            System.out.println("我会生产model3");
        }
    }

    //转换器类
    public static class WorkShopAdapter implements ModelS{

        public Model3 model3;

        public WorkShopAdapter(Model3 model3){
            this.model3 = model3;
        }

        @Override
        public void createModelS() {
            this.model3.createModel3();
            System.out.println("我会生产modelS");
        }
    }

    public static void main(String[] args) {
        Model3 model3 = new Model3WorkShop();
        ModelS modelS = new WorkShopAdapter(model3);
        modelS.createModelS();
    }
}
