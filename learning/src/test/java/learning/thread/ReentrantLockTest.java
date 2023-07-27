package learning.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

public class ReentrantLockTest {
	// Java语言直接提供了synchronized关键字用于加锁，但这种锁一是很重，二是获取时必须一直等待，没有额外的尝试机制

	@Test
	public void test1() throws InterruptedException {
		Thread[] threads = new Thread[50];
		Counter c = new Counter();
		for (Thread t : threads) {
			t = new Thread(() -> {
				for (int i = 0; i < 100; i++) {
					try {
						c.add(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
		Thread.sleep(1000);
	}

	class Counter {
		private final Lock lock = new ReentrantLock();
		private int count;

		public void add(int n) throws InterruptedException {
			lock.lock();
			// ReentrantLock是Java代码实现的锁，我们就必须先获取锁，然后在finally中正确释放锁。
			try {
				count += n;
				System.out.println(Thread.currentThread().getName() + " current count: " + count);
			} finally {
				lock.unlock();
			}
			// if (lock.tryLock(1, TimeUnit.SECONDS))
			// 上述代码在尝试获取锁的时候，最多等待1秒。如果1秒后仍未获取到锁，tryLock()返回false，程序就可以做一些额外处理，而不是无限等待下去。
			/*
			 * if (lock.tryLock(1, TimeUnit.SECONDS)) { try { count += n;
			 * System.out.println(Thread.currentThread().getName() + " current count: " +
			 * count); } finally { lock.unlock(); } }
			 */
		}
	}

}
