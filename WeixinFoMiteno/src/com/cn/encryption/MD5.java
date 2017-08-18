package com.cn.encryption;




	import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

	/**
	 * 转MD5码
	 * @author dongyang
	 *
	 */
	public class MD5 { 

	        
	      //md5加密
	        /**
	         * 转MD5
	         * @param sourceStr
	         * @return
	         */
	        public static String Md5(String sourceStr,int number) { 
	         String  result = "";
	         try { 
	          MessageDigest md = MessageDigest.getInstance("MD5"); 
	          md.update(sourceStr.getBytes()); 
	          byte b[] = md.digest(); 
	          int i; StringBuffer buf = new StringBuffer(""); 
	          for (int offset = 0; offset < b.length; offset++) { 
	           i = b[offset]; 
	           if(i<0) 
	            i+= 256; 
	           if(i<16) 
	            buf.append("0"); 
	            buf.append(Integer.toHexString(i)); 
	          } 
	          if(number == 16){
	        	  result = buf.toString().substring(8,24);
	          }
	          
	          if(number == 32){
	        	  result = buf.toString();
	          }
	          
//	          mylog.vv("result: " + result);//32位的加密 
//	          System.out.println("result: " + result);//32位的加密 
//	          System.out.println("result: " + buf.toString().substring(8,24));//16位的加密 
	         } catch (NoSuchAlgorithmException e) { 
	          // TODO Auto-generated catch block e.printStackTrace(); 
	         } 
	         return result;
	        }
	}


