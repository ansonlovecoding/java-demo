package com.javademo.designpattern.j2ee;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObjectPattern {
    //数据访问对象模式，简称DAO模式：
    //DAO模式将数据实体和数据访问接口分离
    //例子：查询更新车辆库存，用数据访问模式表达

    //库存类
    public static class Store{
        //车辆型号
        private String model;
        //车辆库存
        private int num;

        public Store(String model, int num){
            this.model = model;
            this.num = num;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    //库存对象访问接口
    public interface StoreDao{
        List<Store> getAllStores();
        Store getStore(String name);
    }

    //实现类
    public static class StoreDaoImpl implements StoreDao{

        List<Store> stores;

        public StoreDaoImpl(){
            stores = new ArrayList<>();
            stores.add(new Store("model3", 10));
            stores.add(new Store("modelY", 30));
            stores.add(new Store("modelS", 20));
        }

        @Override
        public List<Store> getAllStores() {
            return stores;
        }

        @Override
        public Store getStore(String name) {
            for (Store tmpStore:stores
                 ) {
                if (tmpStore.model.equals(name)){
                    return tmpStore;
                }
            }
            return null;
        }

    }

    public static void main(String[] args) {
        StoreDao storeDao = new StoreDaoImpl();
        List<Store> stores = storeDao.getAllStores();
        for (Store store:stores
             ) {
            System.out.println(store.getModel()+"-库存："+store.getNum());
        }
    }
}
