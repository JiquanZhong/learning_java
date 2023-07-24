package learning.encoding;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class Encoding {

	@Test
	public void test1() {
		String encoded = URLEncoder.encode("中文!", StandardCharsets.UTF_8);
		System.out.println(encoded);
	}
}
