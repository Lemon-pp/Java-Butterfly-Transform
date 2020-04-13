package com.lemon.practice.demo2;

/**
 * @author zhoup
 * @date 2020/4/1 17:07
 * @describe   证明sleep不释放锁，等到sleep时间到了以后，
 * 正常结束才释放锁
 */
public class SleepDonotReleaseLock implements Runnable{
    private static Object lock = new Object();

    @Override
    public void run() {
        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + "获得锁");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "开始苏醒");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new SleepDonotReleaseLock());
        Thread t2 = new Thread(new SleepDonotReleaseLock());
        t1.start();
        t2.start();
    }
}
