package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/3/31 9:48
 * @describe 每次循环都调用sleep或wait方法，那么不需要每次迭代都检测是否被中断
 */
public class StopThreadWithSleepEveryTest {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = ()->{
            int num = 0;
            try {
                while (num <= 300 && !Thread.currentThread().isInterrupted()) {
                    if (num % 100 == 0) {
                        System.out.println(num + "是100的倍数");
                    }
                    num++;
                    Thread.sleep(10);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
