package learning.encoding;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;


public class KeyEncoding {
	@Test
	public void test1() {
		// Bob和Alice:
		Person bob = new Person("Bob");
		Person alice = new Person("Alice");

		// 各自生成KeyPair:
		bob.generateKeyPair();
		alice.generateKeyPair();

		// 双方交换各自的PublicKey:
		// Bob根据Alice的PublicKey生成自己的本地密钥:
		bob.generateSecretKey(alice.publicKey.getEncoded());

		// Alice根据Bob的PublicKey生成自己的本地密钥:
		alice.generateSecretKey(bob.publicKey.getEncoded());

		bob.printKeys();
		alice.printKeys();
		// 双方的SecretKey相同，后续通信将使用SecretKey作为密钥进行AES加解密...
	}

	class Person {
		public final String name;

		public PublicKey publicKey;
		public PrivateKey privateKey;
		private byte[] secretKey;

		public Person(String name) {
			this.name = name;
		}

		public void generateKeyPair() {
			try {
				KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DH");
				kpGen.initialize(512);
				KeyPair kp = kpGen.generateKeyPair();
				this.privateKey = kp.getPrivate();
				this.publicKey = kp.getPublic();
			} catch (GeneralSecurityException e) {
				throw new RuntimeException(e);
			}
		}

		public void generateSecretKey(byte[] receivedPubKeyBytes) {
			try {
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(receivedPubKeyBytes);
				KeyFactory kf = KeyFactory.getInstance("DH");
				PublicKey receivedPublicKey = kf.generatePublic(keySpec);

				KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
				keyAgreement.init(this.privateKey);
				keyAgreement.doPhase(receivedPublicKey, true);

				this.secretKey = keyAgreement.generateSecret();
			} catch (GeneralSecurityException e) {
				throw new RuntimeException(e);
			}
		}

		public void printKeys() {
			System.out.printf("Name: %s\n", this.name);
			System.out.printf("Private key: %x\n", new BigInteger(1, this.privateKey.getEncoded()));
			System.out.printf("Public key: %x\n", new BigInteger(1, this.publicKey.getEncoded()));
			System.out.printf("Secret key: %x\n", new BigInteger(1, this.secretKey));
		}
	}

	@Test
	public void test2() throws GeneralSecurityException {
		// 生成RSA公钥/私钥:
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
		kpGen.initialize(1024);
		KeyPair kp = kpGen.generateKeyPair();
		PrivateKey sk = kp.getPrivate();
		PublicKey pk = kp.getPublic();

		// 待签名的消息:
		byte[] message = "Hello, I am Bob!".getBytes(StandardCharsets.UTF_8);

		// 用私钥签名:
		Signature s = Signature.getInstance("SHA1withRSA");
		s.initSign(sk);
		s.update(message);
		byte[] signed = s.sign();
		System.out.println(String.format("signature: %x", new BigInteger(1, signed)));

		// 用公钥验证:
		Signature v = Signature.getInstance("SHA1withRSA");
		v.initVerify(pk);
		v.update(message);
		boolean valid = v.verify(signed);
		System.out.println("valid? " + valid);
	}
}
