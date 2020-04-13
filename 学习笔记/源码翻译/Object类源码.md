## Object类源码

```java
package java.lang;

是类层次结构的根。每个类都有Object类作为超类。
public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    返回此{@code Object}的运行时类。
    返回的{@code Class}对象是由{@code锁定的对象表示类的静态同步}方法。
    public final native Class<?> getClass();

    返回对象的哈希代码值。这种方法是支持哈希表。
    两个对象相等，会产生相同的hashCode。
    对象不相等，则会产生不同的hashCode。
    public native int hashCode();

    指示其他对象是否“等于”此对象。
    通常需要重写{@code hashCode}方法。
    以便保持{@code hashCode}方法的一般约定，它声明相等的对象必须有相等的哈希代码。
    public boolean equals(Object obj) {
        return (this == obj);
    }

    创建并返回此对象的副本。
    protected native Object clone() throws CloneNotSupportedException;

    返回对象的字符串表示形式。
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    唤醒等待此对象的监视器。如果有线程正在等待此对象，则其中一个线程被选择来唤醒。
    选择是任意的。
    线程等待对象的通过调用{@code wait}方法之一进行监视。
    此方法只能由所有者线程调用这个物体的监视器。
    一次只能有一个线程拥有对象的监视器。
    public final native void notify();

    唤醒此对象监视器上等待的所有线程。
    此方法只能由所有者线程调用这个物体的监视器。
    public final native void notifyAll();

    使当前线程等待，
    此方法使当前线程把自己放在这个对象的等待集中，然后放弃此对象上的任何和所有同步声明。
    直到另一个线程调用notify()或notifyAll()
    或指定的时间已过或其他线程{@linkplain thread#interrupt（）中断}。
    然后从等待设置中删除线程，对象并重新启用线程调度。
   	当前线程必须拥有此对象的监视器。
    public final native void wait(long timeout) throws InterruptedException;

    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }

    public final void wait() throws InterruptedException {
        wait(0);
    }

    垃圾回收时由对象上的垃圾回收器调用确定不再有对该对象的引用。
    protected void finalize() throws Throwable { }
}

```

