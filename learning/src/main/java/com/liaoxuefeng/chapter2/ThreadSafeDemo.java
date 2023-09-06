package com.liaoxuefeng.chapter2;

/**
 * @author ZHONG Jiquan
 * @create 09/08/2023 - 02:19
 */
public class ThreadSafeDemo {
	public static String name = "jiquan";
	public static void main(String[] args) {
		/**
		 * 2.3.1 谈谈你对线程安全问题的理解
		 * 2.3.2 Java 保证线程安全的方式有哪些
		 */

		/**
		 * 简单来说，多线程环境下，线程安全问题即是：如果访问桶一个对象，不考虑这些线程在运行调度和交替执行，不做任何干预的情况下，调用该对象都可以
		 * 获得预期行为，我们就可以认为这个对象是线程安全的。
		 * 在多线程下保证线程安全无非就是保证对象访问的三个性质：原子性，有序性和可见性
		 * 原子性说的是当一线程执行某一系列指令，它应该是不可被中断的。如果被中断就会有前后结果不一致问题。
		 * 有序性说的是由于指令重排，cpu最终执行指令顺序跟编写的顺序不一样。比如在cpu调用io操作这种耗时的方法后，并不会等待其执行完，而是会继续执行后续的指令
		 * 可见性问题指的是在多线程环境下，对象的读和写可能发生在不同的线程里，就可能导致当前修改对其他线程不可见。导致可见性问题的原因很多：cpu三层缓存架构
		 * cpu指令重排，编译器指令重排等等
		 *
		 * Java保证线程安全有哪些方式：
		 * 1. 原子性：JDK提供了Atomic类让我们可以对对象进行原子性操作，这是基于CAS的。同时我们也可以使用synchronized保证对象的访问原子性。
		 * 2. 有序性：我们可以使用volatile防止对象的执行重排。
		 * 3. 可见性：也可以使用volatile保证对象的可见性。同时我们也可以使用加锁（lock 和 synchronized）的方式去实现对象的单独访问。
		 */

		/**
		 * volatile使用场景：
		 * 状态标志：比如说一个用于标识服务是否关机的布尔变量。
		 */

		/**
		 * 2.3.3 如何安全中断一个正在运行的线程
		 */

		/**
		 * Java中的线程，是对操作系统中内存的一个高层包装，最终线程的创建和停止都是要有操作系统自己去完成。
		 * JDK Thread类提供了一个stop方法可以马上停止线程，但是这个方法是不安全的。可能任务没有完成，突然地中断会导致运行错误。
		 * 在Thread中，还有一个成员变量是interrupted，并且埋类一个钩子方法isInterrupted。外部线程可以调用Thread.interrupt告知线程应该停止了。
		 * 而具体停止逻辑，由被告知的线程自行实现。也可以不理会外部的告知。
		 */
		System.out.println(Thread.currentThread().isInterrupted());

		/**
		 * 2.3.4 SimpleDateFormat是线程安全的吗
		 */

		/**
		 * 先说结论：它不是一个线程安全的类。SimpleDateFormat内部持有一个Calendar对象引用。当有多个线程操作这个Calendar对象引用时，就会出现
		 * 脏读现象。
		 * 避免线程不安全，可以：
		 * 1. SimpleDateFormat作为非全局变量
		 * 2. 使用ThreadLocal把它变成线程私有的对象
		 * 3. 使用SimpleDateFormat时加上同步锁
		 * 4. 使用其他线程安全的类如：LocalDateTimer, DateTimeFormatter
		 */

		/**
		 * 2.3.5 并发场景中，ThreadLocal会造成内存泄漏吗
		 */

		/**
		 * 会。
		 */

		/**
		 * ThreadLocal原理：
		 * 每个线程都持有一个ThreadLocalMap对象，这是一个类似HashMap的kv结构，维护类一个Entry类型的数组table。而它的Entry实现类WeakReference接口，
		 * 即他的键都是弱引用。即当外部没有对该ThreadLocal的强引用时，键（ThreadLocal）就会被自动回收。导致Key为null，此时如果外部没有其他的强引用
		 * 指向该Value，就会导致其无法被访问到（内存泄漏）。而ThreadLocalMap的生命周期是跟Thread一致，Thread的生命周期又很长，时间久了会导致很严重的
		 * 内存泄漏问题。
		 * (这里补充一下，弱引用一旦被发现，下次GC中就一定会被回收)
		 */

		/**
		 * 避免此问题，我们可以在每次调用get后，再调用remove方法移除该Entry对象（键和值都会被清理）。
		 */

		// 创建一个ThreadLocal，当前线程持有，用于存储共享变量name
		ThreadLocal<String> nameThreadLocal = new ThreadLocal<>();
		// 修改共享变量并存入ThreadLocal中
		nameThreadLocal.set("Hello, "+name);
		System.out.println(nameThreadLocal.get());
		nameThreadLocal.remove();
		System.out.println(nameThreadLocal.get());
		System.out.println(name);
	}
}
