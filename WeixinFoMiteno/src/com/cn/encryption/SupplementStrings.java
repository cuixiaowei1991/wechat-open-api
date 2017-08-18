package com.cn.encryption;


/**
 * @author dongyang
 *补充字符
 */
public class SupplementStrings {

	
	String buStr = "F";
	public static void main(String[] args) {
		StringBuffer allmiwen = new StringBuffer();
		SupplementStrings haha = new SupplementStrings();
		String strs = "123456";
		String allshiliu = haha.pandStrings(strs);
		System.out.println(allshiliu);
		String[] hahha =  allshiliu.toString().split(",");
		for (int i = 0; i < hahha.length; i++) {
			
//			allmiwen.append(des.DES_3(source, key, 0));
		}
		System.out.println(allmiwen.toString());
	}

	/**
	 * 补充字符为16位
	 * @param strs
	 * @return
	 */
	public String pandStrings(String strs) {
        
         
		StringBuffer buffers = new StringBuffer();
		
		int allcount = strs.length();
		if(allcount==16){
			return  strs;
		}
		for (int i = 1; i <= allcount / 16; i++) {
			// 这个就是16的倍数了
			if (allcount % 16 == 0) {
				if(i== (allcount / 16)){
					buffers.append(strs.substring((i - 1) * 16, (i * 16)));
				}else{
					buffers.append(strs.substring((i - 1) * 16, (i * 16)) + ",");
				}
				
			}
		}
		if (allcount % 16 != 0) {
			// 那这就不是16的倍数，就要分类
			float a = allcount / 16;
			String aa = String.valueOf(a);
			String hai = aa.substring(0, 1);
			if (hai.equals("0")) {
				buffers.append(strs);
				for (int b = 0; b < 16 - allcount; b++) {
					buffers.append(buStr);
				}
			} else if (!hai.equals("0")) {
				for (int j = 1; j <= a; j++) {					
					buffers.append(strs.substring((j - 1) * 16, j * 16) + ",");		
					String ss = strs.substring(j * 16, strs.length());
					if (ss.length() < 16) {
						buffers.append(ss);
						for (int c = ss.length(); c < 16; c++) {
							buffers.append(buStr);
						}
					}
				}
			}
		}
//		String[] hahha =  buffers.toString().split(",");
//		for (int i = 0; i < hahha.length; i++) {
////			allmiwen.append(jiami(hahha[i]));
//			allmiwen.append(des.DES_3(source, key, 0));
//		}
		return  buffers.toString();
	}



}
