package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/4/1 9:53
 * @describe   证明wait只会释放当前那把锁
 */
public class WaitReleaseOwnTest implements Runnable{
    private static  Object resouceA = new Object();
    private static  Object resouceB = new Object();

    @Override
    public void run() {
        synchronized (resouceA){
            System.out.println("线程" + Thread.currentThread().getName() + "got resouceA ");
            synchronized (resouceB){
                System.out.println("线程" + Thread.currentThread().getName() + "got resouceB ");
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "release resouceA ");
                    resouceA.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new WaitReleaseOwnTest());
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resouceA) {
                    System.out.println("线程" + Thread.currentThread().getName() + "got resouceA ");
                    System.out.println("线程" + Thread.currentThread().getName() + "try to get resouceB ");
                    synchronized (resouceB) {
                        System.out.println("线程" + Thread.currentThread().getName() + "got resouceB ");
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}
