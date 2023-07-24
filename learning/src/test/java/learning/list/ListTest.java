package learning.list;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ListTest {

	@BeforeEach
	private void BeforeTest() {
		System.out.println("============test start=============");
	}

	@AfterEach
	private void AfterTest() {
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
//		File file = new File("classpath:conf.properties");
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
//		File file = new File("classpath:conf.properties");
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
	void testOnlyOnDebugMode() {
		// TODO: this test is only run on DEBUG=true
		System.out.println("hello");
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 5, 100 })
	void testAbs(int x) {
		assertEquals(x, Math.abs(x));
	}
}
