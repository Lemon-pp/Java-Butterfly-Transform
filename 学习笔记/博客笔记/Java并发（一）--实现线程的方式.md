## Java并发（一）--实现线程的方式

### 目录

[TOC]

### 1. 官方声明实现多线程有两种方式。

#### 1.1 通过继承Thread类，重写Thread类的run方法

```java
public class ExtendsThreadTest extends Thread {
    @Override
    public void run() {
        System.out.println(2);
    }

    public static void main(String[] args) {
        new ExtendsThreadTest().start();
    }
}
```

#### 1.2 通过实现Runnable接口，把实例作为参数传递给Thread类

```java
public class RunnableTest implements Runnable{

    @Override
    public void run() {
        System.out.println(1);
    }

    public static void main(String[] args) {
        new Thread(new RunnableTest()).start();
    }
}
```

### 2. 两种方式对比

方法二更好：

1. 具体执行的任务run方法与Thread类耦合性更低。
2. 节约资源，使用第一种方法每次都要创建、销毁线程，使用Runnable方式利于后面使用线程池来节约系统资源。
3. Java单继承原因，使用继承Thread类之后就不能继承其他类，扩展性降低。

本质区别：

源码中：

```java
@Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
```

实现Runnable传入一个Runnable实例参数到构造函数。

方法一是重写整个run()方法，

方法二是最终调用target.run()方法。

### 3. 两种方法一起使用的结果是什么？

```java
public class CreateThreadBothTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(2);
            }
        }){
            @Override
            public void run(){
                System.out.println(1);
            }
        }.start();
    }
}
```

Runnable实例虽然传入，但是整个run方法被重写，所以只会输出下面的1.

==总结==：

​	通常说实现多线程的方法有两种，Oracle官方也是这么声明，

​	准确的来说，创建线程只有一种方法就是构造Thread类，而实现线程的执行单元有两种方式：

 	1. 通过继承Thread类，重写Thread类的run方法
 	2. 通过实现Runnable接口，把实例作为参数传递给Thread类

### 4. 错误说法

1. 线程池

   ​	底层也是通过new Thread来实现。不能成为一种创建线程的方法。

2. 通过Callable和FutureTask创建线程

   ​	底层也是通过实现Runnable接口来实现。不能成为一种创建线程的方法。

3. 定时器

4. 匿名内部类

5. Lambda表达式

### 5.学习编程的途径

宏观上：

1. 不能单看工作年限
2. 要有责任心，不放过任何bug，找到原因并解决
3. 主动，不断重构、优化、学习、总结。
4. 敢于挑战技术难题，不要畏惧难点、没有接触的点。

微观上：

1. 看经典书籍
2. 看官方文档
3. 英文搜Google和StackOverflow
4. 多动手实践，多写demo，尝试用到项目中。
5. 学习开源项目，多分析源码。

### 6. 如何在业务开发中成长

1. 在业务中沉淀，了解业务模型
2. 在技术中深入