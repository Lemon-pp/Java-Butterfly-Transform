## Java并发（二）--启动线程的正确和错误方式以及原理解析

### 目录

[TOC]

### 1. start() 和 run() 方法比较

```java
public class StartThreadTest {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        runnable.run();
        new Thread(runnable).start();
    }
}
```

![1585552881113](https://img-blog.csdnimg.cn/20200330183914659.png)

直接调用run()方法是由主线程执行，而通过start（）方法才是我们创建的线程进行执行的。

### 2. start() 方法原理解析

#### 2.1 start（）方法含义

1. 启动新线程，通知JVM在有空闲情况下启动新线程。具体启动时间有线程调度器决定。
2. 准备工作
   + 线程处于就绪状态
3. 不能重复调用start（）方法。

#### 2.2 start() 源码解析

1. 启动新线程检查线程状态

   首先会判断线程状态，如果线程状态不等于0，就会抛出IllegalThreadStateException()，

   而线程调用start方法后，线程状态就会改变。

2. 加入线程组

3. 调用start0()方法

```java
public synchronized void start() {
        
        零状态值对应于状态new
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

       添加线程组
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }
```



### 3. run() 方法原理解析

```java
@Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
```

两种情况：

+ 第一种直接调用，就相当于调用普通方法
+ 第二种，通过start方法来间接调用run()，来执行新线程的任务。