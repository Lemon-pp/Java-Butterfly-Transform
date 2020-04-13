package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/3/31 15:08
 * @describe 实现NEW、Runnable、Terminated三种状态：
 */
public class NewRunnableTerminatedTest implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        NewRunnableTerminatedTest runnable = new NewRunnableTerminatedTest();
        Thread t1 = new Thread(runnable);
        System.out.println(t1.getState());
        t1.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t1.getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t1.getState());
    }

}
