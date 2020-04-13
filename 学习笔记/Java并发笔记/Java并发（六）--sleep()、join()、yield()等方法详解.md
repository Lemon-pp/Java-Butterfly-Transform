## Java并发（六）--sleep()、join()、yield()等方法详解

### 目录

[TOC]

### 1.sleep()方法详解

作用：让线程在预期的时间执行，其他时候不要占用CPU资源。

性质：

1. sleep方法不会释放锁
2. sleep方法响应中断

代码实现：

1.sleep方法不会释放锁

```java
/**
 * @author zhoup
 * @date 2020/4/1 17:07
 * @describe   证明sleep不释放锁，等到sleep时间到了以后，
 * 正常结束才释放锁
 */
public class SleepDonotReleaseLock implements Runnable{
    private static Object lock = new Object();

    @Override
    public void run() {
        synchronized (lock){
            System.out.println(Thread.currentThread().getName() + "获得锁");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "开始苏醒");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new SleepDonotReleaseLock());
        Thread t2 = new Thread(new SleepDonotReleaseLock());
        t1.start();
        t2.start();
    }
}
```

运行结果：

![1585733189795](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1585733189795.png)

2.sleep方法响应中断

1. 抛出InterruptException
2. 清除中断状态

展示sleep的第二种写法：

```java
/**
 * @author zhoup
 * @date 2020/4/1 17:29
 * @describe   1.每隔1秒打印当前时间，然后中断，观察
 * 2.使用sleep第二种写法：TimeUnit.SECONDS.sleep()
 */
public class SleepInterruptTest implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("线程被中断了！");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new SleepInterruptTest());
        thread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}

```

运行结果：

![1585733656883](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1585733656883.png)

可以看出，sleep方法可以响应中断，且中断后可以继续运行，表面sleep方法可以清除中断状态。

**TimeUnit.SECONDS.sleep()的优点：**

1. Thread.sleep()只接受参数毫秒，而TimeUnit可以接受小时、分钟、秒等多种参数。
2. TimeUnit对于时间参数小于等于0，会直接什么都不做，而Thread.sleep会抛出异常。

源码：

```java
public void sleep(long timeout) throws InterruptedException {
        if (timeout > 0) {
            long ms = toMillis(timeout);
            int ns = excessNanos(timeout, ms);
            Thread.sleep(ms, ns);
        }
    }
```

**总结：**sleep方法可以让线程进入Waiting状态，并且不占用CPU资源，但是不释放锁，直到规定的时间后再执行，休眠期间如果被中断，会抛出异常并清除中断状态。

**wait()/notify和sleep异同：**

+ 相同
  1. 阻塞
  2. 响应中断
+ 不同
  1. 同步方法中
  2. 释放锁
  3. 参数指定时间
  4. 所属类

### 2. join()方法详解

![1585739898163](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1585739898163.png)

作用：等待相应线程运行结束。如果线程A调用线程B的join()方法，那么线程A的运行会被暂停，直到线程B运行结束。

用法：

1. 普通用法
2. 遇到中断
3. join期间线程状态

**1.普通用法代码：**

```java
/**
 * 描述：     演示join，注意语句输出顺序，会变化。
 */
public class Join {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完毕了");
            }
        },"线程1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            }
        },"线程2");

        thread.start();
        thread2.start();
        System.out.println("开始等待子线程运行完毕");
        thread.join();
        thread2.join();
        System.out.println("所有子线程执行完毕");
    }
}

打印结果：
开始等待子线程运行完毕
线程1执行完毕了
线程2执行完毕
所有子线程执行完毕
```

可以看到 打印结果：线程1调用join()方法，线程2要等待线程1执行完成，线程2调用join()方法，要等待线程2执行完毕。

**2.遇到中断的代码：**

```java
/**
 * @author zhoup
 * @date 2020/4/1 19:29
 * @describe   演示join期间被中断的效果
 */
public class JoinThreadInterruptTest {
        public static void main(String[] args) {
            Thread mainThread = Thread.currentThread(); //获取主线程的引用
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mainThread.interrupt();
                        Thread.sleep(5000);
                        System.out.println("Thread1 finished.");
                    } catch (InterruptedException e) {
                        System.out.println("子线程中断");
                      // e.printStackTrace();
                    }
                }
            });
            thread1.start();
            System.out.println("等待子线程运行完毕");
            try {
                thread1.join();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+"线程中断了");
                //thread1.interrupt();
                e.printStackTrace();
            }
            System.out.println("子线程已运行完毕");
        }
}
```

代码逻辑：

首先线程1开始启动，然后进入Thread1的run()方法中，在子线程中用主线程的引用中断主线程，而此时主线程正在等待子线程执行中，被中断，就会抛出异常，然后执行Thread1下面catch中的内容，然后打印子线程执行完毕，子线程执行完成，但是过几秒打印出Thread1 finished，说明子线程还没有完全结束，一旦join期间被打断，我们应该在处理中断异常的时候把子线程的内容都给停止，否则就会出现不一致的情况。我们在主线程获得中断的同时，把中断传递给子线程，这样子线程就会在sleep期间获得中断，抛出异常，执行catch中的内容。这时子线程的catch内容和主线程catch中的内容并行执行。

**3.join期间线程状态**

```java
/**
 * 描述：     先join再mainThread.getState()
 * 通过debugger看线程join前后状态的对比
 */
public class JoinThreadState {
    public static void main(String[] args) throws InterruptedException {
        Thread mainThread = Thread.currentThread();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println(mainThread.getState());
                    System.out.println("Thread-0运行结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        System.out.println("等待子线程运行完毕");
        thread.join();
        System.out.println("子线程运行完毕");

    }
}

```

运行结果：

![1585743041831](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1585743041831.png)

可以看到，在join期间，线程处于Waiting状态。

**join注意点**：

+ CountDownLatch或CyclicBarry类可以实现join

#### 2.1 join原理

1.源码

```java
public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }
```

我们可以发现最终还是调用wait()方法。但是我们看不到唤醒操作notify方法，这个在于JVM层，在run方法执行完后会自动执行notify方法。

2.Thread.join()等价写法：

```java
synchronized (thread) {
    thread.wait();
}
```

### 3. yield()方法详解

作用：释放自己的CPU时间片，但是此时线程状态仍为Runnable状态，不会释放自己的锁，也不会进入阻塞。JVM不一定会遵循yield方法。

yield()和sleep()的区别：是否随时可能再次被调度。



