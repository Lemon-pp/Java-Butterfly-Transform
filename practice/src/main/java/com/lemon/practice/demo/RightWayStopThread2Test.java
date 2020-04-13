package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/3/31 10:56
 * @describe 在catch子语句中调用Thread.currentThread.interrupt()来
 * 设置中断状态，以便在后续的执行中，依然能够检查到刚才发生了中断，
 * 然后补上中断，以便跳出
 */
public class RightWayStopThread2Test implements Runnable{
    @Override
    public void run() {
        while (true){
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("Interrupt,程序结束");
                    break;
                }
                method();
        }
    }
    private void method() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThread2Test());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
