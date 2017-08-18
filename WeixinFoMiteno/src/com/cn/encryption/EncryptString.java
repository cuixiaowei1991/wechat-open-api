package com.cn.encryption;

/**
 * @author dongyang 加密类
 */
public class EncryptString {

	// 主密钥
	private static String Key = "1A8F01BA670E04B90E230B57197F61C2";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EncryptString es = new EncryptString();

		SupplementStrings ss = new SupplementStrings();

		// String strs = "ddddhhhttt1156";
		// String strs2 = "ddddhhhttt1156"+"{13426364664}";
		// System.out.println("strs2： "+strs2);
		// String toMd5 = es.strToMD5(strs2,32);
		// System.out.println("登录密码转md5后： "+toMd5);
		//
		//
		// String strs1 = "123456";
		// ss = new SupplementStrings();
		// String ss_16 = ss.pandStrings(strs1);
		// System.out.println("支付密码补位后： "+ss_16);
		// String allshiliu1 = es.encryptAndDiscrypt(ss_16,0);
		// System.out.println("支付密码加密后： "+allshiliu1);
		//
		//
		//
		// String allshiliu2 = es.encryptAndDiscrypt(allshiliu1,1);
		// System.out.println("支付密码解密后： "+allshiliu2);
		//
		//
		//
		// String emd5 = es.encryptMd5(toMd5);
		// String disemd5 = es.disEncryptMd5(emd5);
		// System.out.println("登录密码转md5后对比0： "+toMd5+"    字符串长度是"+toMd5.length());
		// System.out.println("登录密码转md5后对比1: "+disemd5+"    字符串长度是"+disemd5.length());

		// 将原文转md5码32位
		String aaa = "6222000200108356817";
		String toMd5 = es.strToMD5(aaa, 32);
		System.out.println("转md5后32位码: " + toMd5);
		// 取md5码前8位和后8位当key

		String key16 = ss.pandStrings(aaa);
		;
		String[] hahha = key16.toString().split(",");
		// 将原文加密
		String hahha0 = DES.DES_3(hahha[0], toMd5, 0);
		String hahha1 = DES.DES_3(hahha[1], toMd5, 0);
		// String hahha0 =DES.DES_3("6222000200108356",toMd5,0);
		// String hahha1 =DES.DES_3("8170000000000000",toMd5,0);
		String miwen = hahha0 + hahha1;
		System.out.println("加密后密文: " + miwen);

		// 将密文解密
		String yuanwen = es.encryptAndDiscrypt1(miwen, toMd5, 1);
		System.out.println("解密后原文: " + yuanwen);

		// //登录密码加密存本地密文
		// String ssBM = ss.pandStrings(toMd5);
		// System.out.println("登录密码MD5补位后： "+ssBM);
		//
		// StringBuffer allmiwenEnd = new StringBuffer();
		//
		//
		// // String strHEX =DES.ASC_2_HEX(toMd5);
		// // System.out.println("strHEX: "+strHEX);
		// //
		// // String strASC =DES.HEX_2_ASC(strHEX);
		// // System.out.println("strASC: "+strASC.toLowerCase());
		//
		//
		// String[] hahha = ssBM.toString().split(",");
		// //先加密
		// // for (int i = 0; i < hahha.length; i++) {
		// String hahha0 =DES.DES_3(hahha[0],Key,0);
		// String hahha1 =DES.DES_3(hahha[1],Key,0);
		// System.out.println("allmiwenKey0: "+hahha0);
		// System.out.println("allmiwenKey1: "+hahha1);
		// // allmiwen.append(DES.DES_3(hahha[i],Key,0));
		// //// }
		//
		//
		// //登录密码取本地密文解密
		//
		// String hahha0Dis =DES.DES_3(hahha0,Key,1);
		// String hahha1Dis =DES.DES_3(hahha1,Key,1);
		//
		// System.out.println("hahha0Dis: "+hahha0Dis.toLowerCase());
		// System.out.println("hahha1Dis: "+hahha1Dis.toLowerCase());
		// System.out.println("登录密码转md5后对比0： "+toMd5);
		// System.out.println("登录密码转md5后对比1: "+hahha0Dis.toLowerCase()+hahha1Dis.toLowerCase());

		// System.out.println("手机号补位："+ss.pandStrings("13146185005"));
		// String aaa =
		// encryptAndDiscryptToMobile("6666666677777777","13146185005",0);
		// System.out.println("加密："+aaa);
		// System.out.println("解密："+encryptAndDiscryptToMobile(aaa,"13146185005",1));
	}

	/**
	 * 
	 * @param str
	 * @return 加密MD5码本地持久
	 */
	public static String encryptMd5(String strMD5) {
		SupplementStrings ss = new SupplementStrings();

		String ssBM = ss.pandStrings(strMD5);
		String[] hahha = ssBM.toString().split(",");
		// 先加密

		String hahha0 = DES.DES_3(hahha[0], Key, 0);
		String hahha1 = DES.DES_3(hahha[1], Key, 0);
		// System.out.println("allmiwenKey0: "+hahha0);
		// System.out.println("allmiwenKey1: "+hahha1);

		return hahha0 + hahha1;
	}

	/**
	 * 解密本地存储密文为md5码
	 * 
	 * @param str
	 * @return
	 */
	public static String disEncryptMd5(String str) {
		SupplementStrings ss = new SupplementStrings();
		String ssBM = ss.pandStrings(str);
		String[] hahha = ssBM.toString().split(",");

		String hahha0Dis = DES.DES_3(hahha[0], Key, 1);
		String hahha1Dis = DES.DES_3(hahha[1], Key, 1);

		return (hahha0Dis + hahha1Dis).toLowerCase();
	}

	/**
	 * 字符串转md5码
	 * 
	 * @return str 是原文 number 16 是转16位md5 ，32是转32位md5
	 * 
	 */
	public static String strToMD5(String str, int number) {
		MD5 md5 = new MD5();
		String md5Str = md5.Md5(str, number);
		return md5Str;
	}

	/**
	 * 加密解密方法
	 * 
	 * @param str
	 * @return num： 0:encrypt加密 1:discrypt 解密
	 */
	public static String encryptAndDiscrypt(String str, int num) {

		String temp = DES.DES_3(str, Key, num);

		return temp;
	}

	/**
	 * 加密解密方法1
	 * 
	 * @param str
	 * @return num： 0:encrypt加密 1:discrypt 解密
	 */
	public static String encryptAndDiscrypt1(String str, String Key, int num) {

		String temp = DES.DES_3(str, Key, num);

		return temp;
	}

	/**
	 * 
	 * @param str
	 *            内容16位
	 * @param key
	 *            密钥
	 * @param num
	 *            0:encrypt加密 1:discrypt 解密
	 */
	public static String encryptAndDiscryptToMobile(String str, String key,
			int num) {
		SupplementStrings ss = new SupplementStrings();
		String ss_16 = ss.pandStrings(key); // 补充16位
		String temp = DES.DES_3(str, ss_16 + ss_16, num);

		return temp;
	}

}
