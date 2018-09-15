package com.mall.demo.common.utils;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jdk.nashorn.internal.runtime.GlobalFunctions.encodeURIComponent;

public class EncryptUtil {

	private static final Logger log = LoggerFactory.getLogger(EncryptUtil.class);
	private static final String DESKEY = "DESEncryptUtil";

	/**
	 * 对输入的内容进行加md5单身加密
	 * 
	 * @param input
	 * @return 32的字符串
	 */
	public static String encodeMd5(String input) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			md5.update(input.getBytes("utf-8"));
			byte[] b = md5.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < b.length; i++) {
				String s = Integer.toHexString(b[i] & 0xFF);
				if (s.length() == 1) {
					sb.append("0");
				}
				sb.append(s.toUpperCase());
			}
			return sb.toString();
		} catch (Exception ex) {
			log.error(" Md5 String {" + input + "},failed....");
		}
		return input;
	}

	public static String encodeDES(String input) {
		return encodeDES(input,DESKEY);
	}

	public static String decodeDES(String input) {
		return decodeDES(input,DESKEY);
	}
	
	
	public static String encodeDES(String input,String deskey) {
		try {
			return DESEncryptUtil.getInstance().encrypt(input, fillMask('X', 16, deskey));
		} catch (Exception e) {
			log.error("encode error", e);
		}
		return null;
	}

	public static String decodeDES(String input,String deskey) {
		try {
			return DESEncryptUtil.getInstance().decrypt(input, fillMask('X', 16, deskey));
		} catch (Exception e) {
			log.error("decode error", e);
		}
		return null;
	}
	
	

	/**
	 * eg: 12 ,想要获取 XXXX12,固定长度的字符串
	 * 
	 * @param mask
	 *            不足时用来填充的字符
	 * @param length
	 *            填充后反回字符串的总长度
	 * @param input
	 *            需要填充的字符串
	 * @return 填充后的字符串
	 */
	public static String fillMask(char mask, int length, String input) {
		final int inputLen = input.length();
		if (inputLen >= length) {
			return input;
		}
		int maskLen = length - inputLen;
		char[] chars = new char[maskLen];
		for (int i = 0; i < maskLen; i++) {
			chars[i] = mask;
		}
		return new String(chars) + input;
	}

	private static class DESEncryptUtil {

		private static DESEncryptUtil des;

		static DESEncryptUtil getInstance() {
			if (null == des) {
				des = new DESEncryptUtil();
			}
			return des;
		}

		final static String KEY_ALGORITHM = "DES";
		final static String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

		SecretKey keyGenerator(String keyStr) throws Exception {
			byte input[] = HexString2Bytes(keyStr);
			DESKeySpec desKey = new DESKeySpec(input);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			SecretKey securekey = keyFactory.generateSecret(desKey);
			return securekey;
		}

		int parse(char c) {
			if (c >= 'a')
				return (c - 'a' + 10) & 0x0f;
			if (c >= 'A')
				return (c - 'A' + 10) & 0x0f;
			return (c - '0') & 0x0f;
		}

		byte[] HexString2Bytes(String hexstr) {
			byte[] b = new byte[hexstr.length() / 2];
			int j = 0;
			for (int i = 0; i < b.length; i++) {
				char c0 = hexstr.charAt(j++);
				char c1 = hexstr.charAt(j++);
				b[i] = (byte) ((parse(c0) << 4) | parse(c1));
			}
			return b;
		}

		String encrypt(String data, String key) throws Exception {
			Key deskey = keyGenerator(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			SecureRandom random = new SecureRandom();
			cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
			byte[] results = cipher.doFinal(data.getBytes());
			return Base64.encodeBase64String(results);
		}

		String decrypt(String data, String key) throws Exception {
			Key deskey = keyGenerator(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// 初始化Cipher对象，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			// 执行解密操作
			return new String(cipher.doFinal(Base64.decodeBase64(data)));
		}
	}

	public static void main(String[] args) {
		System.out.println(decodeDES("2VVKrskvy9XJMmH8ssFRjpurqJopEcFWKX+/KU/YKJSkpPZoS3S7IA=="));
		System.out.println(encodeURIComponent(null, "a+b"));
	}
}
