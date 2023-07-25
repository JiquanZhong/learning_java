package learning.encoding;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Encoding {

  @Test
  public void test1() {
    String encoded = URLEncoder.encode("中文!", StandardCharsets.UTF_8);
    System.out.println(encoded);
    System.out.println(URLDecoder.decode(encoded, StandardCharsets.UTF_8));
  }

  @Test
  public void test2() {
    System.out.println("1".hashCode());
    System.out.println("2".hashCode());
    System.out.println("3".hashCode());
    System.out.println("4".hashCode());
  }

  @Test
  public void Test3() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update("hello".getBytes("UTF-8"));
    md.update("world".getBytes("UTF-8"));
    byte[] result = md.digest();
    System.out.println(new BigInteger(1, result).toString(16));
  }

  @Test
  public void test4() throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update("123".getBytes(StandardCharsets.UTF_8));
    md.update("456".getBytes(StandardCharsets.UTF_8));
    byte[] result = md.digest();
    System.out.println(new BigInteger(1, result).toString(16));

    MessageDigest md1 = MessageDigest.getInstance("MD5");
    md1.update("123456".getBytes(StandardCharsets.UTF_8));
    byte[] result1 = md.digest();
    System.out.println(new BigInteger(1, result1).toString(16));

    boolean res = MessageDigest.isEqual(result, result1);
    System.out.println(res);
  }

  @Test
  public void test5() throws NoSuchAlgorithmException {
    Security.addProvider(new BouncyCastleProvider());
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update("HelloWorld".getBytes(StandardCharsets.UTF_8));
    byte[] result = md.digest();
    System.out.println(new BigInteger(1, result).toString(16));
  }

  @Test
  public void test6() throws NoSuchAlgorithmException, InvalidKeyException {
    KeyGenerator generator = KeyGenerator.getInstance("HmacMD5");
    SecretKey key = generator.generateKey();
    System.out.println(Arrays.toString(key.getEncoded()));
    System.out.println(new BigInteger(1, key.getEncoded()).toString(16));
    Mac hmacMD5 = Mac.getInstance("HmacMD5");
    hmacMD5.init(key);
    hmacMD5.update("Hello World".getBytes(StandardCharsets.UTF_8));
    byte[] result = hmacMD5.doFinal();
    System.out.println(new BigInteger(1, result).toString(16));

    SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "HmacMD5");
    Mac decoder = Mac.getInstance("HmacMD5");
    decoder.init(keySpec);
    decoder.update(result);
    byte[] res = decoder.doFinal();
    System.out.println(Arrays.toString(res));
//    KeyGenerator generator1 = KeyGenerator.getInstance("HmacMD5");
//    SecretKey key1 = generator1.generateKey();
//    System.out.println(new BigInteger(1, key1.getEncoded()).toString(16));
//    Mac hmacMD51 = Mac.getInstance("HmacMD5");
//    hmacMD51.init(key);
//    hmacMD51.update("Hello World".getBytes(StandardCharsets.UTF_8));
//    byte[] result1 = hmacMD51.doFinal();
//    System.out.println(new BigInteger(1, result1).toString(16));

  }




}
