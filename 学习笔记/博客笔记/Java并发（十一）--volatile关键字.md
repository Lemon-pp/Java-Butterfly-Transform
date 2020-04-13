## Java并发（十一）--volatile关键字

### 目录

[TOC]

### 1. 什么是volatile？

​	volatile是一种同步机制，比synchronized或者Lock相关类更轻量，因为使用volatile并不会发生上下文切换等开销很大的行为。

### 2. volatile的适用场合

不适合场景：a++；

代码：

```java
/**
 * @author zhoup
 * @date 2020/4/9 15:45
 * @describe
 */
public class VolatileMutil implements Runnable {
    volatile int a ;
    AtomicInteger atomicInteger = new AtomicInteger();
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            a++;
            atomicInteger.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileMutil volatileMutil = new VolatileMutil();
        Thread t1 = new Thread(volatileMutil);
        Thread t2 = new Thread(volatileMutil);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(volatileMutil.a);
        System.out.println(volatileMutil.atomicInteger.get());
    }
}

```

运行结果：可以看到a++的结果小于等于20000，且每次结果不一样。

适合场景：

​	某个属性被多个线程共享，其中有一个线程修改了此属性，其他线程可以立即得到修改后的值。或者作为触发器。

### 3. volatile的作用

1. **可见性：**读一个volatile变量之前，需要先使相应的本地缓存失效，这样就必须到主内存读取最新值，写一个volatile变量会立即刷新到主内存。
2. **保证有序性，禁止指令重排序优化。**

### 4. volatile和synchronized的关系

​	volatile可以看作是轻量版的synchronized，如果一个共享变量自始至终只能被各个线程赋值，而没有其他操作，那么就可以用volatile来代替synchronized或者代替原子变量，因为赋值本身是有原子性的，而volatile又保证了可见性，所以足以保证线程安全。

### 5. 用volatile解决重排序的问题

### 6. volatile特点

1. volatile属性的读写操作都是无锁的，不能替代synchronized，不能提供原子性和互斥性。但是因为无锁，所以也不需要花费时间在获取锁和释放锁上，成本低。
2. volatile只能作为属性，用volatile修饰属性可以禁止指令重排序。
3. volatile提供可见性，任何一个线程对其的修改将立马对其他线程可见。volatile属性不会被线程缓存，始终从主存中读取。
4. volatile提供了happens-before保证，对volatile变量v的写入happens-before所有其他线程后续对v的读操作。

