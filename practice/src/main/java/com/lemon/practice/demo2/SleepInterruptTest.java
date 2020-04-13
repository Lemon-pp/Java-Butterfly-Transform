package com.lemon.practice.demo2;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoup
 * @date 2020/4/1 17:29
 * @describe   1.每隔1秒打印当前时间，然后中断，观察
 * 2.使用sleep第二种写法：TimeUnit.SECONDS.sleep()
 */
public class SleepInterruptTest implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("线程被中断了！");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new SleepInterruptTest());
        thread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}
