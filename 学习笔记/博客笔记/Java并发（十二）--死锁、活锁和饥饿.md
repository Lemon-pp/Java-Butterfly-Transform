## 				Java并发（十二）--死锁、活锁和饥饿

### 目录

[TOC]

### 1. 死锁概念以及危害

​      两个或者更多的线程因竞争资源而造成相互等待的情况，叫做死锁。 

![1586510295018](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586510295018.png)

死锁的影响：

死锁的影响在不同系统中是不一样的，取决于系统对死锁 的处理能力。

+ 数据库中：检测并放弃事务
+ JVM：无法自动处理

死锁的4个必要条件：

1. 互斥条件
2. 请求与保持条件
3. 不剥夺条件
4. 循环等待条件

### 2. 死锁例子

```java
package com.lemon.practice.demo;

public class DiedLockDemo {
    private static Object A = new Obeject();
    private static Object B = new Obeject();

    public static void main(String[] args) {
        DiedLockDemo.diedLockTest();
    }

    private static void diedLockTest() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A){
                    try {
                        Thread.currentThread().sleep(2000);//等待2000ms等线程T2锁定B
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B){
                        System.out.println(1);
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    try {
                        Thread.currentThread().sleep(2000);//等待2000ms等线程T1锁定A
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (A){
                        System.out.println(2);
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}

```



### 3. 死锁排除及分析方法

#### 3.1 jstack方法

1.在命令提示符CMD中输入jps -l，找到当前Java进程的进程号pid,通过包路径很容易区分出自己开发的程序进程。　

2.使用jstack -l pid,查看当前进程的错误信息。

