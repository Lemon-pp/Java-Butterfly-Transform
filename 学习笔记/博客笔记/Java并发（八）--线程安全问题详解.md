## Java并发（八）--线程未捕获异常处理详解

### 目录

[TOC]

### 1. 为什么需要UncaughtExceptionHandler?

**1.主线程可以轻松发现异常，而子线程不可以。**

~~~java
/**
 * @author zhoup
 * @date 2020/4/7 14:15
 * @describe
 */
public class ExceptionChildThread implements Runnable{

    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        new Thread(new ExceptionChildThread()).start();
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }
}
~~~

在上面代码中，子线程抛出异常，但是主线程却丝毫不受影响。

**2.子线程异常无法用传统方法捕获**

~~~java
/**
 * @author zhoup
 * @date 2020/4/7 14:20
 * @describe 1. 不加try catch抛出4个异常，都带线程名字
 * 2. 加了try catch,期望捕获到第一个线程的异常，线程234不应该运行，希望看到打印出Caught Exception
 * 3. 执行时发现，根本没有Caught Exception，线程234依然运行并且抛出异常
 * 说明线程的异常不能用传统方法捕获
 */
public class ChildeThreadTryCatch implements Runnable{

    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        try {
            new Thread(new ChildeThreadTryCatch(),"Thread-1").start();
            new Thread(new ChildeThreadTryCatch(),"Thread-2").start();
            new Thread(new ChildeThreadTryCatch(),"Thread-3").start();
            new Thread(new ChildeThreadTryCatch(),"Thread-4").start();
        }catch (RuntimeException e){
            System.out.println("Caught Exception");
        }
    }
}
~~~

+    1.不加try catch抛出4个异常，都带线程名字

 * 2. 加了try catch,期望捕获到第一个线程的异常，线程234不应该运行，希望看到打印出Caught Exception
 * 3. 执行时发现，根本没有Caught Exception，线程234依然运行并且抛出异常
 * 说明线程的异常不能用传统方法捕获

**3. 提高健壮性**

### 2. 两种解决方案

1.直接在run()方法中进行Try Catch操作

缺点：需要在每一个run方法里都进行Try Catch操作，而且可能还不知道抛出的异常类型。

2.使用UncaughtExceptionHandler

源码：

```java
public interface UncaughtExceptionHandler {
        /**
         * Method invoked when the given thread terminates due to the
         * given uncaught exception.
         * <p>Any exception thrown by this method will be ignored by the
         * Java Virtual Machine.
         * @param t the thread
         * @param e the exception
         */
        void uncaughtException(Thread t, Throwable e);
    }
```

异常处理调用策略：

```java
public void uncaughtException(Thread t, Throwable e) {
    //parent默认也为空
        if (parent != null) {
            parent.uncaughtException(t, e);
        } else {
            Thread.UncaughtExceptionHandler ueh =
                Thread.getDefaultUncaughtExceptionHandler();
            if (ueh != null) {
                ueh.uncaughtException(t, e);
            } else if (!(e instanceof ThreadDeath)) {
                System.err.print("Exception in thread \""
                                 + t.getName() + "\" ");
                e.printStackTrace(System.err);
            }
        }
    }
```

自己实现：

```java
/**
 * 描述：     自己的MyUncaughtExceptionHanlder
 */
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String name;

    public MyUncaughtExceptionHandler(String name) {
        this.name = name;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.WARNING, "线程异常，终止啦" + t.getName());
        System.out.println(name + "捕获了异常" + t.getName() + "异常");
    }
}

```

```java
/**
 * 描述：     使用刚才自己写的UncaughtExceptionHandler
 */
public class UseOwnUncaughtExceptionHandler implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler("捕获器1"));

        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-1").start();
        Thread.sleep(300);
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-2").start();
        Thread.sleep(300);
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-3").start();
        Thread.sleep(300);
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-4").start();
    }


    @Override
    public void run() {
        throw new RuntimeException();
    }
}

```

运行结果：

![1586242769075](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586242769075.png)

可以看到，子线程的异常信息通过自己实现的UncaughtExceptionHandler通过日志形式打印出来了。

