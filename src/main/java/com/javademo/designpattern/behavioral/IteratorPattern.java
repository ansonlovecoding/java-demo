package com.javademo.designpattern.behavioral;

public class IteratorPattern {
    //迭代器模式：
    //迭代器模式提供一种方法顺序访问一个聚合对象的各个元素，而又无需暴露该对象的内部表示
    //例子：将工厂一组车辆的数据迭代输出，用迭代器模式表达

    //迭代器接口
    public interface Iterator<Item>{
        //访问下一个对象
        Item next();

        //下一个对象是否存在
        boolean hasNext();
    }

    //车辆数据迭代器
    public static class VehicleIterator<Item> implements Iterator{

        private Item[] items;
        private int position = 0;

        public VehicleIterator(Item[] items){
            this.items = items;
        }

        @Override
        public Object next() {
            return items[position++];
        }

        @Override
        public boolean hasNext() {
            return position < items.length;
        }
    }

    public static void main(String[] args) {
        String[] vehicleDatas = new String[]{"车架号：123123","车架号：231231","车架号：32312312"};
        Iterator<String> iterator = new VehicleIterator<String>(vehicleDatas);
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

}
