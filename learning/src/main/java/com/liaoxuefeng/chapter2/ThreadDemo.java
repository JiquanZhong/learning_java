package com.liaoxuefeng.chapter2;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author ZHONG Jiquan
 * @create 06/08/2023 - 15:09
 */
public class ThreadDemo {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		/**
		 * 2.2.1 Thread和Runnable的区别是什么
		 */

		/**
		 * 它们都是用于创建新的线程。Thread是一个类，Runnable是一个接口。Thread实现了类Runnable。
		 * 从抽象角度来讲，类代表着某类事物的抽象，而接口定义某一行为的规范。
		 * Thread类表示的就是线程本身，而Runnable抽象的是往线程添加的任务。
		 */
		Thread hello_world = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + ": Hello World");
			}
		});

		/**
		 * 2.2.2 什么是守护线程，它有什么特点
		 */

		/**
		 * 守护线程是一种专门为用户线程服务的线程，随着JVM的关闭守护线程才会关闭。如JVM中的GC线程就是一种守护线程。
		 * 设置守护线程的方式很简单，只需要创建一个线程，并调用setDaemon(true)
		 * 但守护线程不能用于线程池或者IO任务的场景里，因为一旦JVM关闭，守护线程也会退出，导致它们持有的资源没有释放
		 */
