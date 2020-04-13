## Java并发（九）--多线程带来的线程安全问题和性能问题详解

### 目录

[TOC]

### 1. 线程安全问题

​		不管业务中遇到怎样的多个线程访问某对象或某方法的情况，而在编程这个业务逻辑的时候，都不需要额外做任何额外的处理，程序也可以运行正常，就可以称为线程安全。

### 2. 产生线程安全问题的原因

多个线程同时访问共享资源。

主要两个问题：

 	1. 数据争用：数据由于同时读写，会造成数据错误。
 	2. 竞争条件：即使没有同时读写，由于顺序原因也有可能造成错误。

### 3. 线程不安全的三种现象

线程不安全的三种现象：

+ 运行结果错误，如a++在多线程情况下与期望结果不一致。
+ 活跃性问题：死锁、活锁、饥饿。
+ 对象发布和初始化的时候的安全问题。

#### 3.1 运行结果错误：

```java
/**
 * @author zhoup
 * @date 2020/4/7 15:19
 * @describe
 */
public class TwoTreadMutiTest implements Runnable{

    static int count;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TwoTreadMutiTest test = new TwoTreadMutiTest();
        Thread t1 = new Thread(test);
        Thread t2 = new Thread(test);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }
}

```

运行结果不是预期的2000，而且每次运行的结果不一致，说明发生了线程安全问题，导致了运行结果错误的问题。

i++：

![1586244224080](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586244224080.png)

实际运行过程中，i++过程中，会发生上图的过程。

#### 3.2 活跃性问题

死锁例子：

```java
package com.lemon.practice.demo;

public class DiedLockDemo {
    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        DiedLockDemo.diedLockTest();
    }

    private static void diedLockTest() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A){
                    try {
                        Thread.currentThread().sleep(2000);//等待2000ms等线程T2锁定B
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B){
                        System.out.println(1);
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    try {
                        Thread.currentThread().sleep(2000);//等待2000ms等线程T1锁定A
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (A){
                        System.out.println(2);
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}
```

#### 3.3对象的发布和逸出

对象发布是指使对象能够被其作用域之外的线程访问。

发布逸出：将一个对象的发布出现在我们不满意的结果或者对象发布本身不是我们期望的时候。

发布形式：

- 将对象引用存储到public变量中。
- 在非private方法中返回一个对象。
- 创建内部类，使得当前对象能够被这个内部类使用，
- 通过方法调用将对象传递给外部方法。

逸出情况：

1. 方法返回一个private对象。
2. 还未完成初始化就把对象提供给了外界。
   - 在构造函数未初始化完成就进行this赋值
   - 注册监听事件
   - 构造函数中运行线程。

避免逸出方法：

1. 返回副本。
2. 工厂模式。

### 4. 各种需要考虑线程安全的情况

1. 访问共享的变量和资源，会有并发风险，比如对象的属性、静态变量、共享缓存和数据库等。
2. 所以依赖时序的操作，即使每一步都是线程安全的。
   + read-and-write操作：一个线程读取了一个共享变量，并在此基础上更新该数据。比如index++。
   + check-then-act操作：一个线程读取了一个共享变量，并在此基础上决定其下一个的操作。
3. 不同的数据之间存在捆绑关系：比如ip和端口
4. 线程不安全的类。比如hashmap、Arraylist



### 5. 性能问题以及体现

体现：

+ 服务器响应慢，吞吐量低，资源消耗过高。

### 6. 性能问题产生原因

1. 调度：上下文切换

   ​	上下文：看着连续运行的线程，实际上是以断断续续运行的方式使其任务进展的。而在切出和切入的时候操作系统需要保存和恢复相应线程的进度信息，即切入和切出那一刻相应线程执行到了什么程度了。这种进度信息叫做上下文。一般包括通用寄存器的内容和程序计数器的内容。

   ​	上下文切换：当一个线程由于其时间片用完或者自身的原因被迫或者主动暂停其运行时，另外一个线程可以被操作系统选中占用处理器开始或者继续其运行，这种过程被称为上下文切换。

   上下文切换种类：

    + 自发性上下文切换
       + Thread.sleep()
       + Object.wait()
       + Thread.yield()
       + Thread.join
       + LockSupport.parl()
       + 线程发起I/O操作或者等待锁（会密集的发生上下文切换）
    + 非自发性上下文切换
       + 线程调度器的原因（如时间片用完或者线程优先级有关）

2. 协作：内存同步

   主要涉及Java内存模型。

