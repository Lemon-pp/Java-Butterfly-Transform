package com.lemon.practice.demo;

public class CreateThreadBothTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(2);
            }
        }){
            @Override
            public void run(){
                System.out.println(1);
            }
        }.start();
    }
}
