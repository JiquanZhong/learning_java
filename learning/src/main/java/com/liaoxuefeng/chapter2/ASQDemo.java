package com.liaoxuefeng.chapter2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ZHONG Jiquan
 * @create 05/08/2023 - 12:11
 */
public class ASQDemo {
	public static void main(String[] args) throws InterruptedException {
		/**
		 * 2.1.1 什么是AQS
		 * 2.1.2 如何理解AQS 的实现原理
		 */

		/**
		 * 回到什么是AQS问题中。
		 * AQS是一个用于构建同步器的基础框架，是java并发包的一部分，它是一个抽象类，需要由子类自行实现业务逻辑。
		 * 其核心两部分组成  1. 一个volatile修饰的state变量，作为竞争条件，表示当前的同步状态，其他线程通过对CAS操作对state进行原子性修改
		 * 				   2. 使用双向列表维护的FIFO线程等待队列
		 * 原理是，线程通过对共享变量state进行修改以实现竞技条件。如果失败则进入等待队列。
		 * 如果有抢占到竞技条件的线程释放资源后，队列中的线程会按照FIFO顺序被唤醒。
		 * AQS提供了两种资源共享方式：独占模式和共享模式
		 * 像ReentrantLock实现的就是独占模式，同一时刻只允许一个线程获取资源。
		 * 而像线程同步工具Semaphore和CountDownLatch实现的是共享模式
		 */

		/**
		 * 公平锁和非公平锁
		 * FairLock：多个线程按照它们的请求顺序来获取锁，即先来先得。
		 * 优点是可以避险饥饿现象（某个线程获取不到锁）。缺点是需要维护一个等待队列，所以有额外开销
		 * UnfairLock：多个线程获取锁的顺序是不确定的。当线程请求锁的时候，如果是空闲状态，则直接获取锁。如果锁正在被其他线程等待，则有机会先抢到锁，无需进入等待队列。
		 * 优点是性能较高，减少等待时间。但缺点是可能造成某些线程长时间无法获取锁
		 * 默认情况下，ReentrantLock和synchronized关键字是非公平锁。
		 * 但ReentrantLock可以构造时传入true实现公平锁
		 */
		//unfair lock
		ReentrantLock unfairLock = new ReentrantLock(false);
//		public synchronized(true) void someMethod(){
//			return;
//		}

		ReentrantLock fairLock = new ReentrantLock(true);

		/**
		 * CountDownLatch是一个线程同步工具，它主要作用是：让某些线程等待其他线程执行完毕后再继续执行。
		 * 其内部使用AQS实现，具体为：线程完成一定操作后，会让当前CountDownLatch计数器减一。当计数器为0时，会唤醒调用await而等待的线程。
		 * 使用场景为：主线程使用CountDownLatch等待其子进程的任务完成后，再执行统一的汇总操作。如资源和数据的加载。常用于驱动器的资源加载。
		 * 另外，如果一个CountDownLatch的计数器归0则无法再恢复。故CountDownLatch是不可重复使用的。
		 */
		int maxThreadNum = 3;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch endSignal = new CountDownLatch(maxThreadNum);
		for(int i = 0; i < maxThreadNum; i++) {
			// CountDownLatch内部AQS的线程具体执行顺序是不确定的
			// 由系统调度器决定
			new Thread(new Driver(i, endSignal)).start();
		}
		System.out.println("preparing..."); //前置准备工作
		startSignal.countDown();            //释放开始信号
		endSignal.await();                    //主线程等待子线程全部执行
		System.out.println("i'm waked now");//主线程被唤醒

		/**
		 * Semaphore用于多个线程对共享资源的访问控制。它运行多个线程同时访问某个共享资源，但限制访问的线程数量。
		 * 其内部维护一个计数器，表示可用的许可数量。
		 * 当计数器大于零，线程的访问请求会被批准，同时计数器减一；
		 * 当计数器等于零，请求访问的线程会被阻塞；
		 * 当访问线程任务执行完毕将许可释放后，Semaphore计数器加一。
		 */
		Semaphore semaphore = new Semaphore(3);
		for(int i = 0; i < 5; i++) {
			Thread thread = new Thread(new WorkerThread(semaphore));
			thread.start();
//			thread.join();
		}

		/**
		 * 2.1.3 AQS为什么要使用双向链表
		 */

		/**
		 * 1. 双向链表有头尾两个指针，等待队列需要频繁入队出队，头尾指针可以保证操作在O(1)时间完成
		 * 2. 如果没有竞争到锁并加入阻塞队列的前提是，前置节点的状态是正常的。避免了前置节点异常导致后续节点无法唤醒的问题
		 * 3. Lock接口有个lockInterruptibly方法，允许在等待队列中的线程被中断，该线程将不参与后续锁竞争。这时候需要把该节点状态设为cancelled并移出队列。
		 * 	  如果头从遍历，效率太慢。
		 * 4. 避免线程阻塞和唤醒的开销。刚加入链表的线程，会先尝试自旋的方式竞争锁，而按照公平锁的设计，只有head的下一节点才需要竞争锁。否则会造成羊群效应。
		 * 	  使用双向列表，可以判断prev是不是head，如果不是，则没有必要出发竞争锁的动作。
		 */

		/**
		 * 2.1.4 什么是CAS
		 */

		/**
		 * CAS是java中的Unsafe类的方法，全称叫CompareAndSwap。用于保证对数据修改的原子性。
		 * 如果对数据更新要保证原子性，需要加锁，但加锁会带来性能上的损耗。所以调用Unsafe中的compareAndSwap方法也能得到相同的目的
		 * 这些方法有四个参数，实例，字段偏移量，预期值，目标值
		 * CAS会比较目标字段是否等于预期值，是则修改为目标值，并返回true。
		 * 这是一个native方法，Java中不允许对内存直接操作，所以类名叫Unsafe。底层原理是调用该方法会增加一个lock指令对缓存或者总线程加锁，从而保证该操作的原子性
		 */
//		try {
//			Clazz c = new Clazz(1);
//			Unsafe.class.getDeclaredField("theUnsafe").setAccessible(true);
//			long stateOffset = Unsafe.getUnsafe().objectFieldOffset(Clazz.class.getDeclaredField("classID"));
//			Unsafe.getUnsafe().compareAndSwapInt(c, stateOffset, 1, 0);
//			System.out.println(c.classID);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		/**
		 * 应用场景：
		 * 1. JUC中的Atomic的原子实现，如AtomicInteger和AtomicLong
		 * 2. AQS，ConcurrentHashMap和ConcurrentLinkedQueue
		 */

		/**
		 * 2.1.5 什么是乐观锁，什么是悲观锁
		 */

		/**
		 * 悲观锁和乐观锁是锁的设计思想
		 * 乐观锁：操作数据时非常乐观，认为其他线程没有修改数据，不会上锁。但在写数据时，会进行CAS
		 * 悲观锁：操作数据时非常悲观，认为其他线程已经对数据进行了修改，故操作数据的时候会上锁，其他想获取该数据的线程就会被阻塞。JAVA API中的synchronized和ReentrantLock等独占锁都是悲观锁的实现
		 * 应用场景上，乐观锁用于写少读多的时候，可以减少阻塞和锁竞争的消耗。
		 * 悲观锁用于写多读少的时候。因为如果还是用乐观锁，就会导致不断尝试写入的次数，反而带来更大的消耗。
		 */

		/**
		 * 2.1.6 什么条件下会产生死锁，如何避免死锁
		 */

		/**
		 * 死锁，线程A持有锁a时尝试获取线程B的锁b，而线程B在持有锁b时也尝试获取线程A的锁a，此时就会造成两线程互相等待的状态从而无限阻塞下去。
		 * 死锁产生要满足四个条件：
		 * 1. 互斥条件：a和b都是独享锁
		 * 2. 请求和保持条件：A尝试取获取b锁时不释放a锁
		 * 3. 不可抢占条件：其他线程不能强行抢占A的锁a
		 * 4. 循环等待条件：A在等待B占有的资源，B在等待A占有的资源
		 * 死锁一旦出现只能通过外部干预解决，具体为重启服务或者Kill线程
		 * 避免的方案：
		 * 1. 所以我们要最大限度避免死锁的发生。以上条件1无法被破坏，这是互斥锁的基本约束
		 * 2. 对于请求和保持条件，我们可以尝试一次性申请完所有的资源，就不存在等待锁问题
		 * 3. 对于不可抢占条件，占用资源的线程如果进一步申请不到其他资源，可以主动释放
		 * 4. 对于循环等待条件，给资源标号，所有线程按序号申请共享资源，这样可避免循环等待条件
		 */

		/**
		 *	2.1.7 synchronized和Lock的区别是什么
		 */

		/**
		 * 1. 特性上：synchronized是关键字，而Lock是JUC下的一个接口，有很多实现类，如可重入锁
		 * 2. 用法上：synchronized可以用于方法和代码块，Lock则需要创建对象并调用lock和unlock方法。lock比较灵活，可以随时加锁放锁；另外Lock提供了非阻塞锁竞争方法tryLock。
		 * 3. 性能上：低竞争synchronized好，因为synchronized由JVM直接实现，不需要额外方法和资源管理。高竞争Lock好，因为Lock提供更细粒度的控制。
		 *    在实现上有区别。synchronized使用悲观锁机制，在java1.6后采用偏向锁，轻量级锁，重量级锁及锁升级的方式。
		 * 4. 用途上：复杂同步应用中，建议使用Lock，因为synchronized是非公平的，而Lock两种机制都有。
		 * 5. 释放：synchronized执行完代码块或者发生异常时会被动释放锁。Lock则必须手动在finally释放。
		 */

		/**
		 * 2.1.8 什么是可重入锁，它的作用是什么
		 */

		/**
		 * 线程如果抢到了互斥锁资源，如果在释放前再取竞争同一把锁，则不需要等待，直接计入重入次数。
		 * 在JUC中绝大多锁都是可重入的。而不可重入的读写锁StampedLock
		 * 锁的可重入性主要用于避免线程死锁问题。避免申请同一把锁时，自己等待自己释放锁的悖论
		 */

		/**
		 * 2.1.9 ReentrantLock的实现原理是什么
		 */

		/**
		 * ReentrantLock是一个悲观锁，同时也是独享锁，即最多被一个线程持有。
		 * 其实现原理如下：
		 * 1. 锁的竞争，ReentrantLock通过互斥变量，加上CAS机制实现的。具体为，其内部维护一个双向链表实现的AQS，当线程尝试获取锁，会对尝试使用CAS的方式修改锁状态(state)
		 * 	  成功则表示获取了锁，失败则加入AQS
		 * 2. 在线程释放锁后，会修改AQS的互斥变量state。而AQS链表头收到锁释放通知后，会让队列中的下一节点获取锁
		 * 3. ReentrantLock是可重入的，意味着线程如果持有锁后，如还需再次获取锁，可以不用排队直接获取，让状态变量+1
		 * 4. ReentrantLock有公平锁和非公平锁的两种实现，默认是非公平锁，公平锁只需要构造时传入true
		 * 5. 内部的AQS也实现了tryLock方法，使得它支持非阻塞锁的特性，避免了获取锁的开销
		 */

		/**
		 * 2.1.10 ReentrantLock是如何实现锁的公平性和非公平性的
		 */

		/**
		 * 公平锁：严格按照同步队列的出队顺序获取锁，如果队列不为空，则进入队列等待。性能较差，因为有线程的频繁切换。
		 * 非公平锁：不管队列有没有等待线程，新线程都先尝试获取锁，获取失败后再进入同步队列。减少了线程唤醒产生导致的内核态切换所带来的性能开销。
		 */

		/**
		 * 2.1.11 说说你对行锁，间隙锁，临键锁的理解
		 */

		/**
		 * 这三种锁都是Mysql的InnoDB引擎下解决事务隔离性的一系列排他锁。最终是解决幻读的问题
		 * 行锁：锁定单独的数据行，它只锁住指定的行，对其他的行无效。
		 * 间隙锁：锁定一个索引区间。由于索引是基于B+树结构存储的，所以默认是存在一个索引区间的。加入间隙锁后就无法对该区间数据修改。
		 * 临键锁：相当于行锁+间隙锁的组合。它锁定的范围即包含索引记录，也包括索引区间。
		 *
		 */

		/**
		 * 行锁：其中id为主键索引
		 * select * from 'test' where 'id' = 1 for update
		 * 间隙锁：锁定(5,7)开区间，不包括id=5和7这两行具体的数据
		 * select * from 'test' where 'id' between 5 and 7 for update
		 * 临键锁：锁定区间(10,11]
		 * select * from test where age = 11 for update
		 */

		/**
		 * 2.1.12 如何理解Java中令人眼花缭乱的各种并发锁
		 */

		/**
		 * 根据线程是否锁住资源分：乐观锁和悲观锁
		 *		悲观锁：只要读数据就一定上锁，别人总在改数据
		 *		乐观锁：每次读数据都不上锁，更新数据的时候使用原子操作CAS
		 * 根据多个线程是否共享一把锁：独占锁和共享锁（互斥锁ReadWriteLock）
		 * 		独占锁：锁一次只能被一个线程持有：写锁
		 * 		共享锁：锁可以被多个线程同时持有：读锁
		 * 多个线程竞争是否要排队：公平锁和非公平锁
		 * 		公平锁：只要队列不为空，就要进同步队列排队拿锁
		 * 		非公平锁：无论队列是否为空，先尝试抢占锁，失败则进入同步队列
		 * 一个线程能否多次获取同一把锁：可重入锁和不可重入锁
		 * 		可重入锁：一个线程在外层方法获取了锁，进入内层方法时会自动获取锁
		 * 		不可重入锁：统一线程在没释放锁前不能再次获取该锁
		 * 线程获取锁失败是否阻塞：自旋锁（自适应性自旋锁）和阻塞锁
		 * 		阻塞锁：获取锁失败后，线程会挂起
		 * 		自旋锁：获取锁失败后会进入忙循环，不挂起：如AtomicInteger中的会无限循环CAS，直到条件满足
		 * 		自适应性自旋锁：jkd1.6引入自适应自旋，自旋时间不固定，由前一次的自旋时间决定。成功率低则会跳过自旋
		 * 锁升级
		 * 		jkd1.6引入了四种锁的状态：无锁，偏向锁，轻量级锁，重量级锁
		 * 		无锁：只有一个线程能修改资源时，自动使用无锁，使用CAS，即乐观锁思想
		 * 		偏向锁：第一个线程访问加锁的资源自动获取锁，资源偏向第一个线程。因为锁的对象头的mark word中存了该线程的id。如果id一致则直接入锁。
		 * 		轻量级锁：当线程竞争比较激烈时，其他线程通过自旋的方式等待锁的释放，而不会被挂起
		 * 		重量级锁：即升级为互斥锁，其他线程都要被阻塞
		 * 锁再设计和锁优化
		 * 		锁优化：锁粗化和锁消除
		 * 			锁粗化：将多个代码块合并，把多次上锁解锁过程合并为一次请求
		 * 			锁消除：JVM在运行时如果检测到共享数据没有锁的竞争时，就会把锁消除。如栈空间内的StringBuffer会被JVM去掉锁。
		 * 		再设计：细化锁的粒度，不要锁住一大段代码块，按需上锁
		 */

		/**
		 * 2.1.13 阻塞队列被异步消费，怎么保持顺序
		 */

		/**
		 * 阻塞队列也满足FIFO的顺序。其次，在阻塞队列里可以使用两个锁和两个condition(ReentrantLock内维护等待队列)，生产者的和消费者的。
		 * 队列为空：消费线程需要进入消费者阻塞队列，直到condition不满足，它的有序性是按通过加锁保证的。
		 * 队列为满：生产线程需要进入生产者阻塞队列，直到condition不满足
		 */

		/**
		 * 2.1.14 基于数组的阻塞队列ArrayBlockingQueue的实现原理是什么
		 */

		/**
		 * ArrayBlockingQueue是基于数组实现阻塞队列，由于数组的长度是固定的，所以不能无限地往里面加入元素
		 * 当队列为空时，应挂起消费者线程；当队列满了时，应该挂起生产者线程。
		 * 具体实现如下：
		 * 其内部定义了一个ReentrantLock和两个该锁的派生条件：notEmpty和notFull
		 * 在阻塞队列的添加方法里，先循环判断队列大小是否等于数组大小。如果是，则需调用notFull.await方法：释放锁并唤醒消费线程消费。方法返回后跳出循环，队列肯定没满，故可正常添加元素
		 * 注意：condition.await方法，进入方法后，当前线程会释放锁对象，在await方法返回前又会重新获得锁对象。
		 * 在阻塞队列的出队方法里，先循环判断队列大小是否为0。如果是，则调用notEmpty.await方法：唤醒生产者线程。
		 * 方法返回后队列大小肯定不为0
		 */
	}

	static class Driver implements Runnable {
		int id;
		CountDownLatch endSignal;

		public Driver(int id, CountDownLatch endSignal) {
			this.id = id;
			this.endSignal = endSignal;
		}

		@Override
		public void run() {
			doSomeWork();
			endSignal.countDown();
		}

		public void doSomeWork() {
			System.out.println(Thread.currentThread().getName() + " is doing his work");
		}
	}

	static class WorkerThread implements Runnable {
		Semaphore semaphore;

		public WorkerThread(Semaphore semaphore) {
			this.semaphore = semaphore;
		}

		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread() + " is trying to acquire a permit.");
				semaphore.acquire();
				System.out.println(Thread.currentThread() + " is working.");
				Thread.sleep(1000);
				System.out.println(Thread.currentThread() + " has released a permit.");
				semaphore.release();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Clazz {
		int classID;

		public Clazz(int classID) {
			this.classID = classID;
		}
	}
}
