package com.cn.mitenoenc;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class Enc {	
    static String DES_CBC_NOPADDING = "DES/CBC/NoPadding";
    static String DES_CBC_PADDING = "DES/CBC/PKCS5Padding";
    static String DES_ECB_NOPADDING = "DES/ECB/NoPadding";
    static String DES_ECB_PADDING = "DES/ECB/PKCS5Padding";
    static String TriDES_CBC_NOPADDING = "DESede/CBC/NoPadding";
    static String TriDES_CBC_PADDING = "DESede/CBC/PKCS5Padding";
    static String TriDES_ECB_NOPADDING = "DESede/ECB/NoPadding";
    static String TriDES_ECB_PADDING = "DESede/ECB/PKCS5Padding";
    public int ENCRYPT_MODE = Cipher.ENCRYPT_MODE;
    public int DECRYPT_MODE = Cipher.DECRYPT_MODE;
    private int Hash(byte[] uid,byte[] regdatetime,byte[] tky) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] fac = {'s','j','l','z','c','h','1','5'};
        byte[] input=new byte[50];
        for(int i=0;i<uid.length;i++)
        	input[i]=uid[i];
        for(int j=0;j<regdatetime.length;j++)
        	input[uid.length+j]=regdatetime[j];
        for(int k=0;k<fac.length;k++)
            input[uid.length+regdatetime.length+k]=fac[k];
   //     System.out.println(new String(input,0,input.length));
        byte[] out = digest.digest(input);
        MessageDigest digestag = MessageDigest.getInstance("MD5");
        byte[] outf = digestag.digest(out);
        for(int l=0;l<outf.length;l++)
        	tky[l]=outf[l];
        return outf.length;
    }
/*
    public static int DES(byte[] input, byte[] output) throws Exception {
    	return encrypt(DES_ECB_PADDING, ENCRYPT_MODE, input, output);
    }
*/
/*   public static int EncryptTripleDes(byte[] input,byte[] output) throws Exception {
    	return encrypt(TriDES_ECB_PADDING, ENCRYPT_MODE, input, output);
    }
    */
    public int enc(byte[] uid,byte[]regdatetime,byte[] mobile,byte[] nickname,byte[] input,byte[] output) throws Exception {
    	byte[] tky=new byte[24];
    	Hash(uid,regdatetime,tky);
    	//
    	SecretKey securekey = new SecretKeySpec(tky, TriDES_ECB_PADDING.split("/")[0]);
        //
        Cipher cipher = Cipher.getInstance(TriDES_ECB_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte[] out = cipher.doFinal(input);
        System.arraycopy(out, 0 , output, 0, out.length);
        return out.length;
    }
    public int denc(byte[] uid,byte[]regdatetime,byte[] mobile,byte[] nickname,byte[] input,byte[] output) throws Exception {
    	byte[] tky=new byte[24];
    	Hash(uid,regdatetime,tky);
    	//
    	SecretKey securekey = new SecretKeySpec(tky, TriDES_ECB_PADDING.split("/")[0]);
        //
        Cipher cipher = Cipher.getInstance(TriDES_ECB_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        byte[] out = cipher.doFinal(input);
        System.arraycopy(out, 0 , output, 0, out.length);
        return out.length;
    }
    public int denc(byte[] input,byte[] tky,byte[] output) throws Exception {
    	SecretKey securekey = new SecretKeySpec(tky, TriDES_ECB_PADDING.split("/")[0]);
        //
        Cipher cipher = Cipher.getInstance(TriDES_ECB_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        byte[] out = cipher.doFinal(input);
        System.arraycopy(out, 0 , output, 0, out.length);
        return out.length;
    }
    public String LocalTime(){
		String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		return date;
	}    
/*	public static void main(String[] args) throws Exception {
        byte[] output = new byte[256];
        String uid="hc_1029";      
        int len=0;
        String time=LocalTime();
 //       len = DES(strTest.getBytes(), output);
 //       System.out.println("DES:" + new String(output, 0, len));
 //       len = encrypt(DES_ECB_PADDING, DECRYPT_MODE, Arrays.copyOf(output, len), output);
 //       System.out.println("DES:" + new String(output, 0, len));
        len = encrypt(uid.getBytes(),time.getBytes(),uid.getBytes(), output);
        System.out.println("3DES:" + new String(output, 0, len));
        len = dencrypt(uid.getBytes(),time.getBytes(),Arrays.copyOf(output, len), output);
        System.out.println("3DES:" + new String(output, 0, len));
    }
    */   
}
