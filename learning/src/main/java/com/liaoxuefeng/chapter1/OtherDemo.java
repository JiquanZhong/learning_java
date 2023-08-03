package com.liaoxuefeng.chapter1;

import com.liaoxuefeng.chapter1.spi.MyService;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZHONG Jiquan
 * @create 05/08/2023 - 02:53
 */
public class OtherDemo {
	public static void main(String[] args) throws IOException {
		/** 1.3.1 请对比一下Java和JavaScript 的区别 */

		/**
		 * 1. 起源：Java是95年由Sun公司研发的，而JavaScript是NetScape公司为了其浏览器功能而开发的解释型语言，最初命名LiveScript。
		 * 当时Sun和Netscape有合作，而Netscape希望其外观像Java所以给它改名为JavaScript 2.
		 * 对象设计：Java是完全面向对象设计的语言。JavaScript是脚本语言，基于对象和事件驱动的语言。 3.
		 * 运行机制：Java源码在执行前先要被编译成class字节码文件，再由JVM执行。而JavaScript是解释型语言，不需要编译直接由浏览器执行。 4.
		 * 变量定义不同：Java采用强类型变量检查，即所有变量使用前必须声明。而JavaScript是弱类型，使用前可以不声明，由解释器执行时自动判断类型。
		 */

		/** 1.3.2 什么是受检异常和非受检异常 */

		/**
		 * 受检异常：编译时强制要求处理的异常。要么抛给方法调用者处理，要么try catch捕获。常见有：FileNotFoundException
		 * 非受检异常：又为运行时异常（RuntimeException）+Error，不强制要求处理的异常。通常由程序不严谨导致，常见有：NullPointerException,
		 * ArrayIndexOutBoundsException ArithmeticException
		 */
		Throwable a = new NoSuchMethodException();

		/**
		 * Java中异常继承关系： Throwable = Error + Exception Error表示程序底层或硬件相关的错误，如OOM。 Exception表示程序中的异常。
		 * RuntimeException运行时异常即非受检异常。
		 */

		/** 1.3.3 fail-fast和fail-safe机制分别有什么作用 */

		/**
		 * fail-fast机制在遍历集合时，一旦发现其他线程对集合进行修改，就立刻抛出异常。用于快速检测并发修改，但会导致遍历中断。
		 * java.util下的都是fail-fast的，常见的有HashMap和ArrayList
		 * fail-safe机制运行在遍历集合的时候对数据进行修改。因为遍历的是数据的副本，从而避免类并发修改导致的异常，但可能看不到最新的数据。
		 * java.util.concurrent下的都是fail-safe的，常见的有ConcurrentHashMap
		 */
		// fail-fast例子
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("key1", "value1");
		hashMap.put("key2", "value2");
		Iterator<String> iterator1 = hashMap.keySet().iterator();

		// 迭代遍历集合
		while (iterator1.hasNext()) {
			String key = iterator1.next();
			System.out.println(key + ": " + hashMap.get(key));
			// 在迭代过程中，修改集合
//      hashMap.put("key3", "value3"); // 这里会抛出 ConcurrentModificationException 异常
		}

		// fail-safe例子
		Map<String, String> concurrentHashMap = new ConcurrentHashMap<>();
		concurrentHashMap.put("key1", "value1");
		concurrentHashMap.put("key2", "value2");
		Iterator<String> iterator2 = concurrentHashMap.keySet().iterator();
		// 迭代遍历集合
		while (iterator2.hasNext()) {
			String key = iterator2.next();
			System.out.println(key + ": " + concurrentHashMap.get(key));
			// 在迭代过程中，修改集合
			concurrentHashMap.put("key3", "value3"); // 这里不会抛出异常
		}

		/**
		 * 1.3.4 如何理解序列化和反序列化
		 */

		/**
		 * 序列化的目的是为了解决网络通信的对象传输问题。就是说把当前JVM进程里的对象传输到另一JVM里。该对象必须实现Serializable接口。里面没有方法，只是一个标记接口。
		 * 序列化，即把对象转化成字节流。反序列化，即把网络或文件中的字节流转化成对象。
		 * 序列化的前提是双方对于对象的可识别性。如果对方不是Java语言，应该考虑使用其他通用格式如JSON，XML，Protocol Buffers等
		 */

		try {
			// 创建一个对象
			String obj = new String("John");
			// 序列化对象到文件
			FileOutputStream fileOut = new FileOutputStream("data.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			System.out.println("对象已序列化到文件");
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			// 反序列化对象
			FileInputStream fileIn = new FileInputStream("data.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			String obj = (String) in.readObject();
			in.close();
			fileIn.close();
			System.out.println("对象已从文件反序列化");
			// 使用反序列化得到的对象
			System.out.println(obj);
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		/**
		 * 拓展：序列化算法
		 * 1. 表示对象的类。先检查对象的类是否实现了Serializable接口，没有则会抛出NotSerializableException异常。
		 * 2. 序列化对象的字段。Java会对对象的每个字段（基础类型和引用类型）进行递归遍历。引用类型指向的类也需要实现Serializable。
		 * 3. 版本控制。Java会自动生成序列化版本号SerialVersionUID用于表示序列化的类的版本，以保证序列化前后的类是兼容的。
		 * 4. 特殊处理。静态字段不会被序列化，因为静态字段输出类而不是对象；transient修饰的字段也不会被序列化。
		 */

		/**
		 * 1.3.5 什么是SPI，它有什么用
		 */

		/**
		 * 服务提供接口：Service Provider Interface是一种基于接口的动态拓展机制。相当于Java提供了一套接口，然后第三方可以实现这个接口。
		 * 程序在运行时就可以根据配置信息动态加载第三方的接口实现类，从而完成动态的拓展。
		 * 经典案例为数据库jdbc驱动：
		 * Oracle和Mysql数据库都实现Java.sql.Driver这个接口。他们就是数据库服务的提供商。
		 * 在程序运行时，根据我的驱动声明，动态加载对应的拓展实现。
		 * 此外很多开源框架也提供自己的SPI。如Dubbo提供类ExtensionLoader，Spring提供SpringFactoryLoader。
		 * 该机制的思想是把装配的控制权移交到程序外部，做到标准和实现的解藕。
		 */

		/**
		 * 实现SPI的步骤：
		 * 1. 定义一个接口作为标准
		 * 2. 在classpath下创建一个META-INF/service目录
		 * 3. 在应用程序使用ServiceLoad就可以根据接口名找到classpath的所有拓展实现。
		 */
		ServiceLoader<MyService> serviceLoader = ServiceLoader.load(MyService.class);
		for(MyService service : serviceLoader) {
			service.doSomething();
		}

		/**
		 * 1.3.6 finally语句块一定会执行吗
		 */

		/**
		 * finally在以下两种情况不执行：
		 * 1. 程序没有进入try语句块而因为异常导致的程序终止。原因是开发人员捕获的异常范围不够
		 * 2. 在try或catch中执行类System.exit(0)方法导致JVM直接退出
		 */

		// 情况1的例子
		System.out.println("Before try");
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("file"));
		int read = inputStreamReader.read();
		try {
			System.out.println("Inside try");
			// 这里抛出一个异常，但是程序并未进入 try 语句块，因此 finally 不会执行
			throw new RuntimeException("Exception in try");
		} finally {
			System.out.println("Inside finally");
		}

		/**
		 * 1.3.7 什么是内存溢出，什么事内存泄漏
		 */

		/**
		 * 内存溢出：会抛出OOM，当JVM内存不足而又需要放一个新的对象时，就会抛出OOM
		 * 内存泄漏：对象使用完后没有被释放，内存中该对象就会一直占用内存，导致内存像被"偷走"类一部分一样。常见的原因是流对象，数据库的连接等没有释放。
		 */
	}
}
