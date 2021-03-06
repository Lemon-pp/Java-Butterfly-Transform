## Java并发--多线程概念的建立

### 1、什么是进程？

​		进程是程序的真正运行实例，是操作系统资源分配的基本单位。

### 2、什么是线程？

​		线程是CPU的基本调度单位，每个线程执行的都是进程代码的某个片段。

### 3、进程和线程的区别

  1. 概念不同
      	1. 进程是可以真正运行的程序实例，是操作系统资源分配的基本单位。线程是CPU的基本调度单位。

  2. 内存共享方式不同

     ​	进程之间一般操作系统有分配独立的内存，不同进程之间无法访问，只能通过进程间通信IPC。线程操作系统不会分配独立的内存，线程之间共享进程的内存。线程独立的有自己独立的堆栈。

   	1. 数量不同
   	2. 开销不同

相似点：生命周期

### 4、Java语言和多线程的渊源

​	即使不创建线程，在执行main方法的时候，JVM会自动创建其他线程。

 	1. Singal Dispatcher  //负责把操作系统发来的信号分发给适当的处理程序
 	2. Finalized    //负责对象的finalize（）方法
 	3. Reference Handler  //和GC、引用相关的线程
 	4. main   //主线程，用户程序的入口。

### 5、多线程

#### 5.1 什么是多线程？

​	如果一个程序允许两个或以上的线程，那么它就是多线程程序，多线程是指单个进程中运行多个线程。

#### 5.2 为什么使用多线程？

 + 最主要的目的是提高CPU利用率
 + 提高处理速度
 + 避免无效等待
 + 提高用户体验

#### 5.3 什么场景需要使用多线程？

 + 为了同时处理多件不同的事
 + 为了提高工作效率、处理能力。
+ 需要同时有很大并发量的时候。

#### 5.4 多线程带来的问题或局限

1. 性能问题：上下文切换带来的消耗
2. 线程安全问题：包括数据安全问题（i++总数不一致）以及线程带来的活跃性问题（线程饥饿、死锁）。

### 6 、串行、并行、并发

1. 串行：

   两个任务排队执行。

2. 并行：

   真正的同时运行在同一时刻，有多个任务同时执行。如在多核处理器上，有两个线程同时执行一段代码。

3. 并发：

   并发偏重于多个任务交替执行，多个任务有可能是串行的。v

### 7、高并发与多线程

高并发:代表一种状态，指在同一个时间点，有很多用户同时的访问同一 API 接口或者 Url 地址。

高并发指标：

+ QPS：每秒查询数
+ 带宽
+ PV（page view）
+ UV (Unique view)
+ 并发连接数
+ 服务器平均请求等待时间

### 8、同步与异步、阻塞与非阻塞

同步：同步方法调用一旦开始，调用者必须等到方法调用返回后，才能继续后续的行为。

异步：异步方法调用更像是一个消息传递，一旦开始，方法调用会立即返回，调用者就可以继续后续的操作。

阻塞：比如一个线程占用了临界区资源，其他线程需要等待导致线程挂起，这种叫做阻塞。

非阻塞：强调没有一个线程可以妨碍其他线程执行，所有的线程都会尝试不断向前执行。

### 9、临界区

临界区用来表示一种公共资源或者说共享数据，可以被多个线程使用，但是一次只能有一个线程使用它，一旦临界区资源被占用，其他线程想要使用这个资源必须等待。

### 10、死锁、饥饿、活锁

死锁：指两个或两个以上的线程由于竞争资源而互相等待导致阻塞的现象。

饥饿：指一个或多个线程因为种种原因无法获得所要的资源，导致一直无法执行。

 原因：

1.优先级比较低，导致资源不断被高优先级的抢占。

 2.某个线程一直占用资源不放。

活锁：线程都主动将资源释放给其他线程使用，导致资源不断的在线程间跳动，导致没有一个线程可以真正同时拿到所有资源正常执行。