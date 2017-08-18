package com.cn.mitenoenc;



import javax.crypto.Cipher; 
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException; 
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException; 

public class Encrypt {
	//KeyGenerator提供对称密钥生成器的功能，支持各种算法
	private KeyGenerator keygen;      //SecretKey负责保存对称密钥
	private SecretKey deskey;       //Cipher负责完成加密或解密工作
	private Cipher c;      //该字节数组负责保存加密的结果
	private byte[] cipherByte; 
	public Encrypt(){
	//	 Security.addProvider(new javax.crypto.provider.SunJCE());   
		 try{           //实例化支持DES算法的密钥生成器 (算法名称命名需按规定，否则抛出异常 ) 
			 keygen = KeyGenerator.getInstance("DES"); 
		   // 生成密钥
			 deskey = keygen.generateKey();             
		 //生成 Cipher对象，指定其支持DES算法
			 c = Cipher.getInstance("DES");
		 }
		 catch(NoSuchAlgorithmException ex) {
			 ex.printStackTrace();
		 }          
		 catch(NoSuchPaddingException ex) {
			 ex.printStackTrace();
		 } 
	 } 
	public byte[] createEncryptor(String str)  { 
		try  {       
			// 根据密钥，对 Cipher 对象进行初始化,ENCRYPT_MODE表示加密模式
			c.init(Cipher.ENCRYPT_MODE, deskey); 
			byte[] src = str.getBytes(); 
			// 加密，结果保存进 cipherByte   
			cipherByte = c.doFinal(src);  
		 } 
		catch(java.security.InvalidKeyException ex) {                                 
			ex.printStackTrace();
		}   
		catch(javax.crypto.BadPaddingException ex) {
			ex.printStackTrace();
		}     
		catch(javax.crypto.IllegalBlockSizeException ex) {
			ex.printStackTrace();
		} 
		return cipherByte;
	}
	public byte[] createDecryptor(byte[] buff)  {
	    try  {//根据密钥，对Cipher对象进行初始化,ENCRYPT_MODE表示解密模式
	    	c.init(Cipher.DECRYPT_MODE, deskey);             
	    	//得到明文，存入cipherByte字符数组
	    	cipherByte = c.doFinal(buff);
	    }
	    catch(java.security.InvalidKeyException ex){
	    	ex.printStackTrace();         
	    }
	    catch(javax.crypto.BadPaddingException ex) {  
	    	ex.printStackTrace();	
	    }         
	    catch(javax.crypto.IllegalBlockSizeException ex) {
	    	ex.printStackTrace();
	    }
	    return cipherByte;
	} 
	 public static void main(String[] args) throws Exception { 
		 Encrypt encr = new Encrypt();       
		 String msg = "哎我去啊，大侠";         
		 System.out.println("明文是：" + msg); 
		 byte[] enc = encr.createEncryptor(msg);
		 System.out.println("密文是：" + new String(enc));
		 byte[] dec = encr.createDecryptor(enc);
		 System.out.println("解密后的结果是：" + new String(dec));
	} 
}
