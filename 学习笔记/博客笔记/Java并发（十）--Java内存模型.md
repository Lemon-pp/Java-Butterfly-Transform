## Java并发（十）--Java内存模型

### 目录

[TOC]

### 1. 底层原理

Java代码---->CPU指令过程：

 + 首先是.java文件
 + 编译后变成字节码.class文件
 + JVM将字节码文件转为机器指令
 + 机器指令就可以直接在CPU上运行。

由于JVM会带来不同的“翻译”， 不同的CPU平台的机器指令又各有不同，所以无法保证并发安全的效果一致。

所以就出现了Java内存模型这一套规范。

分清楚三个概念：

1.JVM内存模型

​	和Java虚拟机的运行时区域有关。

![1586328202831](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586328202831.png)

2.Java内存模型

​	和Java的并发编程有关。

3.Java对象模型

​	和Java对象在虚拟机中的表现形式有关。Java对象自身的存储模型。

### 2. Java内存模型介绍

 	1. Java Memory Model,JMM是一种规范，需要各个JVM的实现来遵守JMM规范，以便于开发者可以利用这些规范，更方便地开发多线程程序。
 	2. 因为没有这种规范的话，程序很可能经过了不同JVM的不同规则的重排序之后，导致不同的虚拟机上运行的结果不一样，产生一些问题。
 	3. JMM是工具类和关键字的原理。

JMM最重要的三点内容：

1.原子性

2.可见性

3.有序性（重排序）

### 3. 原子性

### 4. 可见性

#### 4.1 关于可见性

概念：可见性就是指一个线程对共享变量的更新的结果对于读取相应共享变量的线程而言是否可见的问题。

代码演示：

```java
/**
 * 描述：     演示可见性带来的问题
 * 会出现： 2，1   3，3   2，3  3，1
 */
public class FieldVisibility {

    volatile int a = 1;
    volatile int b = 2;

    private void change() {
        a = 3;
        b = a;
    }


    private void print() {
        System.out.println("b=" + b + ";a=" + a);
    }

    public static void main(String[] args) {
        while (true) {
            FieldVisibility test = new FieldVisibility();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.change();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    test.print();
                }
            }).start();
        }

    }


}

```

#### 4.2 可见性问题产生的原因

CPU有多级缓存，导致读的数据过期。

+ 高速缓存的容量比主内存小，但是速度仅次于寄存器，所以在CPU和主内存之间就多了Cache层。
+ 线程间对于共享变量的可见性问题不是直接由多核引起的，而是由多缓存引起的。
+ 每个核心都会将自己需要的数据读到独占缓存中，数据修改后也是写到缓存中，然后等待刷入到主内存中。所以导致有些核心读取的指是一个过期的值。

所有的共享变量存在于主内存，每个线程有自己的本地内存，而且线程读写共享数据也是通过本地内存交换的，所以才导致了可见性问题。

#### 4.3 JMM的抽象：主内存和本地内存

​	Java作为高级语言，屏蔽了这些底层细节，用JMM定义了一套读写内存数据的规范，我们不再需要关心一级缓存、二级缓存，而是JMM抽象了主内存和本地内存。

![1586414990348](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586414990348.png)

JMM的规定：

1. 所有的变量都存储在主内存中，同时每个线程也由自己独立的工作内存，工作内存中的变量内容是主内存中的拷贝。
2. 线程不能直接读写主内存中的变量，而是只能操作自己工作内存的变量，然后再同步到主内存中。
3. 主内存是多个线程共享的，但线程间不共享工作内存，如果线程间需要通信，必须借助主内存中转来完成。 

共享变量可见性的实现原理：

1. .线程A在自己的工作内存中修改变量之后，需要将变量的值刷新到主内存中
2. 线程B要把主内存中变量的值更新到工作内存中

#### 4.4 Happens-Before原则

解释：happens-before规则是用来解决可见性问题的：在时间上，动作A发生在动作B之前，B保证能看见A，这就是happens-before.

happens-before规则：

1. 单线程规则
2. **锁操作（synchronized和Lock）**
3. **volatile变量**
4. 线程启动
5. 线程join：join后面的语句一定可以看到前面的语句。
6. 传递性。
7. 中断
8. 构造方法
9. 工具类的Happens-Before原则：
   1. 线程安全的容器get一定能看到在此之前的put等存入动作。
   2. CountDownLatch
   3. Semaphore
   4. Future
   5. 线程池
   6. CyclicBarry

#### 4.5 解决可见性问题的方法

 	1. volatile
 	2. synchronized
 	3. Lock锁

### 5. 有序性

#### 5.1 重排序的概念

重排序：在线程内部的Java代码实际执行顺序和代码在Java文件中的顺序不一致，代码指令并不是严格按照代码语句顺序执行的，它们的顺序被改变的情况就叫做重排序。

重排序例子：

```java
/**
 * @author zhoup
 * @date 2020/4/8 15:37
 * @describe  重排序的现象,根据执行顺序，可以出现四种情况：
 * 1，0   0，1   1，1   0，0
 */
public class OutOfOrderExecution {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;

            CountDownLatch latch = new CountDownLatch(3);

            Thread one = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    a = 1;
                    x = b;
                }
            });
            Thread two = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b = 1;
                    y = a;
                }
            });
            two.start();
            one.start();
            latch.countDown();
            one.join();
            two.join();

            String result = "第" + i + "次（" + x + "," + y + ")";
            if (x == 0 && y == 0) {
                System.out.println(result);
                break;
            } else {
                System.out.println(result);
            }
        }
    }
}

```

**重排序的好处：**提高处理速度。

**重排序的3种情况：**

+ 编译器优化：包括JVM，JIT编译器等。
+ CPU指令重排
+ 内存的“重排序”：线程A的修改线程B看不到，引出可见性的问题。



