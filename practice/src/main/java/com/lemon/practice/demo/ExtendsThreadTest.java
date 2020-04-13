package com.lemon.practice.demo;

public class ExtendsThreadTest extends Thread {
    @Override
    public void run() {
        System.out.println(2);
    }

    public static void main(String[] args) {
        new ExtendsThreadTest().start();
    }
}
