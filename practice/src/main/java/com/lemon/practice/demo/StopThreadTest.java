package com.lemon.practice.demo;

/**
 * @author zhoup
 * @date 2020/3/30 18:54
 * @describe run方法里面没有sleep或者wait方法时，停止线程。打印最大整数下所有10000的倍数
 */
public class StopThreadTest implements Runnable{
    @Override
    public void run() {
        int num = 0;
        while (!Thread.currentThread().isInterrupted() && num <= Integer.MAX_VALUE / 2){
            if (num % 10000 == 0){
                System.out.println(num + "是10000的倍数 ");
            }
            num++;
        }
        System.out.println("任务执行完成！");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new StopThreadTest());
        t.start();
        try {
            t.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();

    }
}
