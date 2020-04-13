package com.lemon.practice.demo2;

/**
 * @author zhoup
 * @date 2020/4/7 14:15
 * @describe
 */
public class ExceptionChildThread implements Runnable{

    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        new Thread(new ExceptionChildThread()).start();
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }
}
