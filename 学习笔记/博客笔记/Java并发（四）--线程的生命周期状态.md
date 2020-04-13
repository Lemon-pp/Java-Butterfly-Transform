## Java并发（四）--线程的生命周期状态

### 目录

[TOC]

### 1. 六种生命周期介绍

​	Java线程的状态可以使用监控工具查看，也可以通过Thread.getState()调用来获取。返回值是一个枚举类型。

```java
线程状态。线程可以处于以下状态之一：
    public enum State {
        尚未启动的线程处于此状态。
        NEW,

        Java虚拟机中执行的线程处于此状态。
        RUNNABLE,

        等待监视器锁定而被阻塞的线程处于这种状态。
        BLOCKED,

        无限期等待另一个线程执行特定操作处于此状态。
        WAITING,

        等待另一线程执行操作的线程在指定的等待时间内处于此状态。
        TIMED_WAITING,

        已退出的线程处于此状态。
        TERMINATED;
    }

    返回此线程的状态。
    public State getState() {
        // get current thread state
        return sun.misc.VM.toThreadState(threadStatus);
    }
```

1. **NEW**:

   一个已创建但是没有启动的线程处于这个状态，由于一个线程实例只能被启动一次，因此一个线程只可能有一次处于该状态。

2. **Runnable**:

   该状态可以看作是一个复合状态，包括两个子状态：Ready和Running。前者表示该状态的线程可以被线程调度器进行调度使之处于Running状态。后者表示处于该状态的线程正在运行，即相应线程对象的run()方法所对应的指令正在由处理器执行。执行Thread.yield()的线程，其状态可能有Running转换位Ready。处于Ready子状态的线程也被称为活跃线程。

3. **Blocked：**

   一个线程发起一个阻塞式I/O操作后，或者申请一个由其他线程持有的独占资源（比如锁）时，相应的线程会处于该状态。处于Blocked状态的线程不会占用处理器资源。当阻塞式I/O操作完成或者线程申请到了资源，该线程的状态又可以转换为Runnable。

4. **Waiting：**

   一个线程执行了某些特定的方法之后会处于这种等待其他线程执行另外一些特定操作的状态。具体方法以及转换看下图。

5. **Timed_Waiting:**

   该状态与Waiting相似，差别在于处于该状态的线程并非无限制的等待其他线程执行特定操作，而是处于带有时间限制的等待状态。当其他线程没有在指定时间内执行该线程所期望的特定操作时，该线程的状态自动转换为Runnable。

6. **Terminated：**

   已经执行结束的线程处于这个状态，由于一个线程实例只能被启动一次，因此一个线程只可能有一次处于该状态。Thread.run()正常返回或者由于抛出异常而提前终止都会导致相应线程处于该状态。

### 2. 状态转换图

![1585638239035](https://img-blog.csdnimg.cn/20200331154727784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

### 3. 阻塞状态

一般Blocked、Waiting、Timed_Waiting都称为阻塞状态。

### 4. 代码实践

1.实现NEW、Runnable、Terminated三种状态：

```java
/**
 * @author zhoup
 * @date 2020/3/31 15:08
 * @describe 实现NEW、Runnable、Terminated三种状态：
 */
public class NewRunnableTerminatedTest implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        NewRunnableTerminatedTest runnable = new NewRunnableTerminatedTest();
        Thread t1 = new Thread(runnable);
        //打印出NEW
        System.out.println(t1.getState());
        t1.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打印出Runnable
        System.out.println(t1.getState());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打印出Terminated
        System.out.println(t1.getState());
    }

}
```

2.实现BLOCKED、Waiting、Timed_Waiting

```java
/**
 * @author zhoup
 * @date 2020/3/31 15:25
 * @describe 实现BLOCKED、Waiting、Timed_Waiting
 */
public class BlockedWaitingTimedWaitingTest implements Runnable {

    @Override
    public void run() {
        syn();
    }
    private synchronized void syn() {
        try {
            Thread.sleep(1000);
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        BlockedWaitingTimedWaitingTest runnable = new BlockedWaitingTimedWaitingTest();
        Thread t1 = new Thread(runnable);
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t2 = new Thread(runnable);
        t2.start();
        //打印出TIMED_WAITING，因为Thread.sleep(1000)处于休眠中
        System.out.println(t1.getState());
        //打印出BLOCKED，因为想要执行syn()方法却拿不到锁
        System.out.println(t2.getState());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打印出WAITING,因为执行syn()方法中的wait()。
        System.out.println(t1.getState());
    }
}
```