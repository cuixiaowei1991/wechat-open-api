package com.cn.crypto.implement;

import org.apache.log4j.Logger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;

import com.cn.crypto.CryptoModule;

/**
 * 数字指纹算法实现
 * 
 * @author hartwell
 * @version 2.0
 */
public class CryptoDigestImpl implements CryptoModule {
	public static String MD5 = "MD5";

	public static String getMD5() {
		return MD5;
	}

	public static void setMD5(String mD5) {
		MD5 = mD5;
	}

	public static String SHA1 = "SHA1";
	public static String RIPEMD128 = "RIPEMD128";
	public static String RIPEMD160 = "RIPEMD160";
	public static String RIPEMD256 = "RIPEMD256";
	public static String RIPEMD320 = "RIPEMD320";
	/**
	 * 算法名称
	 */
	public String algorithmName;

	private Digest digest;
	/**
	 * 日志
	 */
	public static Logger logger = Logger.getLogger(CryptoDigestImpl.class);

	/**
	 * 测试用函数
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		CryptoDigestImpl cryptoDigestImpl = new CryptoDigestImpl();
		cryptoDigestImpl.setAlgorithmName(CryptoDigestImpl.MD5);
		String src = "BBC-欧洲自然写真 ";
		String result = cryptoDigestImpl.encode(src);

		CryptoBASE64Impl base64 = new CryptoBASE64Impl();
		result = base64.encode(result);

		System.out.print(result);
		// byte[] srcBytes = src.getBytes();
		// byte[] resultBytes = cryptoDigestImpl.encode(srcBytes);
		// logger.debug(new String(cryptoHexImpl.encode(resultBytes)));
	}

	@Override
	public byte[] decode(byte[] srcBytes) throws Exception {
		return null;
	}

	@Override
	public String decode(String src) throws Exception {
		return null;
	}

	@Override
	public byte[] encode(byte[] srcBytes) {
		init();
		logger.debug("DigestAlgorithm is " + algorithmName);
		digest.update(srcBytes, 0, srcBytes.length);
		byte[] resultBytes = new byte[digest.getDigestSize()];
		digest.doFinal(resultBytes, 0);
		return resultBytes;
	}

	@Override
	public String encode(String src) throws Exception {
		byte[] srcBytes = src.getBytes();
		byte[] resultBytes = encode(srcBytes);
		// return new String(resultBytes);
		// 以下生成MD5码
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < resultBytes.length; i++) {
			if (Integer.toHexString(0xFF & resultBytes[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & resultBytes[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & resultBytes[i]));
		}

		return md5StrBuff.toString();
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public Digest getDigest() {
		return digest;
	}

	public void init() {
		algorithmName = algorithmName.toUpperCase().trim();

		if (algorithmName.equals(CryptoDigestImpl.MD5)) {
			digest = new MD5Digest();
		} else if (algorithmName.equals(CryptoDigestImpl.SHA1)) {
			digest = new SHA1Digest();
		} else if (algorithmName.equals(CryptoDigestImpl.RIPEMD128)) {
			digest = new RIPEMD128Digest();
		} else if (algorithmName.equals(CryptoDigestImpl.RIPEMD160)) {
			digest = new RIPEMD160Digest();
		} else if (algorithmName.equals(CryptoDigestImpl.RIPEMD256)) {
			digest = new RIPEMD256Digest();
		} else if (algorithmName.toUpperCase().trim()
				.equals(CryptoDigestImpl.RIPEMD320)) {
			digest = new RIPEMD320Digest();
		} else {
			digest = new MD5Digest();
			logger.error("DigestAlgorithm setting error!");
		}
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setDigest(Digest digest) {
		this.digest = digest;
	}

}
