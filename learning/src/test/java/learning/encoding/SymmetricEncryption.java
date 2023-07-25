package learning.encoding;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

/**
 * @author ZHONG Jiquan
 * @create 25/07/2023 - 15:47
 */
public class SymmetricEncryption {
  // AES加密
  @Test
  public void test1() throws GeneralSecurityException {
    String message = "HelloWorld";
    System.out.println("Message: " + message);
    // 128位密钥 = 16 bytes Key:
    byte[] key = "1234567890abcdef".getBytes(StandardCharsets.UTF_8);
    System.out.println(new String(key, StandardCharsets.UTF_8));
    byte[] data = message.getBytes(StandardCharsets.UTF_8);
    byte[] encrypted = encrypt(key, data);
    System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
    byte[] decrypted = decrypt(key, encrypted);
    System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
  }

  /**
   * Java标准库提供的对称加密接口非常简单，使用时按以下步骤编写代码：
   *
   * <p>1.根据算法名称/工作模式/填充模式获取Cipher实例； 2.根据算法名称初始化一个SecretKey实例，密钥必须是指定长度；
   * 3.使用SecretKey初始化Cipher实例，并设置加密或解密模式； 4.传入明文或密文，获得密文或明文。
   *
   * @param key 密钥
   * @param message 需要加密的信息
   * @return
   * @throws GeneralSecurityException
   */
  private static byte[] encrypt(byte[] key, byte[] message) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    return cipher.doFinal(message);
  }

  private static byte[] decrypt(byte[] key, byte[] encrypted) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    return cipher.doFinal(encrypted);
  }

  @Test
  public void test2() throws GeneralSecurityException {
    String message = "HelloWorld";
    System.out.println("Message: " + message);
    byte[] key = "1234567890abcdef1234567890abcdef".getBytes(StandardCharsets.UTF_8);
    System.out.println("Key: " + new String(key,StandardCharsets.UTF_8));
    byte[] data = message.getBytes(StandardCharsets.UTF_8);
    byte[] encrypted = encryption(key, data);
    System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
    byte[] decrypted = decryption(key, encrypted);
    System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
  }

  public static byte[] encryption(byte[] key, byte[] input) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    SecureRandom sr = SecureRandom.getInstanceStrong();
    // CBC模式需要生成一个16bytes的Initialization vector
    byte[] iv = sr.generateSeed(16);
    IvParameterSpec ivps = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
    byte[] data = cipher.doFinal(input);
    return join(iv, data);
  }

  public static byte[] decryption(byte[] key, byte[] input) throws GeneralSecurityException {
    byte[] iv = new byte[16];
    byte[] data = new byte[input.length - 16];
    System.arraycopy(input, 0, iv, 0, 16);
    System.arraycopy(input, 16, data, 0, data.length);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    IvParameterSpec ivps = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);
    return cipher.doFinal(data);
  }

  private static byte[] join(byte[] iv, byte[] data) {
    byte[] res = new byte[iv.length + data.length];
    System.arraycopy(iv, 0, res, 0, iv.length);
    System.arraycopy(data, 0, res, iv.length, data.length);
    return res;
  }

  @Test
  public void test3() throws GeneralSecurityException {
    Security.addProvider(new BouncyCastleProvider());
    String message = "Hello, world";
    String password = "hello12345";
    byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);
    System.out.println("salt: " + new BigInteger(1,salt));
    //加密
    byte[] data = message.getBytes(StandardCharsets.UTF_8);
    byte[] encrypted = PBEEncrypt(password, salt, data);
    System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

    //解密
    byte[] decrypted = PBEDecrypt(password, salt, encrypted);
    System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
  }

  private static byte[] PBEEncrypt(String password, byte[] salt, byte[] input) throws GeneralSecurityException {
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
    SecretKey skey = sKeyFactory.generateSecret(pbeKeySpec);
    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 1000);
    Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
    cipher.init(Cipher.ENCRYPT_MODE, skey, pbeParameterSpec);
    return cipher.doFinal(input);
  }

  private static byte[] PBEDecrypt(String password, byte[] salt, byte[] input) throws GeneralSecurityException {
    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
    SecretKey skey = sKeyFactory.generateSecret(keySpec);
    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 1000);
    Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
    cipher.init(Cipher.DECRYPT_MODE, skey, pbeParameterSpec);
    return cipher.doFinal(input);
  }
}
