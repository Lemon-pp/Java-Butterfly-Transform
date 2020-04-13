package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/3/31 15:25
 * @describe 实现BLOCKED、Waiting、Timed_Waiting
 */
public class BlockedWaitingTimedWaitingTest implements Runnable {

    @Override
    public void run() {
        syn();
    }
    private synchronized void syn() {
        try {
            Thread.sleep(1000);
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        BlockedWaitingTimedWaitingTest runnable = new BlockedWaitingTimedWaitingTest();
        Thread t1 = new Thread(runnable);
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t2 = new Thread(runnable);
        t2.start();
        System.out.println(t1.getState());
        System.out.println(t2.getState());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t1.getState());
    }
}