![1585123359526](https://img-blog.csdnimg.cn/20200325161642208.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

#### 3.2 JConsole方法

1.使用win+R 打开运行对话框，输入JConsole

![1585123493836](https://img-blog.csdnimg.cn/20200325161659824.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

2.连接自己要选择的进程。

3.检测死锁。

![1585123556011](https://img-blog.csdnimg.cn/20200325161715363.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMDg5OTA3,size_16,color_FFFFFF,t_70)

### 4. 死锁修复策略

1. **避免策略**：哲学家就餐的换手方案。

   思路:避免相反的获取锁的顺序。比如转账时为了避免死锁，可以采用hashcode来决定获取锁的顺序，冲突时再添加其他操作。

   哲学家就餐问题：

   ![1586510590498](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586510590498.png)

   哲学家就餐问题可以这样表述，假设有五位哲学家围坐在一张圆形餐桌旁，做以下两件事情之一：吃饭，或者思考。吃东西的时候，他们就停止思考，思考的时候也停止吃东西。餐桌中间有一大碗意大利面，每两个哲学家之间有一只餐叉。因为用一只餐叉很难吃到意大利面，所以假设哲学家必须用两只餐叉吃东西。他们只能使用自己左右手边的那两只餐叉。哲学家就餐问题有时也用米饭和筷子而不是意大利面和餐叉来描述，因为很明显，吃米饭必须用两根筷子。

   哲学家从来不交谈，这就很危险，==可能产生死锁==，每个哲学家都拿着左手的餐叉，永远都在等右边的餐叉（或者相反）。即使没有死锁，==也有可能发生资源耗尽==。例如，假设规定当哲学家等待另一只餐叉超过五分钟后就放下自己手里的那一只餐叉，并且再等五分钟后进行下一次尝试。这个策略消除了死锁（系统总会进入到下一个状态），但仍然有可能发生“[活锁](https://baike.baidu.com/item/%E6%B4%BB%E9%94%81)”。如果五位哲学家在完全相同的时刻进入餐厅，并同时拿起左边的餐叉，那么这些哲学家就会等待五分钟，同时放下手中的餐叉，再等五分钟，又同时拿起这些餐叉。

   流程：

    	1. 先拿起左手的筷子
    	2. 然后拿起右手的筷子
    	3. 如果筷子被人使用了，等待别人用完
    	4. 吃完，把筷子放回原位

   代码：

   ```java
   /**
    * @author zhoup
    * @date 2020/4/10 17:36
    * @describe
    */
   public class PhilosopherTest {
       static class Philosopher implements Runnable {
           private Object leftChopstick;
           private Object rightChopstick;
   
           public Philosopher(Object leftChopstick, Object rightChopstick) {
               this.leftChopstick = leftChopstick;
               this.rightChopstick = rightChopstick;
           }
   
           @Override
           public void run() {
               try {
                   while (true) {
                       doAction("think;");
                       synchronized (leftChopstick) {
                           doAction("get left;");
                           synchronized (rightChopstick) {
                               doAction("get right;");
                           }
                           doAction("put down left and right");
                       }
                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
   
           }
   
           private void doAction(String action) throws InterruptedException {
               System.out.println(Thread.currentThread().getName() + " " + action);
               Thread.sleep((long) (Math.random() * 10));
           }
       }
   
       public static void main(String[] args) {
           Philosopher[] philosophers = new Philosopher[5];
           Object[] chopsticks = new Object[philosophers.length];
           for (int i = 0; i < chopsticks.length; i++) {
               chopsticks[i] = new Object();
           }
           for (int i = 0; i < philosophers.length; i++) {
               Object leftchopstick = chopsticks[i];
               Object rightchopstick = chopsticks[(i+1) % chopsticks.length];
               philosophers[i] = new Philosopher(leftchopstick , rightchopstick);
               new Thread(philosophers[i], "哲学家"+ (i+1) +"号").start();
           }
       }
   }
   
   ```

   运行结果：

   ![1586512736810](C:\Users\dell\AppData\Roaming\Typora\typora-user-images\1586512736810.png)

   所有哲学家都在拿到了左边的筷子，都在等待右边的筷子而陷入了死锁。

   解决方案：

   1. 服务员检查（避免策略）

   2. 改变一个哲学家拿叉子的顺序（避免策略）

      ```java
      if(i == philosophers.length-1){
                      philosophers[i] = new Philosopher(rightchopstick , leftchopstick);
                  }else {
                      philosophers[i] = new Philosopher(leftchopstick , rightchopstick);
                  }
      ```

   3. 餐票（避免策略）

   4. 领导调节（检测和恢复策略）

   

2. **检测与恢复策略**：一段时间检测是否有死锁，如果有就剥夺某一个资源，来打开死锁。

   检测算法：锁的调用链路图。

    + 允许发生死锁
    + 每次调用锁都记录
    + 定期检查“锁的调用链路图”中是否存在环路。
    + 一旦发生死锁，就用死锁恢复机制进行恢复。

   恢复方法：

    	1. 逐个终止进程，知道死锁消除。
    	2. 资源抢占，把已经分发出去的锁给收回来或者让线程回退几步。

3. **鸵鸟策略**：如果发生死锁的概率极其低，那我们就直接忽略它，直到死锁发生的时候，再人工修复。

### 5. 实际项目中避免死锁方法

1. 设置超时时间：Lock的tryLock(long timeout, TimeUnit timeUnit),如果超时获取锁失败就进行日志打印、警告、重启等操作。

   ```java
   /**
    * @author zhoup
    * @date 2020/4/13 10:15
    * @describe
    */
   public class TryLockDiedLock {
       private static Lock lock1 = new ReentrantLock();
       private static Lock lock2 = new ReentrantLock();
   
       public static void main(String[] args) {
           TryLockDiedLock.diedLockTest();
       }
   
       private static void diedLockTest() {
           Thread t1 = new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       if (lock1.tryLock(800, TimeUnit.MILLISECONDS)) {
                           System.out.println("线程1获取到了锁1");
                           Thread.sleep(new Random().nextInt(1000));
                           if (lock2.tryLock(800, TimeUnit.MILLISECONDS)) {
                               System.out.println("线程1获取到了锁2");
                               System.out.println("线程1成功获取到了两把锁");
                               lock2.unlock();
                               lock1.unlock();
                           } else {
                               System.out.println("线程1尝试获取锁2失败，已重试");
                               lock1.unlock();
                               Thread.sleep(new Random().nextInt(1000));
                           }
                       } else {
                           System.out.println("线程1获取锁1失败，已重试");
                       }
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           });
           Thread t2 = new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       if (lock2.tryLock(3000, TimeUnit.MILLISECONDS)) {
                           System.out.println("线程2获取到了锁2");
   
                           Thread.sleep(new Random().nextInt(1000));
                           if (lock1.tryLock(3000, TimeUnit.MILLISECONDS)) {
                               System.out.println("线程2获取到了锁1");
                               System.out.println("线程2成功获取到了两把锁");
                               lock1.unlock();
                               lock2.unlock();
                           } else {
                               System.out.println("线程2尝试获取锁1失败，已重试");
                               lock2.unlock();
                               Thread.sleep(new Random().nextInt(1000));
                           }
                       } else {
                           System.out.println("线程2获取锁2失败，已重试");
                       }
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           });
           t1.start();
           t2.start();
       }
   }
   ```

2. 多使用并发类。

3. 尽量降低锁的使用粒度：用不同的锁而不是一个锁。

4. 如果可以使用同步代码块，就不使用同步方法。

5. 给线程取名清晰一点，比较好排查。

6. 避免锁的嵌套。

### 6. 活锁

​	虽然线程没有阻塞，也始终在运行，但是程序得不到进展，因为线程始终重复做同样的事。一直谦让，导致资源一直在线程间跳动。

解决活锁的办法：

 1.  以太网的指数退避算法

 2.  **加入随机因素**

     ```java
     package com.lemon.practice.jmm;
     
     import java.util.Random;
     
     /**
      * 描述：     演示活锁问题
      */
     public class LiveLock {
     
         static class Spoon {  //勺
     
             private Diner owner;
     
             public Spoon(Diner owner) {
                 this.owner = owner;
             }
     
             public Diner getOwner() {
                 return owner;
             }
     
             public void setOwner(Diner owner) {
                 this.owner = owner;
             }
     
             public synchronized void use() {
                 System.out.printf("%s吃完了!", owner.name);
     
     
             }
         }
     
         static class Diner {
     
             private String name;
             private boolean isHungry;
     
             public Diner(String name) {
                 this.name = name;
                 isHungry = true;
             }
     
             public void eatWith(Spoon spoon, Diner spouse) {
                 while (isHungry) {
                     if (spoon.owner != this) {
                         try {
                             Thread.sleep(1);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         continue;
                     }
                     Random random = new Random();
                     if (spouse.isHungry && random.nextInt(10) < 9) {
                         System.out.println(name + ": 亲爱的" + spouse.name + "你先吃吧");
                         spoon.setOwner(spouse);
                         continue;
                     }
     
                     spoon.use();
                     isHungry = false;
                     System.out.println(name + ": 我吃完了");
                     spoon.setOwner(spouse);
     
                 }
             }
         }
     
     
         public static void main(String[] args) {
             Diner husband = new Diner("牛郎");
             Diner wife = new Diner("织女");
     
             Spoon spoon = new Spoon(husband);
     
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     husband.eatWith(spoon, wife);
                 }
             }).start();
     
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     wife.eatWith(spoon, husband);
                 }
             }).start();
         }
     }
     
     ```

### 7. 饥饿

当线程需要某些资源比如CPU，却始终得不到。

原因：

线程优先级设置过低，或者有线程持有锁同时又无限循环从而不释放锁，或者某程序始终占用某文件的写锁。