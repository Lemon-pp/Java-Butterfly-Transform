package com.lemon.practice.demo3;

/**
 * @author zhoup
 * @date 2020/4/10 17:36
 * @describe
 */
public class PhilosopherTest {
    static class Philosopher implements Runnable {
        private Object leftChopstick;
        private Object rightChopstick;

        public Philosopher(Object leftChopstick, Object rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    doAction("think;");
                    synchronized (leftChopstick) {
                        doAction("get left;");
                        synchronized (rightChopstick) {
                            doAction("get right;");
                        }
                        doAction("put down left and right");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private void doAction(String action) throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " " + action);
            Thread.sleep((long) (Math.random() * 10));
        }
    }

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] chopsticks = new Object[philosophers.length];
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }
        for (int i = 0; i < philosophers.length; i++) {
            Object leftchopstick = chopsticks[i];
            Object rightchopstick = chopsticks[(i+1) % chopsticks.length];
            if(i == philosophers.length-1){
                philosophers[i] = new Philosopher(rightchopstick , leftchopstick);
            }else {
                philosophers[i] = new Philosopher(leftchopstick , rightchopstick);
            }
            new Thread(philosophers[i], "哲学家"+ (i+1) +"号").start();
        }
    }
}
