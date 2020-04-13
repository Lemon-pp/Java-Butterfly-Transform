package com.lemon.practice.demo;

import java.lang.reflect.Method;

/**
 * @author zhoup
 * @date 2020/3/31 10:33
 * @describe catch了InterruptedException之后优先选择：
 * 在方法签名中抛出异常，这样run()就会强制try/catch
 */
public class RightWayStopThreadTest implements Runnable{

    @Override
    public void run() {
        while (true && !Thread.currentThread().isInterrupted()){
            try {
                System.out.println("go");
                method();
            } catch (InterruptedException e) {
                System.out.println("日志");
                e.printStackTrace();
            }
        }
    }
    private void method() throws InterruptedException {
        Thread.sleep(2000);
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadTest());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
