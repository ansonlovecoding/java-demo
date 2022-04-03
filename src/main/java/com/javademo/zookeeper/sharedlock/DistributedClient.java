package com.javademo.zookeeper.sharedlock;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributedClient {

    //会话超时时间
    private static final int SESSION_TIMEOUT = 200;

    //zookeeper集群地址
    private static final String SERVER_ADDRESS = "127.0.0.1:2181";

    //节点路径
    private static final String GROUP_NODE = "/locks";
    private static final String SUB_NODE = "/sub";

    //zookeeper实例
    private ZooKeeper zooKeeper;
    //当前client创建的子节点，用于后面删除时触发通知
    private String createPath;
    //当前client等待的子节点，当该子节点删除时获得锁
    private String waitPath;

    //闭锁实例
    private CountDownLatch latch = new CountDownLatch(1);

    //连接zookeeper
    private void connectZooKeeper() throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(SERVER_ADDRESS, SESSION_TIMEOUT, watchedEvent -> {
            //连接成功，则闭锁放开
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                latch.countDown();
            }

            //监听等待子节点被删除则获得锁
            if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted
            && watchedEvent.getPath().equals(waitPath)){
                //执行方法
                try {
                    doSomething();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        });

        //等待连接建立
        latch.await();

        //创建子节点
        createPath = zooKeeper.create(GROUP_NODE+SUB_NODE, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        Thread.sleep(10000);

        //获取子节点，如果只有一个子节点则表示不存在等待的情况，如果多于1个节点，则获取前一个节点等待该节点释放锁
        List<String> nodeList = zooKeeper.getChildren(GROUP_NODE, false);
        if (nodeList.size() == 1){
            doSomething();
        }else {
            //截取已创建子节点的节点名称
            String nodeStr = createPath.substring((GROUP_NODE+"/").length());
            //排序取序号最小
            Collections.sort(nodeList);
            int nodeIndex = nodeList.indexOf(nodeStr);

            //根据index执行不同逻辑，但序号最小时不需等待
            if (nodeIndex == 0){
                //执行方法
                doSomething();
            }else {
                //取序号前一个为等待线程
                this.waitPath = GROUP_NODE + "/" + nodeList.get(nodeIndex - 1);
                zooKeeper.getData(this.waitPath, true, null);
            }
        }

    }

    //获得锁执行方法
    private void doSomething() throws InterruptedException, KeeperException {
        try{
            System.out.println("获得锁：%s" + createPath);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("执行完成：%s" + createPath);
            //释放锁
            zooKeeper.delete(this.createPath, -1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        DistributedClient client = new DistributedClient();
                        client.connectZooKeeper();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        Thread.sleep(Long.MAX_VALUE);
    }

}
