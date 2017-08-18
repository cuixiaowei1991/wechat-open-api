package com.cn.crypto.implement;

import org.bouncycastle.util.encoders.Base64;

import com.cn.crypto.CryptoModule;

public class CryptoBASE64Impl implements CryptoModule {
	public CryptoBASE64Impl() {
	}

	@Override
	public byte[] decode(byte[] srcBytes) throws Exception {
		byte[] resulteBytes = Base64.decode(srcBytes);
		return resulteBytes;
	}

	@Override
	public byte[] encode(byte[] srcBytes) throws Exception {
		byte[] resulteBytes = Base64.encode(srcBytes);
		return resulteBytes;
	}

	@Override
	public String decode(String src) throws Exception {
		byte[] srcBytes = src.getBytes();
		byte[] resultBytes = encode(srcBytes);
		return new String(resultBytes);
	}

	@Override
	public String encode(String src) throws Exception {
		byte[] srcBytes = src.getBytes();
		byte[] resultBytes = encode(srcBytes);
		return new String(resultBytes);
	}

}
