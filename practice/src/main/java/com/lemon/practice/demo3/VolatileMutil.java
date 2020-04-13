package com.lemon.practice.demo3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhoup
 * @date 2020/4/9 15:45
 * @describe
 */
public class VolatileMutil implements Runnable {
    volatile int a ;
    AtomicInteger atomicInteger = new AtomicInteger();
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            a++;
            atomicInteger.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileMutil volatileMutil = new VolatileMutil();
        Thread t1 = new Thread(volatileMutil);
        Thread t2 = new Thread(volatileMutil);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(volatileMutil.a);
        System.out.println(volatileMutil.atomicInteger.get());
    }
}
