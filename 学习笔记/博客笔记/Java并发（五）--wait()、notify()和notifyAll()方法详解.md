## Java并发（五）--wait()、notify()和notifyAll()方法详解

目录

[TOC]

### 1. 重要方法概览

Thread类:

+ sleep相关   --线程休眠
+ join   ---等待其他线程执行完毕
+ yield相关  --放弃已经获取到的CPU资源
+ currentThread相关 ---获取当前执行线程的引用
+ start、run相关  --启动线程相关
+ interrupt相关  ---中断线程
+ stop、suspend、resume相关   --弃用方法

Object类：

+ wait、notify、notifyAll相关  ---让线程暂时休息和唤醒

### 2.wait、notify、notifyAll详解

#### 2.1 作用、用法

1.阻塞阶段--调用wait()方法使线程休息。

注意点：

 + 执行wait()方法首先要有这个对象的监视器monitor锁。
 + 调用wait()方法的线程会让出CPU，且会释放锁。

只有以下四种情况之一发送才可以被唤醒：

1. 另一个线程调用该对象的notify()方法且刚好被唤醒的是本线程。
2. 另一个线程调用该对象的notifyAll()方法
3. 过来wait(long timeout)规定的超时时间，如果传入0就是永久等待。
4. 线程自身调用了interrupt()。

2.唤醒阶段

**notify():** 会唤醒单个正在等待某对象monitor的线程，如果执行时，有多个线程正在等待，

只会唤醒其中的任意一个，具体有JVM自己决定。

**notifyAll():** 会唤醒所有正在等待monitor的线程。

3.遇到中断

如果线程执行了wait()方法，遇到了中断，会抛出InterruptedException异常，

并且会释放当前已经获取的monitor。

**注意：**这三个方法都要在同步代码块中执行。

### 3. 代码实践

**1.展示wait和notify方法的基本用法**

1. 研究代码的执行顺序
2. 证明wait释放锁

```java
/**
 * @author zhoup
 * @date 2020/3/31 17:37
 * @describe
 */
public class WaitNotifyTest {
    private static Object object = new Object();

    static class T1 extends Thread{
        @Override
        public void run() {
            synchronized (object){
                System.out.println("线程" + Thread.currentThread().getName() + "开始执行了");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "获得锁");
            }
        }
    }
    static class T2 extends Thread{
        @Override
        public void run() {
            synchronized (object){
                object.notify();
                System.out.println("线程" + Thread.currentThread().getName() + "执行了notify()");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T1 t1 = new T1();
        T2 t2 = new T2();
        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}
```

运行结果：

