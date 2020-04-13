package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/3/31 17:37
 * @describe
 */
public class WaitNotifyTest {
    private static Object object = new Object();

    static class T1 extends Thread{
        @Override
        public void run() {
            synchronized (object){
                System.out.println("线程" + Thread.currentThread().getName() + "开始执行了");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "获得锁");
            }
        }
    }
    static class T2 extends Thread{
        @Override
        public void run() {
            synchronized (object){
                object.notify();
                System.out.println("线程" + Thread.currentThread().getName() + "执行了notify()");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T1 t1 = new T1();
        T2 t2 = new T2();
        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}
