package learning.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;

public class ThreadTest2 {
	@Test
	public void test1() throws InterruptedException {
		var q = new TaskQueue();
		var ts = new ArrayList<Thread>();
		for (int i = 0; i < 5; i++) {
			var t = new Thread() {
				public void run() {
					// 执行task:
					while (true) {
						try {
							String s = q.getTask();
							System.out.println("execute task: " + s);
						} catch (InterruptedException e) {
							return;
						}
					}
				}
			};
			t.start();
			ts.add(t);
		}
		var add = new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				// 放入task:
				String s = "t-" + Math.random();
				System.out.println("add task: " + s);
				q.addTask(s);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		});
		add.start();
		add.join();
		Thread.sleep(1000);
		for (var t : ts) {
			t.interrupt();
		}
	}

	class TaskQueue {
		Queue<String> queue = new LinkedList<>();

		public synchronized void addTask(String s) {
			this.queue.add(s);
			// 唤醒在this锁等待的线程
			// 这个方法会唤醒所有正在this锁等待的线程（就是在getTask()中位于this.wait()的线程），
			// 从而使得等待线程从this.wait()方法返回
			this.notifyAll();
		}

		public synchronized String getTask() throws InterruptedException {
			// 只能使用while而不能使用if
			// 每次被唤醒后拿到this锁就必须再次判断：
			while (queue.isEmpty()) {
				// 只有synchronized的代码才能调用wait方法，调用后，会释放当前线程获得的锁，wait方法返回后，线程又会重新试图获取锁
				// 在此方法中，锁对象为this，所以对this调用wait方法。
				this.wait();
			}
			return queue.remove();
		}
	}
}
