## Java并发（七）--线程各属性

### 目录

[TOC]

### 1. 属性概览

![](https://img-blog.csdnimg.cn/20200402100149361.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

### 2. 线程ID

每个线程都有自己的ID，用于标识不同的线程。

源码：

```java
/* Set thread ID */
    tid = nextThreadID();
	private static long threadSeqNumber;
    private static synchronized long nextThreadID() {
        return ++threadSeqNumber;
    }
```

线程ID通过synchronized关键字修饰的方法里，进行先++自增。所以主线程ID不是０而是１，而我们自己创建的线程也不是２，因为JVM会自动帮我们创建一些守护线程，来为我们的用户线程提供服务。

### 3. 线程名称

源码：

```java
	public Thread() {
        init(null, null, "Thread-" + nextThreadNum(), 0);
    }

	private static int threadInitNumber;
    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }
```



```java
public final synchronized void setName(String name) {
    checkAccess();
    if (name == null) {
        throw new NullPointerException("name cannot be null");
    }

    this.name = name;
    //这个判断线程是不是刚刚启动，如果不是，则不能修改native层的线程名字，
    //但是线程内部的Java名字可以修改。
    if (threadStatus != 0) {
        setNativeName(name);
    }
}
```

### 3. 守护线程

作用：给用户线程提供服务

```java
private boolean     daemon = false;
this.daemon = parent.isDaemon();
```

**守护线程的三个特性：**

+ 线程类型默认继承自父线程
+ 守护线程由JVM启动
+ 不影响JVM退出，JVM退出只看用户线程是否执行完成。

**守护线程和普通线程的区别：**

+ 整体无太大区别
+ 唯一区别在于是否影响JVM退出。

是否需要设置守护线程：不需要，因为把我们自己的线程设置为守护线程不安全，如果JVM看到只有守护线程，会自动关闭JVM，则会导致数据不一致等问题。

### 4. 线程优先级

```java
public final static int MIN_PRIORITY = 1;
public final static int NORM_PRIORITY = 5;
public final static int MAX_PRIORITY = 10;
```

在Java中，最小优先级为1，最大优先级为10，默认是5.

程序设计不应依赖于优先级：

+ 不同操作系统优先级不同
+ 优先级会被操作系统改变

