package com.lemon.practice.demo2;

/**
 * @author zhoup
 * @date 2020/4/7 14:20
 * @describe 1. 不加try catch抛出4个异常，都带线程名字
 * 2. 加了try catch,期望捕获到第一个线程的异常，线程234不应该运行，希望看到打印出Caught Exception
 * 3. 执行时发现，根本没有Caught Exception，线程234依然运行并且抛出异常
 * 说明线程的异常不能用传统方法捕获
 */
public class ChildeThreadTryCatch implements Runnable{

    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        try {
            new Thread(new ChildeThreadTryCatch(),"Thread-1").start();
            new Thread(new ChildeThreadTryCatch(),"Thread-2").start();
            new Thread(new ChildeThreadTryCatch(),"Thread-3").start();
            new Thread(new ChildeThreadTryCatch(),"Thread-4").start();
        }catch (RuntimeException e){
            System.out.println("Caught Exception");


        }
    }
}
