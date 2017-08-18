package com.cn.mitenoenc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.cn.crypto.implement.CryptoBASE64Impl;
import com.cn.encryption.EncryptString;
import com.cn.encryption.MD5;

public class MitenoEncryption {

	static String TriDES_ECB_PADDING = "DESede/ECB/PKCS5Padding";
	private static Logger logger = Logger.getLogger(MitenoEncryption.class);
	/**
	 * 对需要加密的原文补充的字符
	 */
	private static String buStr = "8";

	public MitenoEncryption() {
		// TODO Auto-generated constructor stub
	}

	private static byte[] Hash(byte[] record_no, byte[] coupon_num,
			byte[] coupon_activity_id, byte[] user_tel) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] input = new byte[record_no.length + coupon_num.length
				+ coupon_activity_id.length + user_tel.length];
		for (int i = 0; i < record_no.length; i++)
			input[i] = record_no[i];
		for (int j = 0; j < coupon_num.length; j++)
			input[record_no.length + j] = coupon_num[j];
		for (int j = 0; j < coupon_activity_id.length; j++)
			input[record_no.length + coupon_num.length + j] = coupon_activity_id[j];
		for (int j = 0; j < user_tel.length; j++)
			input[record_no.length + coupon_num.length
					+ coupon_activity_id.length + j] = user_tel[j];

		// logger.debug(new String(input,0,input.length));

		byte[] out24 = new byte[24];
		byte[] out = digest.digest(input);
		for (int i = 0; i < out.length; i++) {
			out24[i] = out[i];
		}
		out24[20] = (byte) 0x00;
		out24[21] = (byte) 0x00;
		out24[22] = (byte) 0x00;
		out24[23] = (byte) 0x00;
		// MessageDigest digestag = MessageDigest.getInstance("MD5");
		// byte[] outf = digestag.digest(out);
		// for(int l=0;l<outf.length;l++)
		// coupon_activity_id[l]=outf[l];

		return out24;
	}

	private static String Hash1(String record_no, String coupon_num,
			String coupon_activity_id, String user_tel) {
		String input = "";
		input = record_no + coupon_num + coupon_activity_id + user_tel;
		logger.debug("生成密文所需的拼接字符串为:" + input);
		String toMd5 = EncryptString.strToMD5(input, 32);
		// String key16 =toMd5.substring(0, 8)+toMd5.substring(24, 32);
		return toMd5;
	}

	/**
	 * hash2百度测试用
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @return
	 */
	private static String Hash2(String coupon_activity_id, String coupon_num,
			String record_no, String sign_method, String source, String ts,
			String user_tel, String baiduDES_password) {

		String toMd5 = MitenoEncryption.createMD5_2(coupon_activity_id,
				coupon_num, record_no, sign_method, source, ts, user_tel,
				baiduDES_password);
		// String input = "";
		// input = coupon_activity_id + coupon_num + record_no
		// + sign_method + source + ts + user_tel + baiduDES_password;
		// logger.debug("生成密文所需的拼接字符串为:" + input);
		// String toMd5 = EncryptString.strToMD5(input, 32);
		// String key16 =toMd5.substring(0, 8)+toMd5.substring(24, 32);
		return toMd5;
	}

	/**
	 * 按照参数对 银行卡号密文 进行解密
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @param bank_card_num
	 * @return
	 */
	public static String createDecryptor1(String record_no, String coupon_num,
			String coupon_activity_id, String user_tel, String bank_card_num) {
		String key32 = Hash1(record_no, coupon_num, coupon_activity_id,
				user_tel);
		// 将原文解密
		String yuanwen = EncryptString.encryptAndDiscrypt1(bank_card_num,
				key32, 1);
		logger.debug("银行卡号密文:" + bank_card_num + "使用key(MD5):" + key32);
		logger.debug("解密后原文: " + yuanwen);
		return yuanwen;
	}

	/**
	 * 按照参数对 银行卡号密文 进行解密
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @param bank_card_num
	 * @return
	 * @throws Exception 
	 */
	public static String createDecryptor2(String bank_card_num,
			String coupon_activity_id, String coupon_num, String record_no,
			String sign_method, String source, String ts, String user_tel,
			String baiduDES_password) throws Exception {
		String key32 = Hash2(coupon_activity_id, coupon_num, record_no,
				sign_method, source, ts, user_tel, baiduDES_password);
		
		//BASE64解码
//		String bank_card_32=com.meterware.httpunit.Base64.decode(bank_card_num);
//		if(bank_card_32==null||bank_card_32.length()!=32){
//			logger.debug("银行卡密文BASE64解密后,所得字符串错误:"+bank_card_32);
//			return "error";
//		}
		// 将32位 银行卡号密文 拆成两个16位字符串
		String miwen1 = bank_card_num.substring(0, 16);
		String miwen2 = bank_card_num.substring(16, 32);
		logger.debug(miwen1);
		logger.debug(miwen2);

		// 将原文解密
		String yuanwen1 = EncryptString.encryptAndDiscrypt1(miwen1, key32, 1);
		String yuanwen2 = EncryptString.encryptAndDiscrypt1(miwen2, key32, 1);
		logger.debug("使用key(MD5):" + key32 + "开始解密");
		logger.debug("银行卡号密文前半段:" + miwen1 + "解密后: " + yuanwen1);
		logger.debug("银行卡号密文后半段:" + miwen2 + "解密后: " + yuanwen2);
		// 将两段原文拼接,并去除补位的字母
		String bank_card_num_yuanwen = yuanwen1 + yuanwen2;
		int tailNum=bank_card_num_yuanwen.lastIndexOf(buStr);
		if(tailNum==-1){
			return "error";
		}
		bank_card_num_yuanwen = bank_card_num_yuanwen.substring(0,tailNum);
		logger.debug("去除字母后的原文为:" + bank_card_num_yuanwen);
		return bank_card_num_yuanwen;
	}

	/**
	 * 按照参数对 银行卡号 进行加密
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @param bank_card_num
	 * @return
	 * @throws Exception
	 */
	public static String createEncryptor1(String record_no, String coupon_num,
			String coupon_activity_id, String user_tel, String bank_card_num)
			throws Exception {
		String key32 = Hash1(record_no, coupon_num, coupon_activity_id,
				user_tel);
		// 将原文加密
		// String miwen = EncryptString.encryptAndDiscrypt1(bank_card_num,
		// key32,0);
		Enc denc = new Enc();
		byte[] output = new byte[100];
		denc.denc(bank_card_num.getBytes(), key32.getBytes(), output);
		System.out.print(output.length);
		logger.debug("银行卡号:" + bank_card_num + "使用key(MD5):" + key32);
		logger.debug("加密后密文: " + new String(output));
		return new String(output);
	}

	/**
	 * createEncryptor2百度本机测试 按照参数对 银行卡号 进行加密
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @param bank_card_num
	 * @return
	 */
	public static String createEncryptor2(String bank_card_num,
			String coupon_activity_id, String coupon_num, String record_no,
			String sign_method, String source, String ts, String user_tel,
			String baiduDES_password) {
		String key32 = Hash2(coupon_activity_id, coupon_num, record_no,
				sign_method, source, ts, user_tel, baiduDES_password);
		// 将原文解密
		String miwen = EncryptString.encryptAndDiscrypt1(bank_card_num, key32,
				0);
		logger.debug("银行卡号:" + bank_card_num + "使用key(MD5):" + key32);
		logger.debug("加密后密文: " + miwen);
		return miwen;
	}

	/**
	 * 百度测试用
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @param source
	 * @param bank_card_num

	 * @return
	 */
	public static String createMD5_1(String bank_card_num,
			String coupon_activity_id, String coupon_num, String record_no,
			String sign_method, String source, String ts, String user_tel,
			String baiduDES_password) {
		TreeMap<String, String> dataMap = new TreeMap<String, String>();
		dataMap.put("bank_card_num", bank_card_num);
		dataMap.put("coupon_activity_id", coupon_activity_id);
		dataMap.put("coupon_num", coupon_num);
		dataMap.put("record_no", record_no);
		dataMap.put("source", source);
		dataMap.put("sign_method", sign_method);
		dataMap.put("ts", ts);
		dataMap.put("user_tel", user_tel);
		String data = "";
		Iterator titer = dataMap.entrySet().iterator();
		boolean first = true;
		while (titer.hasNext()) {
			Map.Entry ent = (Map.Entry) titer.next();
			String keyt = ent.getKey().toString();
			String valuet = ent.getValue().toString();
			// System.out.println(keyt+"*"+valuet);
			// 判断是否是第一个参数
			if (first) {
				data = data + keyt + "=" + valuet;
				first = false;
			} else {
				data = data + "&" + keyt + "=" + valuet;
			}

		}
		// for (int i = 0; i < dataMap.size(); i++) {
		// data = data + "&" + dataMap.get(i).toString();
		// }
		try {

			data = data + "&key="
					+ URLEncoder.encode(baiduDES_password, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("生成MD5码签名的原文" + data);
		String sign = new MD5().Md5(data, 32);
		logger.debug("生成的MD5码签名" + sign);
		return sign;
	}
	
	/**
	 * 百度测试用
	 * 

	 * @param coupon_activity_id

	 * @param source

	 * @return
	 */
	public static String createMD5_2(
			String coupon_activity_id, 
			String sign_method, 
			String source, 
			String ts,
			String baiduDES_password) {
		TreeMap<String, String> dataMap = new TreeMap<String, String>();
		
		dataMap.put("coupon_activity_id", coupon_activity_id);
		dataMap.put("source", source);
		dataMap.put("sign_method", sign_method);
		dataMap.put("ts", ts);
		String data = "";
		Iterator titer = dataMap.entrySet().iterator();
		boolean first = true;
		while (titer.hasNext()) {
			Map.Entry ent = (Map.Entry) titer.next();
			String keyt = ent.getKey().toString();
			String valuet = ent.getValue().toString();
			// System.out.println(keyt+"*"+valuet);
			// 判断是否是第一个参数
			if (first) {
				data = data + keyt + "=" + valuet;
				first = false;
			} else {
				data = data + "&" + keyt + "=" + valuet;
			}

		}
		// for (int i = 0; i < dataMap.size(); i++) {
		// data = data + "&" + dataMap.get(i).toString();
		// }
		try {

			data = data + "&key="
					+ URLEncoder.encode(baiduDES_password, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("生成MD5_2码签名的原文" + data);
		String sign = new MD5().Md5(data, 32);
		logger.debug("生成的MD5_2码签名" + sign);
		return sign;
	}

	/**
	 * 百度测试用
	 * 
	 * @param record_no
	 * @param coupon_num
	 * @param coupon_activity_id
	 * @param user_tel
	 * @param source

	 * @return
	 */
	public static String createMD5_2(String coupon_activity_id,
			String coupon_num, String record_no, String sign_method,
			String source, String ts, String user_tel, String baiduDES_password) {
		TreeMap<String, String> dataMap = new TreeMap<String, String>();
		dataMap.put("coupon_activity_id", coupon_activity_id);
		dataMap.put("coupon_num", coupon_num);
		dataMap.put("record_no", record_no);
		dataMap.put("source", source);
		dataMap.put("sign_method", sign_method);
		dataMap.put("ts", ts);
		dataMap.put("user_tel", user_tel);
		String data = "";
		Iterator titer = dataMap.entrySet().iterator();
		boolean first = true;
		while (titer.hasNext()) {
			Map.Entry ent = (Map.Entry) titer.next();
			String keyt = ent.getKey().toString();
			String valuet = ent.getValue().toString();
			// System.out.println(keyt+"*"+valuet);
			// 判断是否是第一个参数
			if (first) {
				data = data + keyt + "=" + valuet;
				first = false;
			} else {
				data = data + "&" + keyt + "=" + valuet;
			}

		}
		// for (int i = 0; i < dataMap.size(); i++) {
		// data = data + "&" + dataMap.get(i).toString();
		// }
		try {

			data = data + "&key="
					+ URLEncoder.encode(baiduDES_password, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("生成MD5码密钥的原文" + data);
		String sign = new MD5().Md5(data, 32);
		logger.debug("生成的MD5密钥码" + sign);
		return sign;
	}

	public static String createMD5(String record_no, String coupon_num,
			String coupon_activity_id, String user_tel, String source,
			String bank_card_num, String key) {
		TreeMap<String, String> dataMap = new TreeMap<String, String>();
		dataMap.put("record_no", record_no);
		dataMap.put("coupon_num", coupon_num);
		dataMap.put("coupon_activity_id", coupon_activity_id);
		dataMap.put("user_tel", user_tel);
		dataMap.put("source", source);
		dataMap.put("bank_card_num", bank_card_num);
		String data = "";
		Iterator titer = dataMap.entrySet().iterator();
		while (titer.hasNext()) {
			Map.Entry ent = (Map.Entry) titer.next();
			String keyt = ent.getKey().toString();
			String valuet = ent.getValue().toString();
			// System.out.println(keyt+"*"+valuet);
			data = data + "&" + keyt + "=" + valuet;
		}
		// for (int i = 0; i < dataMap.size(); i++) {
		// data = data + "&" + dataMap.get(i).toString();
		// }
		try {

			data = data + "&key=" + URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("生成MD5码的原文" + data);
		String sign = new MD5().Md5(data, 32);
		logger.debug("生成的MD5码" + sign);
		return sign;
	}

	/**
	 * 
	 * @param record_no
	 *            领券流水号
	 * @param coupon_num
	 *            银行卡号
	 * @param coupon_activity_id
	 *            票券活动id
	 * @param user_tel
	 *            用户手机号
	 * @param bank_card_num
	 *            券号
	 * @return
	 */
	public static byte[] createEncryptor(byte[] record_no, byte[] coupon_num,
			byte[] coupon_activity_id, byte[] user_tel, byte[] bank_card_num) {
		if (record_no == null || coupon_num == null
				|| coupon_activity_id == null || user_tel == null
				|| bank_card_num == null) {
			return null;
		}
		Cipher c = initEncryptorCipher(record_no, coupon_num,
				coupon_activity_id, user_tel);
		try {
			return bankCardNumdoFinal(bank_card_num, c);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] createDecryptor(byte[] record_no, byte[] coupon_num,
			byte[] coupon_activity_id, byte[] user_tel, byte[] bank_card_num_enc) {
		if (record_no == null || coupon_num == null
				|| coupon_activity_id == null || user_tel == null
				|| bank_card_num_enc == null) {
			return null;
		}
		Cipher c = initDecryptorCipher(record_no, coupon_num,
				coupon_activity_id, user_tel);
		try {
			return bankCardNumdoFinal(bank_card_num_enc, c);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Cipher initEncryptorCipher(byte[] record_no,
			byte[] coupon_num, byte[] coupon_activity_id, byte[] user_tel) {
		Cipher c = null; // 该字节数组负责保存加密的结果
		byte[] hashByte = null;

		try {
			// 生成key
			hashByte = Hash(record_no, coupon_num, coupon_activity_id, user_tel);
			// deskey = new SecretKeySpec(hashByte,
			// TriDES_ECB_PADDING.split("/")[0]);
			// 生成 Cipher对象，指定其支持DES算法
			c = Cipher.getInstance(TriDES_ECB_PADDING);
			// 根据密钥，对 Cipher 对象进行初始化,ENCRYPT_MODE表示加密模式
			SecretKey securekey = new SecretKeySpec(hashByte,
					TriDES_ECB_PADDING.split("/")[0]);

			c.init(Cipher.ENCRYPT_MODE, securekey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	private static Cipher initDecryptorCipher(byte[] record_no,
			byte[] coupon_num, byte[] coupon_activity_id, byte[] user_tel) {
		Cipher c = null; // 该字节数组负责保存加密的结果
		byte[] hashByte = null;

		try {
			// 生成key
			hashByte = Hash(record_no, coupon_num, coupon_activity_id, user_tel);
			// deskey = new SecretKeySpec(hashByte,
			// TriDES_ECB_PADDING.split("/")[0]);
			// 生成 Cipher对象，指定其支持DES算法
			c = Cipher.getInstance(TriDES_ECB_PADDING);
			// 根据密钥，对 Cipher 对象进行初始化,ENCRYPT_MODE表示加密模式
			SecretKey securekey = new SecretKeySpec(hashByte,
					TriDES_ECB_PADDING.split("/")[0]);

			c.init(Cipher.DECRYPT_MODE, securekey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * 执行加密或解密
	 * 
	 * @param bank_card_num
	 * @param c
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static byte[] bankCardNumdoFinal(byte[] bank_card_num, Cipher c)
			throws IllegalBlockSizeException, BadPaddingException {
		byte[] bank_card_num_encrypt = null;

		byte[] src = bank_card_num;
		// 加密，结果保存进 cipherByte
		bank_card_num_encrypt = c.doFinal(src);
		return bank_card_num_encrypt;
	}

}
