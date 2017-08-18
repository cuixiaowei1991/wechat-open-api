package com.cn.weixin.cache;

import java.util.TreeMap;

public class WeixinMerchants {
	private static TreeMap<String, MerchantTokens> merchantTokensList = new TreeMap<String, MerchantTokens>();

	public static MerchantTokens get(String sid) {
		
		try {
			return merchantTokensList.get(sid);
		} catch (NullPointerException e) {
			// TODO: handle exception
			return null;
		}
	}
	public static void put(String sid,MerchantTokens mt){
		merchantTokensList.put(sid, mt);
	}
	
}
