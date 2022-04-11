package com.javademo.thread;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeDemo {

//    线程安全 - 如果线程执行过程中不会产生共享资源的冲突，则线程安全。线程不安全 - 如果有多个线程同时在操作主内存中的变量，则线程不安全
//    实现线程安全的三种方式:
//    1.互斥同步:
//    * 临界区：syncronized、ReentrantLock　　
//    * 信号量 semaphore　　
//    * 互斥量　mutex
//    2.非阻塞同步:　　
//    * CAS（Compare And Swap）
//    3.无同步方案:
//    * 可重入代码　
//    * 使用Threadlocal 类来包装共享变量，做到每个线程有自己的copy
//    * 线程本地存储

    public static void main(String[] args) throws InterruptedException {
        
        //使用syncronized，可以将线程里的synchronized注释以观察区别
        String lockName = "lock";
        Thread thread1 = new Thread(() -> {
            synchronized (lockName){
                for (int i = 0; i < 5; i++) {
                    System.out.println("李四");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            synchronized (lockName){
                for (int i = 0; i < 5; i++) {
                    System.out.println("张三");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();
        Thread.sleep(1000);

        //此处使用ReentrantLock的trylock()是尝试获取锁不会一直等待，如果使用lock()会一直等待
        final Lock lock = new ReentrantLock();
        Thread thread3 = new Thread(() -> {
            try {
                boolean pass = lock.tryLock(1, TimeUnit.SECONDS);
                if (pass){
                    try {
//                        lock.lock();
                        for (int i = 0; i < 5; i++) {
                            System.out.println("李小龙");
                            Thread.sleep(100);
                        }
                    }finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread3.start();

        Thread thread4 = new Thread(() -> {
            try {
                boolean pass = lock.tryLock(5, TimeUnit.SECONDS);
                if (pass){
                    try {
//                        lock.lock();
                        for (int i = 0; i < 5; i++) {
                            System.out.println("成龙");
                            Thread.sleep(100);
                        }
                    }finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread4.start();
        Thread.sleep(1000);

        //使用semaphore，信号量设为1表示并发数为1，起到锁的作用
        Semaphore semaphore = new Semaphore(1);
        Thread thread5 = new Thread(() -> {
            try {
                semaphore.acquire();
                for (int i = 0; i < 5; i++) {
                    System.out.println("李连杰");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        });
        thread5.start();

        Thread thread6 = new Thread(() -> {
            try {
                semaphore.acquire();
                for (int i = 0; i < 5; i++) {
                    System.out.println("甄子丹");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        });
        thread6.start();
        Thread.sleep(1000);


        //非阻塞同步CAS,通常来说一个CAS接收三个参数，数据的现值V，进行比较的值A，准备写入的值B。只有当V和A相等的时候，才会写入B。无论是否写入成功，都会返回V
        CasCounter casCounter = new CasCounter(1);
        Thread thread7 = new Thread(() -> {
            try {
                boolean casResult = casCounter.compareAndSet(1, 2);
                if (casResult){
                    for (int i = 0; i < 5; i++) {
                        System.out.println("周星驰");
                        Thread.sleep(100);
                    }
                    casCounter.set(3);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread7.start();

        Thread thread8 = new Thread(() -> {
            try {
                Thread.sleep(500); //睡眠线程等cascounter设置value为3
                boolean casResult = casCounter.compareAndSet(3, 4);
                if (casResult){
                    for (int i = 0; i < 5; i++) {
                        System.out.println("吴孟达");
                        Thread.sleep(100);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread8.start();
        Thread.sleep(1000);

        //非同步方案，使用ThreadLocal共享变量，各个线程对其设置的变量仅其线程可见
        ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);
        Thread thread9 = new Thread(() -> {
            Integer sign = threadLocal.get();
            System.out.println("线程一的threadlocal值-1："+sign);
            threadLocal.set(2);
            sign = threadLocal.get();
            System.out.println("线程一的threadlocal值-2："+sign);
        });
        thread9.start();

        Thread thread10 = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Integer sign = threadLocal.get();
            System.out.println("线程二的threadlocal值-1："+sign);
            threadLocal.set(3);
            sign = threadLocal.get();
            System.out.println("线程二的threadlocal值-2："+sign);
            threadLocal.remove();
        });
        thread10.start();
        Thread.sleep(1000);


    }
}
