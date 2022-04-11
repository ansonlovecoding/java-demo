package com.javademo.thread;

public class ThreadLifeDemo {

    //线程具有生命周期，其中包含5种状态，分别为出生状态、就绪状态、运行状态、暂停状态（包括休眠、等待和阻塞等）和死亡状态
    //僵死进程是指子进程退出时，父进程并未对其发出的SIGCHLD信号进行适当处理，导致子进程停留在僵死状态等待其父进程为其收尸，这个状态下的子进程就是僵死进程
    public static void main(String[] args) {

        //生成线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("线程执行！");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //输出线程当前状态
        Thread.State state = thread.getState();
        System.out.println(state);

        //启动线程后输出线程状态
        thread.start();
        state = thread.getState();
        System.out.println(state);

        //持续输出线程状态
        while (state != Thread.State.TERMINATED){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            state = thread.getState();
            System.out.println(state);
        }
    }
}
