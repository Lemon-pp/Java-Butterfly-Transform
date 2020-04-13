package com.lemon.practice.demo2;

/**
 * @author zhoup
 * @date 2020/4/1 19:29
 * @describe   演示join期间被中断的效果
 */
public class JoinThreadInterruptTest {
        public static void main(String[] args) {
            Thread mainThread = Thread.currentThread(); //获取主线程的引用
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mainThread.interrupt();
                        Thread.sleep(5000);
                        System.out.println("Thread1 finished.");
                    } catch (InterruptedException e) {
                        System.out.println("子线程中断");
                      // e.printStackTrace();
                    }
                }
            });
            thread1.start();
            System.out.println("等待子线程运行完毕");
            try {
                thread1.join();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+"线程中断了");
                thread1.interrupt();
                e.printStackTrace();
            }
            System.out.println("子线程已运行完毕");
        }
}
