package com.javademo.zookeeper.service;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class AppServer {

    //会话超时时间
    private static final int SESSION_TIMEOUT = 200;

    //zookeeper集群地址
    private static final String SERVER_ADDRESS = "127.0.0.1:2181";

    //节点路径
    private static final String GROUP_NODE = "/sgroup";
    private static final String SUB_NODE = "/sub";

    //zookeeper实例
    private static ZooKeeper zooKeeper = null;

    //连接ZooKeeper
    private void connectZookeeper(String address) throws IOException, InterruptedException, KeeperException {
        if (zooKeeper == null){
            zooKeeper = new ZooKeeper(SERVER_ADDRESS, SESSION_TIMEOUT, watchedEvent -> {
                System.out.printf("\nEVENT:%s", watchedEvent.toString());
            });
        }

        //创建临时节点
        String createPath = zooKeeper.create(GROUP_NODE+SUB_NODE, address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.printf("\ncreate:%s", createPath);
    }

    //server工作逻辑
    private void handle() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        //在参数中指定Sercer地址
        if (args.length == 0){
            System.err.println("第一个参数不能为空，为服务器地址");
            System.exit(1);
        }

        //连接zookeepser
        AppServer appServer = new AppServer();
        appServer.connectZookeeper(args[0]);
        appServer.handle();
    }
}