//		hello_world.setDaemon(true);
		hello_world.start();

		/**
		 * 2.2.3 BLOCKED和WAITING两种线程状态有什么区别
		 */

		/**
		 * 它们都表示着线程处于阻塞的状态，但是造成阻塞的原因却不一样。
		 * BLOCKED表示：线程尝试获取锁失败后，获取失败进入的状态
		 * WAITING表示：线程主动调用特定方法进入WAITING状态，以等待其他线程的任务完成。可以使用Object.wait和Thread.join
		 * 同时，wait和notify方法一定要在同步代码块或同步方法中使用，否则会报错。调用之后，线程会自动释放持有的锁。在方法返回后，线程又会获取锁。
		 * 可以notify和notifyAll来唤醒WAITING的线程
		 * 总结：它们都表示线程的阻塞状态，它们不同的原因是WAITING由人主动进入，而BLOCKED竞争锁失败后被动进入
		 * 		BLOCKED的唤醒是自动触发的，而WAITING需要特定方法触发
		 */

		/**
		 * 2.2.4 为什么启动线程不能直接调用run()方法，调用两次start()方法会有什么后果
		 */

		/**
		 * 直接调用Run方法会导致调用方去执行run方法，而调用start才会创建新线程并让其执行run方法
		 * 在Java中，调用两次start方法会抛出IllegalStateException。因为从NEW到RUNNABLE是单向的，不能从RUNNABLE到NEW
		 */
		hello_world.run();

		/**
		 * 2.2.5 谈谈你对Java线程五种状态流转原理的理解
		 */

		/**
		 * Java线程有以下六种状态
		 */
		// 表示线程还没执行start()方法：Thread state for a thread which has not yet started.
		Thread.State aNew = Thread.State.NEW;
		// 表示线程正在执行，也有可能在等待OS分配处理器资源
		// Thread state for a runnable thread. A thread in the runnable state is executing in the Java virtual machine
		// but it may be waiting for other resources from the operating system such as processor.
		Thread.State aRunnable = Thread.State.RUNNABLE;
		// 表示线程正在等待一个监视器锁，以进入一个同步方法或者同步代码块
		// Thread state for a thread blocked waiting for a monitor lock. A thread in the blocked state is waiting for a
		// monitor lock to enter a synchronized block/method or reenter a synchronized block/method after calling Object.wait.

		/**
		 * 		线程正在等待其他线程执行特定动作。可以通过Object.wait，Thread.join和LockSupport.park进入该方法。
		 * 		要唤醒此状态需要其他线程调用	Object.notify()，Object.notifyAll()或
		 * 		Thread.State aBlocked = Thread.State.BLOCKED;
		 * 		Thread state for a waiting thread. A thread is in the waiting state due to calling one of the following methods:
		 * 		Object.wait with no timeout
		 * 		Thread.join with no timeout
		 * 		LockSupport.park
		 * 		A thread in the waiting state is waiting for another thread to perform a particular action.
		 * 		For example, a thread that has called Object.wait() on an object is waiting for another thread to call
		 * 		Object.notify() or Object.notifyAll() on that object. A thread that has called Thread.join() is waiting
		 * 		for a specified thread to terminate.
		 */
		Thread.State aWaiting = Thread.State.WAITING;
		// 线程处于计时等待状态，计时完成后会苏醒进入RUNNABLE状态。如果获取锁失败则会进入BLOCKED
		// Thread state for a waiting thread with a specified waiting time. A thread is in the timed waiting state due
		// to calling one of the following methods with a specified positive waiting time:
		Thread.State aTimedWaiting = Thread.State.TIMED_WAITING;
		// 线程完成了执行的任务
		// Thread state for a terminated thread. The thread has completed execution
		Thread.State aTerminated = Thread.State.TERMINATED;

		/**
		 * JDK提供的三种自定义线程的方式：
		 * 1. 继承Thread并重写run方法
		 * 2. 实现Runnable接口
		 * 3. 实现Callable接口，这是一个范型接口，T等于返回值类型。可以用于接收新线程的执行结果。
		 */

		/**
		 * Callable任务需要提交给ExecutorService执行
		 */
		MyCallable myCallable = new MyCallable();
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		Future<String> res = threadPool.submit(myCallable);
		System.out.println("start waiting for the result");
		System.out.println(res.get());
		// 如果不调用此方法，则线程会被挂起
		threadPool.shutdown();

		/**
		 * 2.2.6 谈谈你对线程池的理解
		 * 2.2.7 Java有哪些实现线程池的方式
		 */

		/**
		 * Params:
		 * corePoolSize – the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set
		 * maximumPoolSize – the maximum number of threads to allow in the pool
		 * keepAliveTime – when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
		 * unit – the time unit for the keepAliveTime argument
		 * workQueue – the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
		 * 线程池是一种线程的池化技术，以达到资源复用的目的。常用池化技术还有连接池和内存池
		 * 线程的创建和销毁会带来频繁的cpu上下文切换，内存分配。性能开销很大。
		 * 线程池本身有参数去控制线程的创建数量，以避免线程无休止地创建所带来的cpu利用率过高的问题
		 * 原理：线程池一般有两个参数，核心线程数和最大线程数。核心线程一旦创建哪怕没有任务也不会被销毁，除非设置allowCoreThreadTimeOut为true
		 * 		最大线程数决定当核心线程被占用，还能创建新线程的最大数量（包括了核心线程）
		 * 		还有其他的辅助参数如最大存活时间和阻塞队列，拒绝策略。
		 * 		最大存活时间：决定非核心线程能空闲的最大时间，超出则会被回收
		 * 		工作队列：需要传入阻塞队列的具体实现
		 * 		拒绝策略：在ThreadPoolExecutor中定义了一些默认的拒绝策略，可以选择。或者自己定制拒绝策略。
		 */

		/**
		 * 线程池的参数如何设置
		 * 取决于具体的任务类型。
		 * 如果是计算密集型，一般是CPU数量+1。因为如果线程池数太大会频繁进行上下文切换和任务调度。
		 * 如果是IO密集型，一般是cpu数*2
		 * 如果是混合型，可以考虑根据情况将它们拆分成IO密集型和计算密集型。如果执行时间相差不大，可考虑拆分，这有利于提高吞吐量
		 */

		/**
		 * 线程池有哪几种状态：
		 * private static final int RUNNING    = -1 << COUNT_BITS;
		 * private static final int SHUTDOWN   =  0 << COUNT_BITS;
		 * private static final int STOP       =  1 << COUNT_BITS;
		 * private static final int TIDYING    =  2 << COUNT_BITS;
		 * private static final int TERMINATED =  3 << COUNT_BITS;
		 * RUNNING：该状态的线程池会接收新任务，并处理阻塞队列中的任务;
		 * SHUTDOWN：该状态的线程池不会接收新任务，但会处理阻塞队列中的任务；该状态由RUNNING调用shutdown进入
		 * STOP：该状态的线程不会接收新任务，也不会处理阻塞队列中的任务，而且会中断正在运行的任务；该状态由RUNNING调用shutdownNow进入
		 * TIDYING：该状态表明所有的任务已经运行终止，记录的任务数量为0；该状态由SHUTDOWN和STOP的状态转化而来，即满足队列为空且工作线程为0时
		 * TERMINATED：该状态表示线程池彻底终止
		 */

		/**
		 * 	ThreadPoolExecutor和Executors的区别：
		 * 	ThreadPoolExecutor是线程池的底层实现，提供非常丰富的参数选项以及常用线程池类型实现
		 * 	Executors是对ThreadPoolExecutor的封装，是一个工具类，简化了线程池的配置，但不够灵活。
		 */

		/**
		 * 为什么不建议使用Executors创建线程池：
		 * 阿里巴巴《Java开发手册》里禁止使用这种方式来创建线程池。
		 * https://juejin.cn/post/7064161230885224462
		 * Executors是JDK提供的线程池工具类，封装了五种基本线程池类型的创建方法。但并不被推荐使用Executors创建线程池，原因有几点：
		 * 1. 拿newFixedThreadPool为例，其也是调用new ThreadPoolExecutor并传入无界队列，它把LinkedBlockingQueue的最大长度设为了Integer.MAX_VALUE
		 *    在这种情况下，如果任务不断堆积，最终肯定会在内存堆积造成OOM
		 * 2. 另外在出现问题时无法快速定位到问题出现的线程池。
		 * 	  使用Executors创建的线程池通常是匿名的。假设现在有两个ExecutorService，如果线程池发生错误，根据错误堆栈信息，是无法区分由Executors创建的线程池的。
		 *    但如果使用自定义的ThreadFactory，就可以正确命名线程信息。但是在使用Executors时，很多人不设置自定义的ThreadFactory，导致不能判断错误线线程的归属。
		 */
		Executors.newFixedThreadPool(1).shutdown();


		/**
		 * 常用拒绝策略：
		 * AbortPolicy：直接抛出异常，默认使用此策略
		 * DiscardPolicy：直接忽略
		 * DiscardOldestPolicy：抛弃队头的任务，立刻执行新任务
		 * CallerRunsPolicy：让调用者自己执行该任务
		 * 如果需要自定义拒绝策略，需要实现RejectedExecutionHandler并实现rejectedExecution方法
		 */

		/**
		 * 常用的线程池
		 * JDK默认提供了五种线程池的实现方式：
		 * newFixedThreadPool:固定大小的线程池。核心线程数和最大线程数都是一样的。任务过多时，会放进阻塞队列
		 * CachedThreadPool:缓存线程池。核心线程数为0，最大线程为Integer.MAX_VALUE。其中线程的最大存活时间都是60s。用于处理大量短期的突发流量
		 * SingleThreadExecutor:单一线程池。只有一个核心线程且不能被扩容，线程空闲也不会被回收。可以保证任务都按照队列顺序执行
		 * ScheduledThreadPool:定时线程池。具有延时执行的线程池，可以用来实现定时调度
		 * WorkStealingPool:工作窃取线程池。在队列没有任务的时候，可以从其他线程池的任务队列拿任务执行。避免的线程池的空闲。常用于一些计算性的工作
		 */
