package learning.list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class ListTest {

	@BeforeEach
	public void BeforeTest() {
		System.out.println("============test start=============");
	}

	@AfterEach
	public void AfterTest() {
		System.out.println("============test end=============");
	}

	@Test
	@Order(30)
	@EnabledOnOs(OS.MAC)
	public void test1() throws FileNotFoundException, IOException {
		URL url = ListTest.class.getResource("/conf.properties");
		System.out.println("url: " + url);
		File file = new File(url.getPath());
		System.out.println("building file path: " + file.getPath());
		// File file = new File("classpath:conf.properties");
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		String last_open_file = prop.getProperty("last_open_file");
		String auto_save_interval = prop.getProperty("auto_save_interval");
		System.out.println(last_open_file);
		System.out.println(auto_save_interval);
	}

	@Test
	@Order(20)
	@EnabledOnOs(OS.WINDOWS)
	public void test2() throws FileNotFoundException, IOException {
		URL url = ListTest.class.getResource("/conf.properties");
		System.out.println("url: " + url);
		File file = new File(url.getPath());
		System.out.println("building file path: " + file.getPath());
		// File file = new File("classpath:conf.properties");
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		String last_open_file = prop.getProperty("last_open_file");
		String auto_save_interval = prop.getProperty("auto_save_interval");
		System.out.println(last_open_file);
		System.out.println(auto_save_interval);
	}

	@Test
	@Order(10)
	@EnabledOnJre(JRE.JAVA_17)
	public void testOnlyOnDebugMode() {
		System.out.println("hello");
	}

	@Test
	@EnabledOnOs(OS.MAC)
	public void testAbs() {
		System.out.println("hello");
	}

	@Test
	public void test3() {
		Queue<Integer> queue = new LinkedList<>();
		queue.offer(null);
		queue.offer(null);
		System.out.println(queue.size());
	}

	@Test
	public void test4() {
		Set<String> res = new HashSet<>();
		res.add("0");
		res.add("1");
		res.add("2");
		res.add("3");
		res.add("4");
		System.out.println(Arrays.toString(res.toArray()));
	}
}
