package com.lemon.practice.demo2;

/**
 * @author zhoup
 * @date 2020/4/7 15:19
 * @describe
 */
public class TwoTreadMutiTest implements Runnable{

    static int count;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TwoTreadMutiTest test = new TwoTreadMutiTest();
        Thread t1 = new Thread(test);
        Thread t2 = new Thread(test);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }
}