//		Executors.newCachedThreadPool();

		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
//		threadPoolExecutor.prestartCoreThread();
		threadPoolExecutor.prestartAllCoreThreads();
		/**
		 * 2.2.8 线程池是如何回收线程的
		 */

		/**
		 * ThreadPoolExecutor中的getTask会使其周期性的从任务队列中拿任务。如果发现没有任务且有非核心线程空闲超时，则会将其移除工作队列
		 * 并同步将其销毁。
		 */

		/**
		 * 线程池的预热：
		 * 线程池的预热指的是线程池在创建之初就提前准备好一部分线程，避免需要处理任务时，还需要花时间创建线程的情况。
		 * 一般情况下，有两种预热场景：
		 * 服务器开机预热：在服务开启阶段，就创建好核心线程。这样服务开机就有处理并发请求的能力。ThreadPoolExecutor中的preStartCoreThread和
		 * preStartAllCoreThreads
		 * 业务高峰期预热：在服务高峰期前创建好所有的核心线程。以在服务高峰期能够更快地响应请求。
		 */

		/**
		 * 2.2.9 线程池时如何实现线程复用的
		 */

		/**
		 * 线程池是生产者消费者模型的经典实现。其内部维护一个阻塞队列用于添加新任务。
		 * 生产者不断将新的任务不断地添加进阻塞队列，消费者即工作线程不断从阻塞队列中获取任务执行。
		 * 当阻塞队列为空时，所有的工作线程都会被阻塞。当新的任务加入后，工作线程又回再次会唤醒。
		 */

		/**
		 * 2.2.10 线程池如何知道一个线程的任务已经执行完成
		 */

		/**
		 * 1. 使用isTerminated()判断线程池的运行状态，但这个方法只在shutDown或者shutDownNow之后使用。
		 * 2. submit方法可以传入Callable接口的实现并返回Future对象。Future中就包含了执行结果，如果调用Future.get，在任务没执行完的时候会一致被阻塞
		 * 3. 引入CountDownLatch计时器，即在提交的任务的run方法中加入计数为1的CountDownLatch计数器并调用await方法。如果任务没有执行完毕，所有调用了await方法的线程会一致被阻塞。
		 * 4. ThreadPoolExecutor还有一个钩子方法afterExecution，这是工作线程完成任务后会回调的方法。我们可以重写该方法获取线程的执行结果。
		 */
		// 2
		Future<Void> futureRes = threadPoolExecutor.submit(new FutureDemo());
		futureRes.get();

		// 3
		CountDownLatch cdl = new CountDownLatch(1);
		threadPoolExecutor.submit(new CountDownLatchDemo(cdl));
		cdl.await();//等待结束信号

		// 4
		AfterExecutionDemo tp = new AfterExecutionDemo(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
		tp.submit(() -> {
			System.out.println("AfterExecutionDemo starts");
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("AfterExecutionDemo ends");
		});

		// 1
		// shutdown不会马上关闭，需要等待最后一个任务执行完成才会关闭
		System.out.println("isTerminating: "+tp.isTerminating());
		threadPoolExecutor.shutdown();
		tp.shutdown();
		//调用shutdown方法后isTerminating会返回true
		if(tp.isTerminating()) System.out.println("tp 任务都结束了，正在关闭");

		Thread.sleep(3000);
		if(tp.isTerminated()) System.out.println("tp 已经关闭");

		/**
		 * 2.2.11 当任务超过线程池的核心线程数时，如果让任务不进入队列
		 */

		/**
		 * 线程池新任务提交后的运行原理：
		 * https://www.cnblogs.com/three-fighter/p/15565691.html
		 * 1. 执行execute()
		 * 2. 检查当前核心线程数是否满，没有满就创建新的核心线程并执行该任务
		 * 3. 如果核心线程数满了，则查看阻塞队列是否满，没满则加入阻塞队列排队
		 * 4. 如果阻塞队列满了，则查看当前线程数是否超过最大线程数，没有则创建非核心线程并执行
		 * 5. 如果非核心线程也满了，此时会执行拒绝策略
		 */

		/**
		 * execute和submit的区别
		 * execute无返回值，用于执行不需要返回值的任务。如Runnable实例
		 * submit有返回值，用于执行有返回值的任务，并会返回Future实例。如Callable实例
		 */

		/**
		 * 阻塞队列的类型：
		 * ArrayBlockingQueue:有界阻塞队列，使用数组实现，按FIFO排序
		 * LinkedBlockingQueue:基于链表的阻塞队列，可以设置容量，如果不设置，默认为Integer.MAX_VALUE。有OOM风险
		 * DelayQueue:延迟队列，是一个基于定时周期延迟执行的队列。根据延迟时间从小到大排序。如果延迟没有到期则poll返回的是null（因为取出操作一直被阻塞）
		 * PriorityBlockingQueue:具有优先级的无解阻塞队列，可自己实现优先级，最小的在队头
		 * SynchronousQueue:是一个不存储元素的阻塞队列，插入操作必须等待另一个移除操作的完成，否则会一直被阻塞。吞吐量高于无界队列，故缓存线程池使用的是这个队列。
		 */

		/**
		 * 当任务超过线程池的核心线程数时，如果让任务不进入队列:
		 * 我们使用SynchronousQueue。每产生一个任务都必须要只拍一个消费者来处理，否则就会阻塞生产者。
		 * 另外，还可以设置最大线程数为Integer.MAX_VALUE这样没有最大线程数限制。
		 */

		/**
		 * 2.2.12 什么是伪共享，如何避免伪共享
		 */

		/**
		 * 数据的更新操作都需要CPU先把数据读进cpu内存的，而为了更快的IO读取操作，OS设计了三级缓存，一次性会读取64字节作为一个缓存行。
		 * 第一第二级缓存都是每个cpu独占的，而第三级缓存是cpu会共享的一个区域。如果有多个cpu对同一缓存行的不同数据进行操作。其中一个
		 * cpu的更新会导致其他cpu的缓存失效，导致它们需要更新第一第二级缓存。这就是伪共享问题，看起来第三级缓存是大家共享的数据，实际
		 * 上每次更新都会造成别的cpu重新从内存读取。带来性能上很大的开销。
		 * 在Java中，为了避免伪共享问题，可以使用@Contented注解，被它标注的数据会独占一个缓存行。
		 */

		/**
		 *  2.2.13 wait和notify为什么要写synchronized代码块中
		 */

		/**
		 * 1. wait和notify用于多线程的协调工作，wait让线程进入阻塞状态，notify唤醒阻塞线程
		 * 2. 多线程具有并行执行任务的能力，同一时刻可以有多个线程同时进行。但对于它们共享的变量，就需要知道其他线程何时修改了变量。同时修改后
		 *    还需要通知在等待的线程。wait和notify就是用于这种场景。wait主动释放资源，让其他资源可以进行修改，notify用于通知其他线程对变量进行
		 *    下一步的修改。
		 * 3. 为了避免wait和synchronized的错误使用，JDK强制要求其必须写在同步代码块中。
		 */

		/**
		 * 2.2.14 wait和sleep是否会触发锁的释放及CPU资源的释放
		 */

		/**
		 * Object.wait会释放锁及cpu资源
		 * Thread.sleep不会释放锁，但会释放cpu资源
		 * 使用场景：
		 * wait用于当前线程任务已经完成，需要等待其他线程处理下一步的场景。所以wait必须释放锁，否则会造成死锁问题。同时wait必须在同步代码块中使用
		 * sleep只是单纯让当前线程休眠一段时间，不需要强制在同步代码块中使用。即使使用了也不会释放持有的锁
		 */

		/**
		 * 2.2.15 volatile关键字有什么用，它的实现原理是什么
		 */

		/**
		 * volatile是java中用于修饰变量的一个关键字，它的作用有以下几点：
		 * 1. 保证了变量对所有线程的可见性
		 * 2. 禁止了cpu对该变量操作的指令重排
		 * 原理如下：
		 * 可见性方面，由于cpu使用三级缓存的优化实现，cpu在操作一般变量的时候并不是直接从内存中获取，而是先把变量加载到这三层缓存中。修改变量则是
		 * 先修改一级缓存的变量，再不断更新到二级三级直至更新到内存中，变量的修改才算完成。但这会造成一个问题，当其他线程对变量进行修改后，当前线程
		 * 的缓存会失效，即导致两个线程所查看到的变量值不一致。使用volatile可以保证cpu对该变量每次都直接从内存中获取，每次写也直接写进内存。
		 * 指令重排方面，多线程环境下，cpu指令的编写顺序和实际的执行顺序并不是完全一致的。比如执行耗时的写的操作，cpu在写操作完成的期间会先去执行
		 * 后面比较轻松的工作。这样能有效提高cpu利用率。这也会带来操作乱序的问题。volatile通过在写操作前后加入内存屏障避免指令重排。
		 * 综上，volatile相当于轻量级的synchronized，保证了变量的可见性，但没有保证原子性。但如果多线程下的操作本身就是原子性（比如变量赋值），那么volatile会优于synchronized。
		 */

		/**
		 * 2.2.16 说说你对CompletableFuture的理解
		 */
		// Supplier是Java函数式编程中的一个接口，不接受任何参数但返回一个结果。用于生成某种类型的值。
		Supplier<Double> supplier = () -> Math.random();
		System.out.println(supplier.get());

		// thenCombine用于把两个任务组合在一起，当两个任务都执行结束以后触发回调
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "world");
		CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "!");
		CompletableFuture<String> future = future1.thenCombine(future2, (res1, res2) -> res1 + " " + res2);// 第二个参数是回调函数
		CompletableFuture<String> stringCompletableFuture = future.thenCombine(future3, (res1, res2) -> res1 + " " + res2);
		System.out.println("thenCombine: "+stringCompletableFuture.get());

		// thenCompose把两个任务组合在一起，这两个任务串行执行，也就是第一个任务执行完以后自动触发执行第二个任务。
		CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
		CompletableFuture<Integer> fRes = f1.thenCompose(parameter -> { // res为f1返回的结果，作为参数传入
			int intRes = parameter * 2;
			return CompletableFuture.supplyAsync(() -> intRes);
		});
		System.out.println("thenCompose: "+fRes.get());

		// thenAccept第一个任务执行结束后触发执行第二个任务，并且第一个任务的执行 结果作为第二个任务的参数，这个方法纯粹接受上一个任务的结果，不返回新的计算值。
		CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 100);
		f2.thenAccept((rF2) -> {
			int r = rF2 * 2;
			System.out.println("thenAccept: " + r);
		});

		// thenApply和thenAccept 一样，但是它有返回值。
		CompletableFuture<Integer> f3 = CompletableFuture.supplyAsync(() -> 100);
		CompletableFuture<Integer> f4 = f3.thenApply(rr -> rr * 2);
		System.out.println("thenApply: " + f4.get());

		// thenRun，就是第一个任务执行完成后触发执行一个实现了Runnable接口的任务，
		CompletableFuture<Integer> f5 = CompletableFuture.supplyAsync(() -> 100);
		f5.thenRun(() -> System.out.println("thenRun紧接其后的Runnable"));

		/**
		 * CompletableFuture是JDK1.8后引入的基于事件驱动的异步回调类。可以使用异步线程执行完一个任务后，继续执行另一个动作。
		 * 举个例子：在一个支付界面，我们希望先查询订单，再支付订单，再把支付结果以邮件发送。那么这算个任务就是串行进行的，
		 * 前一个任务的执行触发下一个任务。后一个任务的执行依赖前一个任务的结果
		 */

		/**
		 * 2.2.17 谈谈你对ThreadLocal实现原理的理解
		 */

		for(int i = 0; i < 5; i++){

			new Thread(() -> {
				int count = threadLocal.get();
				count++;
				threadLocal.set(count);
				System.out.println(Thread.currentThread().getName() + "get the counter is " + count);
			}).start();
		}
		/**
		 * ThreadLocal是一种线程隔离机制，实现了对共享变量的安全访问。
		 * 在一般情况下，访问共享变量需要对其进行加锁保证同一时刻只有一个线程能够对其修改。但加锁会导致性能的下降。
		 * ThreadLocal用了一种空间换时间的思想，在每个线程内部用一个ThreadLocalMap作为容器，储存了共享变量的副本。每次只对副本进行操作。
		 * 它的使用场景比较多，比如数据库单次连接的隔离。即实现了单次连接对数据库数据访问与其他线程隔离，其他线程的更新不会导致这个连接的数据库内容更新。
		 * 在断开连接时才更新数据库的实际内容。
		 */

		/**
		 * ThreadLocal的内存泄漏问题
		 * 由于ThreadLocalMap的生命周期与Thread一致，ThreadLocalMap使用ThreadLocal的弱引用作为key，如果一个ThreadLocal不存在外部强引用时，
		 * Key(ThreadLocal)势必会被GC回收，这样就会导致ThreadLocalMap中key为null，而value还存在着强引用，只有thead线程退出以后,value的强引用链条才会断掉。
		 * Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value
		 * 											  ｜
		 * 										 threadLocal(被回收)
		 *
		 */

		/**
		 * 2.2.18 CountDownLatch和CyclicBarrier有什么区别
		 */

		/**
		 * CountDownLatch字面意思是倒数发令枪，能让一个或多个线程等待，直到其他多线程完成一组操作后再让等待线程继续。
		 * 有以下使用场景：
		 * 单个线程需要汇总多个线程的结果。需要等到所有线程都返回结果后再继续执行，如并发计算。
		 * 让多个线程等待。比如秒杀场景里，让一组线程同时等待，同时执行，实现最大并行。
		 *
		 * CyclicBarrier字面意思是循环屏障，是一个同步辅助类，运行一组线程相互等待，直到所有线程达到一个公共的屏障点，屏障点就会打开，所有
		 * 等待的线程可以继续执行。不像CountDownLatch不可重复使用，CyclicBarrier可以继续使用。
		 *
		 * 区别：
		 * 1. CountDownLatch只能用一次，CyclicBarrier可以使用多次
		 * 2. CyclicBarrier可以处理更复杂的业务场景，比如发生错误可以阻塞，重制计数器
		 * 3. CyclicBarrier不会阻塞主进程，而CountDownLatch会。
		 */

		int numberOfThreads = 3;
		// 这里的3就是屏障点的数量，意味着需要等待3个线程到达屏障点后，屏障点才会打开。
		// 指在构造函数中指定的等待线程数量
		CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfThreads, () -> System.out.println("All threads have reached the barrier"));
		for(int i = 0; i < numberOfThreads; i++){
			new Thread(()->{
				try {
					System.out.println("thread is running");
					Thread.sleep(1000);
					System.out.println("Thread has reached the barrier.");
					cyclicBarrier.await(); // 等待其他线程到达屏障点
				}catch(Exception e){
					e.printStackTrace();
				}
			}).start();
		}

		/**
		 * 2.2.19 谈谈你对Happens-Before的理解
		 */

		/**
		 * Happens-Before是一种可见性模型，在多线程环境下，cpu指令会重排，但JVM通过Happens-Before保证了某些规则下的先后顺序，不受指令重排的影响。
		 * 1. 程序顺序规则：单个线程中的每个操作Happens-Before后续操作。即不管怎么重排，单线程的执行不会被重排
		 * 2. 传递性原则：如果A Happens-Before B B Happens-Before C，那么 A Happens-Before C
		 * 3. volatile：对volatile变量的写操作一定Happens-Before后续对该变量的读操作
		 * 4. 作监视器锁规则：一个线程对锁的释放一定Happens-Before后续线程对锁的加锁操作
		 * 5. 线程启动规则：如果A线程执行start方法，那么start之前的操作Happens-BeforeA线程的所有操作
		 * 6. join规则：如果A执行B.join，那么B线程的所有操作 Happens-Before A中的join操作返回
		 */
	}

	private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

	public static class MyCallable implements Callable<String>{
		@Override
		public String call() throws Exception {
			 return "Hello world";
		}
	}

	static class FutureDemo implements Callable<Void> {
		@Override
		public Void call() throws Exception {
			System.out.println("future demo starts");
			Thread.sleep(2000);
			System.out.println("future demo ends");
			return null;
		}
	}

	static class CountDownLatchDemo implements Runnable{

		CountDownLatch cdl;

		public CountDownLatchDemo(CountDownLatch cdl) {
			this.cdl = cdl;
		}

		@Override
		public void run() {
			System.out.println("CountDownLatchDemo start");
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("CountDownLatchDemo end");
			cdl.countDown();
		}
	}

	static class AfterExecutionDemo extends ThreadPoolExecutor{
		public AfterExecutionDemo(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r,t);

			if(t == null){
				System.out.println("钩子方法执行成功");
			}else{
				System.out.println("Task execution failed: " + t.getMessage());
			}
		}
	}
}
