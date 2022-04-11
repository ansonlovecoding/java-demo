package com.javademo.threadpool;

import java.util.concurrent.*;

public class ThreadPoolDemo {

    //创建核心线程数5，最大线程数10的线程池，观察线程输出了解线程池的工作原理：
//    提交一个任务到线程池中，线程池的处理流程如下：
//    1、判断线程池里的核心线程是否都在执行任务，如果不是（核心线程空闲或者还有核心线程没有被创建）则创建一个新的工作线程来执行任务。如果核心线程都在执行任务，则进入下个流程。
//    2、线程池判断工作队列是否已满，如果工作队列没有满，则将新提交的任务存储在这个工作队列里。如果工作队列满了，则进入下个流程。
//    3、判断线程池里的线程是否都处于工作状态，如果没有，则创建一个新的工作线程来执行任务。如果已经满了，则交给饱和策略来处理这个任务。
    //线程池优点：
//    1.能有效利用空闲线程；
//    2.避免过度创建新线程导致超过CPU线程数而崩溃；
    public static void main(String[] args) {

        //生成工作队列对象，声明容量，当线程超过核心线程数时会放入工作队列等待，当工作队列满了才开辟新线程
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(5);

        //生成饱和策略，饱和策略主要是声明当线程数超过最大线程数时应该怎么做
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy(); //是强制停止程序抛出异常，正常不会使用这个策略
        RejectedExecutionHandler rejectedExecutionHandler1 = new ThreadPoolExecutor.CallerRunsPolicy(); //继续等待使用线程池里的线程执行
        RejectedExecutionHandler rejectedExecutionHandler2 = new ThreadPoolExecutor.DiscardOldestPolicy(); //丢弃最旧的工作任务，以执行当前任务
        RejectedExecutionHandler rejectedExecutionHandler3 = new ThreadPoolExecutor.DiscardPolicy(); //不处理，丢弃该任务

        //生成线程池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, queue, rejectedExecutionHandler1);

        //执行线程
        int totalThreadNum = 20;
        for (int i = 0; i < totalThreadNum; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadPool.execute(thread);
            System.out.println("线程池中活跃线程数：" + threadPool.getPoolSize());
            System.out.println("工作任务队列等待执行任务数：" + queue.size());
        }

        //关闭线程池
        threadPool.shutdown();
    }
}
