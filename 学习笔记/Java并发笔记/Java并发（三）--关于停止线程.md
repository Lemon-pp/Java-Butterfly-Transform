## Java并发（三）--关于停止线程

### 目录

[TOC]

### 1. 原理介绍

​	使用interrupt来通知，而不是强制。

### 2. 停止线程的实践

#### 2.1正确的停止方法

##### 2.2.1 普通情况

```java
/**
 * @author zhoup
 * @date 2020/3/30 18:54
 * @describe run方法里面没有sleep或者wait方法时，停止线程。打印最大整数下所有10000的倍数
 */
public class StopThreadTest implements Runnable{
    @Override
    public void run() {
        int num = 0;
        while ( num <= Integer.MAX_VALUE / 2){
            if (num % 10000 == 0){
                System.out.println(num + "是10000的倍数 ");
            }
            num++;
        }
        System.out.println("任务执行完成！");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new StopThreadTest());
        t.start();
    }
}
```

不使用interrupt()方法时的运行结果：

![1585617246638](https://img-blog.csdnimg.cn/20200331135621958.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

```java
/**
 * @author zhoup
 * @date 2020/3/30 18:54
 * @describe run方法里面没有sleep或者wait方法时，停止线程。打印最大整数下所有10000的倍数
 */
public class StopThreadTest implements Runnable{
    @Override
    public void run() {
        int num = 0;
        while ( num <= Integer.MAX_VALUE / 2){
            if (num % 10000 == 0){
                System.out.println(num + "是10000的倍数 ");
            }
            num++;
        }
        System.out.println("任务执行完成！");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new StopThreadTest());
        t.start();
        try {
            t.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();


    }
}
```

这是使用了interrupt()方法的运行结果：

![1585617281429](https://img-blog.csdnimg.cn/20200331135621958.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

可以发现使用和没使用的运行结果都一样，interrupt()方法只是给程序一个中断信号。

再加上判断条件，检测当前线程是否被中断：

```java
/**
 * @author zhoup
 * @date 2020/3/30 18:54
 * @describe run方法里面没有sleep或者wait方法时，停止线程。打印最大整数下所有10000的倍数
 */
public class StopThreadTest implements Runnable{
    @Override
    public void run() {
        int num = 0;
        while (!Thread.currentThread().isInterrupted() && num <= Integer.MAX_VALUE / 2){
            if (num % 10000 == 0){
                System.out.println(num + "是10000的倍数 ");
            }
            num++;
        }
        System.out.println("任务执行完成！");
    }

    public static void main(String[] args) {
        Thread t = new Thread(new StopThreadTest());
        t.start();
        try {
            t.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();

    }
}
```

加了判断条件后运行结果：

![1585617333588](https://img-blog.csdnimg.cn/20200331135729573.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)



##### 2.2.2 在阻塞情况下中断线程

```java
/**
 * @author zhoup
 * @date 2020/3/31 9:28
 * @describe   带有sleep方法的中断
 */
public class StopThreadWithSleepTest {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = ()->{
            int num = 0;
            try {
                while (num <= 300 && !Thread.currentThread().isInterrupted()) {
                    if (num % 100 == 0) {
                        System.out.println(num + "是100的倍数");
                    }
                    num++;
                }
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
    }
}

```

运行结果：

![1585619081569](https://img-blog.csdnimg.cn/20200331135753693.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

可以看到执行完成，报出sleep interrupted异常。

##### 2.2.3 线程每次迭代后都阻塞的情况下中断

```java
/**
 * @author zhoup
 * @date 2020/3/31 9:48
 * @describe 每次循环都调用sleep或wait方法，那么不需要每次迭代都检测是否被中断
 */
public class StopThreadWithSleepEveryTest {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = ()->{
            int num = 0;
            try {
                while (num <= 300 && !Thread.currentThread().isInterrupted()) {
                    if (num % 100 == 0) {
                        System.out.println(num + "是100的倍数");
                    }
                    num++;
                    Thread.sleep(10);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}

```

运行结果：

![1585619580266](https://img-blog.csdnimg.cn/20200331135818181.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

我们发现，打印时间，较慢，大部分时间消耗在循环中的休眠10毫秒中，所以我们取消检测程序是否被中断的判断：

发现运行结果没有变化，所以，当每次循环都调用sleep或wait方法，那么不需要每次迭代都检测是否被中断。

##### 2.2.4 TryCatch放到while里面的问题

```java
/**
 * @author zhoup
 * @date 2020/3/31 9:58
 * @describe 把TryCatch放到while中，会出现
 */
public class StopThreadTryCatchProblemTest {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            int num = 0;
            while (num <= 10000 && !Thread.currentThread().isInterrupted()) {
                if (num % 100 == 0) {
                    System.out.println(num + "是100的倍数");
                }
                num++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
```

运行结果：

![1585620434689](https://img-blog.csdnimg.cn/20200331135841701.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

可以看到，中断后程序继续运行。

**原因:sleep函数会把interrupt标志位清除,所以程序找不到被中断的迹象。**

##### 2.2.5 停止线程的两种最佳实践

**1.传递中断**

```java
/**
 * @author zhoup
 * @date 2020/3/31 10:33
 * @describe catch了InterruptedException之后优先选择：
 * 在方法签名中抛出异常，这样run()就会强制try/catch
 */
public class RightWayStopThreadTest implements Runnable{

    @Override
    public void run() {
        while (true && !Thread.currentThread().isInterrupted()){
            method();
        }
    }
    private void method() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadTest());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
```

运行结果：

![1585622394230](https://img-blog.csdnimg.cn/20200331135902630.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

发现抛出异常，但是程序没有被中断，sleep()会把interrupt标志位清除。

于是我们catch了InterruptedException之后优先选择：

 * 在方法签名中抛出异常，这样run()就会强制try/catch

```java
/**
 * @author zhoup
 * @date 2020/3/31 10:33
 * @describe catch了InterruptedException之后优先选择：
 * 在方法签名中抛出异常，这样run()就会强制try/catch
 */
public class RightWayStopThreadTest implements Runnable{

    @Override
    public void run() {
        while (true && !Thread.currentThread().isInterrupted()){
            try {
                System.out.println("go");
                method();
            } catch (InterruptedException e) {
                System.out.println("日志");
                e.printStackTrace();
            }
        }
    }
    private void method() throws InterruptedException {
        Thread.sleep(2000);
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThreadTest());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
```

运行结果：

![1585623135261](https://img-blog.csdnimg.cn/20200331135922811.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

**这样我们可以在run()方法做完事的手续处理。**

**2.恢复中断**

在catch子语句中调用Thread.currentThread.interrupt()来
 * 设置中断状态，以便在后续的执行中，依然能够检查到刚才发生了中断，
 * 然后补上中断，以便跳出

```java
/**
 * @author zhoup
 * @date 2020/3/31 10:56
 * @describe 在catch子语句中调用Thread.currentThread.interrupt()来
 * 设置中断状态，以便在后续的执行中，依然能够检查到刚才发生了中断，
 * 然后补上中断，以便跳出
 */
public class RightWayStopThread2Test implements Runnable{
    @Override
    public void run() {
        while (true){
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("Interrupt,程序结束");
                    break;
                }
                method();
        }
    }
    private void method() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new RightWayStopThread2Test());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}

```

运行结果：

![1585624494571](https://img-blog.csdnimg.cn/2020033113594577.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

==不能屏蔽中断==

### 3. 应中断的方法

+ Object.wait()
+ Thread.sleep()
+ Thread.join()
+ JUC.BlockingQueue.take()/put(E)
+ JUC.locks.Lock.lockInterruptibly()
+ JUC.CountDownLatch.await()
+ JUC.CyclicBarrier.await()
+ JUC.Exchange.exchange(V)

### 4. 错误的停止方法

1. 被弃用的stop()、suspend()和resume()。

   stop()方法会直接中断线程，不管在执行什么程序,会结束所有未结束的方法。

   suspend()方法会将线程挂起，且不会释放锁，容易造成死锁。

   resume()方法是将线程唤醒，但是也不会释放锁，容易造成死锁。

2. 用volatile设置Boolean标志位

### 5. 如何分析native方法

1. 进GitHub（open JDK）
   + 点搜索文件，搜索对应的c代码类
   + 找到native方法对应的方法名





