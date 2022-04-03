package com.javademo.zookeeper.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AppClient {

    //会话超时时间
    private static final int SESSION_TIMEOUT = 200;

    //zookeeper集群地址
    private static final String SERVER_ADDRESS = "127.0.0.1:2181";

    //节点路径
    private static final String GROUP_NODE = "/sgroup";

    //zookeeper实例
    private ZooKeeper zooKeeper;

    //server列表
    private volatile List<String> serverList;

    //连接zookeeper
    private void connectZooKeeper() throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(SERVER_ADDRESS, SESSION_TIMEOUT, watchedEvent -> {
            //观察节点的变化
            if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged && watchedEvent.getPath().equals(GROUP_NODE)){
                //节点有变化，更新server列表
                try {
                    updateServerList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        updateServerList();
    }

    //更新server列表
    private void updateServerList() throws InterruptedException, KeeperException, UnsupportedEncodingException {
        List<String> tmpServerList = new ArrayList<>();
        //获取节点下的子节点
        List<String> subList = zooKeeper.getChildren(GROUP_NODE, true);
        for (String node : subList){
            System.out.printf("\nNode:%s",node);
            //获取子节点的数据存进serverList
            byte[] bytes = zooKeeper.getData(GROUP_NODE+"/"+node, false, null);
            if (bytes != null){
                String nodeDataStr = new String(bytes, "utf-8");
                tmpServerList.add(nodeDataStr);
            }
        }
        //转换成serverlist
        serverList = tmpServerList;
        System.out.println("server list updated: " + serverList);
    }

    //client工作逻辑
    private void handle() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    //main方法
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        AppClient appClient = new AppClient();
        appClient.connectZooKeeper();
        appClient.handle();
    }
}
