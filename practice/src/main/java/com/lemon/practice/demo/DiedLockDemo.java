package com.lemon.practice.demo;

public class DiedLockDemo {
    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        DiedLockDemo.diedLockTest();
    }

    public static void diedLockTest() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A){
                    try {
                        Thread.currentThread().sleep(2000);//等待2000ms等线程T2锁定B
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B){
                        System.out.println(1);
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    try {
                        Thread.currentThread().sleep(2000);//等待2000ms等线程T1锁定A
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (A){
                        System.out.println(2);
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}
