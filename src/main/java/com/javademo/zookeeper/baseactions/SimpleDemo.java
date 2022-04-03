package com.javademo.zookeeper.baseactions;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class SimpleDemo {

    //会话超时时间
    private static final int SESSION_TIMEOUT = 200;

    //zookeeper集群地址
    private static final String SERVER_ADDRESS = "127.0.0.1:2181";

    //zookeeper实例
    private static ZooKeeper zooKeeper = null;

    private void createZooKeeper() throws IOException {
        zooKeeper = new ZooKeeper(SERVER_ADDRESS, SESSION_TIMEOUT, watchedEvent -> {
            System.out.println(watchedEvent.toString());
        });
    }

    //zookeeper基本操作
    private void baseActions() throws InterruptedException, KeeperException {
        //创建节点
        zooKeeper.create("/test", "hello world".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //查看节点数据
        String dataStr = new String(zooKeeper.getData("/test", false, null));
        System.out.println("节点数据："+dataStr);
        //修改节点数据
        zooKeeper.setData("/test", "Keep foolish, Keep hungry!".getBytes(), -1);
        //查看节点数据
        String dataStr2 = new String(zooKeeper.getData("/test", false, null));
        System.out.println("节点数据："+dataStr2);
        //删除节点
        zooKeeper.delete("/test", -1);
        //查看节点状态
        Stat stat = zooKeeper.exists("/test", false);
        System.out.printf("节点状态：%s", "["+stat+"]");
        //关闭节点
        zooKeeper.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        SimpleDemo simpleDemo = new SimpleDemo();
        simpleDemo.createZooKeeper();
        simpleDemo.baseActions();

    }
}