![1585703533771](https://img-blog.csdnimg.cn/20200401160846837.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

从运行结果就可以证明以上两点。

**2.展示notify和notifyAll()的区别：**

```java
/**
 * @author zhoup
 * @date 2020/4/1 9:13
 * @describe  1. 三个线程，线程1和线程2首先被阻塞，
 * 线程3通过notifyAll唤醒他们。2.证明start先执行不代表线程先启动。
 */
public class NotifyNotifyAll implements Runnable{

    private static Object object = new Object();
    @Override
    public void run() {
        synchronized (object){
            System.out.println("线程"+ Thread.currentThread().getName() + "拿到锁");
            try {
                System.out.println("线程"+ Thread.currentThread().getName() + "wait to start");
                object.wait();
                System.out.println("线程"+ Thread.currentThread().getName() + "wait to end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new NotifyNotifyAll());
        Thread t2 = new Thread(new NotifyNotifyAll());
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    object.notifyAll();
                    //object.notify();
                    System.out.println("线程" + Thread.currentThread().getName() + "执行了notifyAll");
                }
            }
        });
        t1.start();
        t2.start();
        try {
            Thread.sleep(100);//休眠是为了保证t2线程在t3线程之前执行。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();
      
    }
}
```

运行结果：

![1585705221319](https://img-blog.csdnimg.cn/20200401160915427.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

如果执行object.notifyAll()，打印结果如上图，线程1和线程2均会唤醒然后执行后续任务。

而执行object.notify()，线程1和线程2只会唤醒其中一个，且唤醒线程随机。由JVM决定。

**3.只释放当前monitor：**

```java
/**
 * @author zhoup
 * @date 2020/4/1 9:53
 * @describe   证明wait只会释放当前那把锁
 */
public class WaitReleaseOwnTest implements Runnable{
    private static  Object resouceA = new Object();
    private static  Object resouceB = new Object();

    @Override
    public void run() {
        synchronized (resouceA){
            System.out.println("线程" + Thread.currentThread().getName() + "got resouceA ");
            synchronized (resouceB){
                System.out.println("线程" + Thread.currentThread().getName() + "got resouceB ");
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "release resouceA ");
                    resouceA.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new WaitReleaseOwnTest());
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//sleep1秒是为了等待线程1释放resourceA
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resouceA) {
                    System.out.println("线程" + Thread.currentThread().getName() + "got resouceA ");
                    System.out.println("线程" + Thread.currentThread().getName() + "try to get resouceB ");
                    synchronized (resouceB) {
                        System.out.println("线程" + Thread.currentThread().getName() + "got resouceB ");
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}

```

运行结果：

![1585706648949](https://img-blog.csdnimg.cn/20200401160938640.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

可以从运行结果看到线程1执行wait()方法，只释放了resourceA，而resourceB没有释放。线程2一直请求不到resourceB。

### 4.wait、notify、notifyAll特点、性质

1. 用之前必须拥有monitor,需要在同步代码块中执行。
2. notify()只能随机唤醒一个，具体由JVM决定。
3. 都属于Object类，object类是所有类的父类，任何对象都可以调用这三个方法。而且都属于native方法，所以具体实现都在JVM层。
4. 类似功能Condition。是封装好的类。
5. 同时持有多把锁。

### 5. wait()底层原理

原理图：

![1585707484086](https://img-blog.csdnimg.cn/20200401161011985.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

+ EntrySet：入口集
+ WaitSet：出口集

首先线程争夺锁，如果锁被其他线程获取 ，没获取到锁的线程都会放到EntrySet入口集中，然后继续等待获取锁，如果获取锁的线程正常释放则会退出，如果被wait了则会进入左边的等待集中的蓝色部分，知道被notified了，则会进入等待集中的下面的粉色部分，然后来争夺锁。

**生命周期状态转换的特殊情况：**

1. 从Object.wait()状态刚被唤醒时，通常不能立刻抢到monitor锁，就会从Waiting状态转为Blocked状态，抢到锁后转化为Runnable状态。
2. 如果发送异常，可以直接跳到终止Terminated状态。

### 6. 手写生产者和消费者设计模式

流程图：

![1585709327956](https://img-blog.csdnimg.cn/202004011610300.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

用代码实现用wait/notify来实现生产者消费者模式：

```java
/**
 * 描述：     用wait/notify来实现生产者消费者模式
 */
public class ProducerConsumerModel {
    public static void main(String[] args) {
        EventStorage eventStorage = new EventStorage();
        Producer producer = new Producer(eventStorage);
        Consumer consumer = new Consumer(eventStorage);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

class Producer implements Runnable {

    private EventStorage storage;

    public Producer(
            EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.put();
        }
    }
}

class Consumer implements Runnable {

    private EventStorage storage;

    public Consumer(
            EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.take();
        }
    }
}

class EventStorage {

    private int maxSize;
    private LinkedList<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    public synchronized void put() {
        while (storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(new Date());
        System.out.println("仓库里有了" + storage.size() + "个产品。");
        notify();
    }

    public synchronized void take() {
        while (storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("拿到了" + storage.poll() + "，现在仓库还剩下" + storage.size());
        notify();
    }
}
```

### 7.小例子

**1.两个线程交替打印0-100**
两种方法：

+ 使用synchronized，缺点会比较浪费
+ 使用wait()、notify()。

synchronized实现代码：

```java
/**
 * 描述：     两个线程交替打印0~100的奇偶数，用synchronized关键字实现
 */
public class WaitNotifyPrintOddEvenSyn {

    private static int count;
    private static final Object lock = new Object();
    //新建2个线程
    //1个只处理偶数，第二个只处理奇数（用位运算）
    //用synchronized来通信
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    synchronized (lock) {
                        if ((count & 1) == 0) {
                            System.out.println(Thread.currentThread().getName() + ":" + count++);
                        }
                    }
                }
            }
        }, "偶数").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    synchronized (lock) {
                        if ((count & 1) == 1) {
                            System.out.println(Thread.currentThread().getName() + ":" + count++);
                        }
                    }
                }
            }
        }, "奇数").start();
    }
}
```

wait()和notify()实现代码：

```java
/**
 * 描述：     两个线程交替打印0~100的奇偶数，用wait和notify
 */
public class WaitNotifyPrintOddEveWait {

    private static int count = 0;
    private static final Object lock = new Object();


    public static void main(String[] args) {
        new Thread(new TurningRunner(), "偶数").start();
        new Thread(new TurningRunner(), "奇数").start();
    }

    //1. 拿到锁，我们就打印
    //2. 打印完，唤醒其他线程，自己就休眠
    static class TurningRunner implements Runnable {

        @Override
        public void run() {
            while (count <= 100) {
                synchronized (lock) {
                    //拿到锁就打印
                    System.out.println(Thread.currentThread().getName() + ":" + count++);
                    lock.notify();
                    if (count <= 100) {
                        try {
                            //如果任务还没结束，就让出当前的锁，并休眠
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

```

