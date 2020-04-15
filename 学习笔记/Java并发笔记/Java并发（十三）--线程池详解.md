## Java并发（十三）--线程池详解

### 目录

[TOC]

### 一、线程池介绍

##### 1. 什么是线程池？

​	线程池也是一种多线程处理方式，处理过程中将任务提交到线程池，任务执行交由线程池来管理。

##### 2.创建线程池的原因

​	如果每个请求都创建一个线程去处理，服务器的资源很快别耗尽，使用线程池可减少创建线程的次数，避免反复创建并销毁线程带来的开销问题，每个工作线程都被重复利用，可执行多个任务。 

1. 反复创建线程开销大。
2. 过多的线程会占用太多内存。

##### 3.线程池的好处

+ 加快响应速度
+ 合理利用CPU资源
+ 统一管理线程

### 二、创建和停止线程池

##### 1.线程池构造函数的参数

![1586914315611](https://img-blog.csdnimg.cn/20200415153311683.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

corePoolSize：

​	是线程池中的核心线程数量，不会被回收。

maximumPoolSize:

​	线程不够用时线程池可以创建的最大线程数量。

workQueue：

​	阻塞队列，存储等待执行的任务，执行FIFO原则。

有三种常见的队列类型：

  + 直接交换队列: SynchronousQueue，内部没有容量，只是作为中转，所所以maximumPoolSize需要设置的大一些。
  + 无界队列：LinkedBlockingQueue，不会被塞满。maximumPoolSize设置多大也没有影响。但是如果任务处理不够快，容易造成内存浪费或者OOM。
  + 有界队列：ArrayBlockingQueue。

KeepAliveTime:

​	空闲线程的保留的时间。

unit：

​	KeepAliveTime的时间单位。

handler：

​	线程的饱和策略，一种拒绝策略，在任务满了以后，拒绝执行某些任务。

ThreadFactory：

​	创建线程的线程工厂。默认使用Executors.defaultThreadFactory(),创建的线程都在同一个线程组，拥有同样的优先级，都不是守护线程。如果自己指定ThreadFactory,那么就可以改变线程名、线程组、优先级、是否是守护线程。

源码：

```java
DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                          poolNumber.getAndIncrement() +
                         "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
```



**线程池中接收任务后的流程：**

1.当前线程数小于coreThreads核心线程数时，即时线程池中其他线程是空闲的，也会创建线程新线程来运行新任务。

2.当前线程数大于核心线程数小于max最大线程数时，会放入工作队列中，只有工作队列满了才会创建线程。

3.如果工作队列满了并且当前线程数大于或等于最大线程数，会采用Handler执行拒绝策略。

![1586914813003](https://img-blog.csdnimg.cn/20200415153054965.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

**增减线程的特点**：

1. 如果核心线程数等于最大线程数，则线程池的大小是固定的。
2. 线程池希望保持较少的线程数，只有在负载变得很大时才增加。
3. 通过设置maximumPoolSize为很高的值，例如Integer.MAX_value，可以允许线程池容纳任意数量的并发任务。
4.  只有在队列填满时才创建多余corePoolSize的线程，所以如果使用的是无界队列，例如LinkedBlockingQueue，那么线程数就不会超过corePoolSize。 

##### 2.常见的几种线程池以及应用场景

1.newSingleThreadExecutor

​	创建一个单线程化的线程池，唯一工作是执行任务，保证所有任务按照指定顺序执行。

```java
public class SingleThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Task());
        }
    }
}
```

源码：

```java
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
```

可以看到corePoolSize和maxPoolSize都是1，队列也是无界队列LinkedBlockingQueue，也会有请求堆积的问题。导致占用大量内存。

2.newFixedThreadPool

​	创建一个指定工作线程数量的线程池，可控制线程最大并发数，超出的线程在队列中等待。 

```java
public class FixedThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Task());
        }
    }
}
class Task implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}

```

源码:

```java
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
```

可以看到，corePoolSize和MaxPoolSize的值相等，不会有线程被回收，存活时间为0s,队列采用的是无界队列LinkedBlockingQueue。由于无界队列是没有容量的，导致请求数越来越多且处理不过来的时候，会造成占用大量内存，以及OOM的情况。

3.newCachedThreadPool

​	可缓存线程池，如果线程池超过处理需要，可灵活回收空闲线程，若无可回收则新建线程。

```java
public class CachedThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Task());
        }
    }
}
```

源码：

```java
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```

可以看到工作队列使用的SynchronousQueue，所以没有容量来存储线程，maxPoolSize为Integer.VALUE,这可能会创建数量非常多的线程，有可能导致OOM。

4.newScheduledThreadPool

​	创建一个支持定时或周期性任务执行的线程池。

```java
public class ScheduledThreadPoolTest {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        //scheduledExecutorService.schedule(new Task(),3,TimeUnit.SECONDS);  等待3秒执行任务
        //1秒后执行任务，然后每隔3秒重复执行。
        scheduledExecutorService.scheduleAtFixedRate(new Task(),1,3,TimeUnit.SECONDS);
    }
}
```

源码:

```java
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }
    
    
public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
              new DelayedWorkQueue());
    }
```

内部使用的是延迟队列。

JDK1.8新加的线程池：

workStealingPool:

特点：

​	1.子任务。

​	2.窃取。

应用场景：

newSingleThreadExecutor

​	适用于一个任务一个任务执行的场景。

newFixedThreadPool：

​	适用于执行长期的任务，性能好很多。

newCachedThreadPool

​	适用于执行很多短期异步的小程序或者负载较轻的服务器。

newScheduleThreadPool

​	适用于周期性执行任务的场景。

四种线程池构造函数参数：

![1586921435365](https://img-blog.csdnimg.cn/20200415153122837.png)

##### 3.线程池的线程数量如何设定

1.CPU密集型：线程数=按照核数或者核数+1设定。

2.IO密集型：线程数=CPU核数*（1+平均等待时间/平均工作时间）

##### 4.停止线程池

1.shutdown

等待线程池中执行的任务和等待队列中的任务执行完毕就停止，而在执行shutdown之后任新加入任务会报异常。

2.isShutdown

判断线程池是否进入停止状态。

3.isTerminated

判断整个线程是否都停止了。

4.awaitTermination

等待一段时间，线程停止了返回true。否则返回false。

5.shutdownNow

立刻停止线程。

```java
public class FixedThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Task());
        }
        Thread.sleep(1500);
        List<Runnable> runnableList = executorService.shutdownNow();
        
        //System.out.println(executorService.isShutdown());
        executorService.shutdown();
        boolean b = executorService.awaitTermination(12, TimeUnit.SECONDS);
        System.out.println(b);
        //System.out.println(executorService.isShutdown());
        //System.out.println(executorService.isTerminated());
        //executorService.execute(new Task());
        //Thread.sleep(10000);
        //System.out.println(executorService.isTerminated());
    }
}
class Task implements Runnable{

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
}
```

### 三、线程池拒绝策略

拒绝时机：

1.当Executor关闭时，提交新任务被拒绝。

2.以及当Executor对最大线程和工作队列容量使用有限边界并已经饱和时。

四种拒绝策略：

1.AbortPolice

​	直接抛出异常。默认策略。

2.DiscardPolice

​	抛弃当前任务.

3.DiscardOldestPolice

​	抛弃队列中最早的任务

4.CallerRunsPolice

​	用调用者所在线程来执行任务。

### 四、钩子方法

```java
/**
 * 描述：     演示每个任务执行前后放钩子函数
 */
public class PauseableThreadPool extends ThreadPoolExecutor {

    private final ReentrantLock lock = new ReentrantLock();
    private Condition unpaused = lock.newCondition();
    private boolean isPaused;


    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                               TimeUnit unit, BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                               TimeUnit unit, BlockingQueue<Runnable> workQueue,
                               RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                               TimeUnit unit, BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,
                handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        lock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void pause() {
        lock.lock();
        try {
            isPaused = true;
        } finally {
            lock.unlock();
        }
    }

    public void resume() {
        lock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PauseableThreadPool pauseableThreadPool = new PauseableThreadPool(10, 20, 10l,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("我被执行");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 10000; i++) {
            pauseableThreadPool.execute(runnable);
        }
        Thread.sleep(1500);
        pauseableThreadPool.pause();
        System.out.println("线程池被暂停了");
        Thread.sleep(1500);
        pauseableThreadPool.resume();
        System.out.println("线程池被恢复了");

    }
}

```



### 五、实现原理、源码分析

线程池组成部分：

1. 线程池管理器（创建线程等）
2. 工作线程
3. 任务队列
4. 任务接口（Task）

Executor家族：

![1586930768504](https://img-blog.csdnimg.cn/20200415153147473.png)

Executor: 顶层接口，只有一个方法，就是执行任务的。

```java
public interface Executor {
    void execute(Runnable command);
}
```

ExecutorService: 增加了一些初步管理线程池的方法。

```java
public interface ExecutorService extends Executor {
	void shutdown();
	List<Runnable> shutdownNow();
	...
	boolean isTerminated();
}
```

Executors: 工具类，帮助我们快速创建线程池用的。

```java
public class Executors {
    
	public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
    //省略
    
}  
```

ThreadPoolExecutor: 创建线程池。

自定义创建线程池demo：

```java
	public class CreateThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException, IOException {
        int corePoolSize = 2;
        int maximumPoolSize = 4;
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ThreadFactory threadFactory = new NameTreadFactory();
        RejectedExecutionHandler handler = new MyIgnorePolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
        executor.prestartAllCoreThreads(); // 预启动所有核心线程

        for (int i = 1; i <= 10; i++) {
            MyTask task = new MyTask(String.valueOf(i));
            executor.execute(task);
        }

        System.in.read(); //阻塞主线程
    }

    static class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            System.out.println(t.getName() + " has been created");
            return t;
        }
    }

    public static class MyIgnorePolicy implements RejectedExecutionHandler {

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            doLog(r, e);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.err.println( r.toString() + " rejected");
//          System.out.println("completedTaskCount: " + e.getCompletedTaskCount());
        }
    }

    static class MyTask implements Runnable {
        private String name;

        public MyTask(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "MyTask [name=" + name + "]";
        }
        @Override
        public void run() {
            try {
                System.out.println(this.toString() + " is running!");
                Thread.sleep(3000); //让任务执行慢点
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

```

原理：

+ 相同线程执行不同任务。

源码：

```java
if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        
        
 final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        w.unlock(); // allow interrupts
        boolean completedAbruptly = true;
        try {
            while (task != null || (task = getTask()) != null) {
                w.lock();
                if ((runStateAtLeast(ctl.get(), STOP) ||
                     (Thread.interrupted() &&
                      runStateAtLeast(ctl.get(), STOP))) &&
                    !wt.isInterrupted())
                    wt.interrupt();
                try {
                    beforeExecute(wt, task);
                    Throwable thrown = null;
                    try {
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    w.completedTasks++;
                    w.unlock();
                }
            }
            completedAbruptly = false;
        } finally {
            processWorkerExit(w, completedAbruptly);
        }
    }
```

首先检查工作线程是否小于corePoolSize,如果小于的话，就去增加worker,然后在worker类中，可以看到，首先先拿到一个任务，如果任务不为空就去运行任务。

