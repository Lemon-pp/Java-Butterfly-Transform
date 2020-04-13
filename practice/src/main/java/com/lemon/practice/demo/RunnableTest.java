package com.lemon.practice.demo;

public class RunnableTest implements Runnable{

    @Override
    public void run() {
        System.out.println(1);
    }

    public static void main(String[] args) {
        new Thread(new RunnableTest()).start();
    }
}
