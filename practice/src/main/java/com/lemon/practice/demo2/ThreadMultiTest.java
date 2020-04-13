package com.lemon.practice.demo2;

/**
 * @author zhoup
 * @date 2020/4/2 15:35
 * @describe
 */
public class ThreadMultiTest implements Runnable {
    static ThreadMultiTest test = new ThreadMultiTest();
    int num;
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            System.out.println(Thread.currentThread().getName() + " " + num++);

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(test);
        Thread t2 = new Thread(test);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(test.num);
    }
}
