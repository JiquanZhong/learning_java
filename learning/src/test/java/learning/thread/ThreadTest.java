package learning.thread;

import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class ThreadTest {
	@BeforeEach
	public void before(TestInfo testInfo) {
		System.out.print("==================");
		System.out.print(testInfo.getDisplayName() + " start!");
		System.out.println("==================");
	}

	@AfterEach
	public void after(TestInfo testInfo) {
		System.out.print("==================");
		System.out.print(testInfo.getDisplayName() + " end!");
		System.out.println("==================");
	}

	@Test
	public void test1() {
		Thread t = new Mythread();
		t.start();
	}

	class Mythread extends Thread {
		@Override
		public void run() {
			System.out.println("Method 1");
		}
	}

	@Test
	public void test2() {
		Thread t = new Thread(new MyRunnable());
		t.start();
	}

	class MyRunnable implements Runnable {
		@Override
		public void run() {
			System.out.println("Method 2");
		}
	}

	@Test
	public void test3() {
		Thread t = new Thread(() -> {
			System.out.println("Method 3");
		});
		t.start();
	}

	@Test
	public void test4() throws InterruptedException {
		Thread t = new Thread(() -> {
			System.out.println("Say Hello");
		});
		System.out.println("start");
		t.start();
		t.join();
		System.out.println("end");
	}

	@Test
	public void test5() throws InterruptedException {
		Thread t = new Thread1();
		t.start();
		Thread.sleep(1);
		t.interrupt();
		t.join();
		System.out.println("end");
	}

	class Thread1 extends Thread {
		@Override
		public void run() {
			int n = 0;
			while (!isInterrupted()) {
				n++;
				System.out.println(n);
			}
		}
	}

	@Test
	public void test6() throws InterruptedException {
		Thread t = new Thread2();
		t.start();
		Thread.sleep(1000);
		t.interrupt(); // 中断t线程
		t.join(); // 等待t线程结束
		System.out.println("end");
	}

	class Thread2 extends Thread {
		public void run() {
			Thread hello = new HelloThread();
			hello.start(); // 启动hello线程
			try {
				hello.join(); // 等待hello线程结束
			} catch (InterruptedException e) {
				System.out.println("interrupted!");
			}
			hello.interrupt();
		}
	}

	class HelloThread extends Thread {
		public void run() {
			int n = 0;
			while (!isInterrupted()) {
				n++;
				System.out.println(n + " hello!");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	@Test
	public void test7() throws InterruptedException {
		Thread t = new Thread(() -> {
			while (true) {
				System.out.println(LocalTime.now());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		});
//		t.setDaemon(true);
		t.start();
	}

	@Test
	public void test8() throws InterruptedException {
		var add = new AddThread();
		var dec = new DecThread();
		add.start();
		dec.start();
		add.join();
		dec.join();
		System.out.println(Counter.count);
	}

	class Counter {
		public static final Object lock = new Object();
		public static int count = 0;
	}

	class AddThread extends Thread {
		public void run() {
			for (int i = 0; i < 10000; i++) {
				synchronized (Counter.lock) {
					Counter.count += 1;
				}
			}
		}
	}

	class DecThread extends Thread {
		public void run() {
			for (int i = 0; i < 10000; i++) {
				synchronized (Counter.lock) {
					Counter.count -= 1;
				}
			}
		}
	}

	@Test
	public void test9() throws InterruptedException {
		Counter1 c = new Counter1();
		Thread t1 = new Thread(() -> {
			c.add(15);
		});
		t1.start();

		Thread t2 = new Thread(() -> {
			c.dec(-15);
		});
		t2.start();
		System.out.println(c.count);
	}

	public class Counter1 {
		private int count = 0;

		public synchronized void add(int n) {
			if (n < 0) {
				dec(-n);
			} else {
				count += n;
			}
		}

		public synchronized void dec(int n) {
			count += n;
		}
	}
}
