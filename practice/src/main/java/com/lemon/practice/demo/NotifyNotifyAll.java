package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/4/1 9:13
 * @describe  1. 三个线程，线程1和线程2首先被阻塞，
 * 线程3通过notifyAll唤醒他们。2.证明start先执行不代表线程先启动。
 */
public class NotifyNotifyAll implements Runnable{

    private static Object object = new Object();
    @Override
    public void run() {
        synchronized (object){
            System.out.println("线程"+ Thread.currentThread().getName() + "拿到锁");
            try {
                System.out.println("线程"+ Thread.currentThread().getName() + "wait to start");
                object.wait();
                System.out.println("线程"+ Thread.currentThread().getName() + "wait to end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new NotifyNotifyAll());
        Thread t2 = new Thread(new NotifyNotifyAll());
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    object.notifyAll();
                    System.out.println("线程" + Thread.currentThread().getName() + "执行了notifyAll");
                }
            }
        });
        t1.start();
        t2.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();

    }
}
