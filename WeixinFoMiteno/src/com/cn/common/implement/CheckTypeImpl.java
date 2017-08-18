package com.cn.common.implement;

import java.util.List;

import org.apache.log4j.Logger;


public class CheckTypeImpl {
	/**
	 * 手机号码集合
	 */
	public List<String> mobileNumbers;
	/**
	 * 中国联通号码集合
	 */
	public List<String> unicomNumbers;
	/**
	 * 中国电信号码集合
	 */
	public List<String> telecomNumbers;
	/**
	 * 卫星通信号码集合
	 */
	public List<String> satcomNubmers;

	public Logger logger = Logger.getLogger(CheckTypeImpl.class);
//	TelaccsegmentAction ta;

	/**
	 * 根据电话号码检测运营商
	 * 
	 * @param phone
	 *            电话号码
	 * @return 运营商代码
	 */
	public int checkthePhone(String phone) {

		logger.debug("进入checkTypeImpl");
//		int resultCode = 11;
//		// 去掉前缀取得手机号码
//		if (phone.startsWith("0") || phone.startsWith("+860")) {
//			phone = phone.substring(phone.indexOf("1"), phone.length());
//		}
//		// mobile参数为空或者手机号码长度不为11，错误！
//		if (phone == null || phone.trim().length() != 11) {
//			return -1;
//		}
//		logger.debug("ta.findTelaccsegment(phone):"
//				+ ta.findTelaccsegment(phone).get(0).getIsp_id());
//		switch (ta.findTelaccsegment(phone).get(0).getIsp_id()) {
//		case 0:
//			// 检测是否为联通号段
//			resultCode = 10010;
//			break;
//		case 1:
//			// 检测是否为移动号段
//			resultCode = 10086;
//			break;
//		case 2:
//			// 检测是否为电信号段
//			resultCode = 10000;
//			break;
//		// case 3:
//		// // 检测是否为卫通号段
//		// resultCode = 11000;
//		// break;
//		default:
//			// 未知运营商
//			resultCode = 0;
//			break;
//		}
//		return resultCode;
		return 0;
	}

	/**
	 * 手机号码集合
	 */
	public List<String> getMobileNumbers() {
		return mobileNumbers;
	}

	/**
	 * 手机号码集合
	 */
	public void setMobileNumbers(List<String> mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

	/**
	 * 中国联通号码集合
	 */
	public List<String> getUnicomNumbers() {
		return unicomNumbers;
	}

	/**
	 * 中国联通号码集合
	 */
	public void setUnicomNumbers(List<String> unicomNumbers) {
		this.unicomNumbers = unicomNumbers;
	}

	/**
	 * 中国电信号码集合
	 */
	public List<String> getTelecomNumbers() {
		return telecomNumbers;
	}

	/**
	 * 中国电信号码集合
	 */
	public void setTelecomNumbers(List<String> telecomNumbers) {
		this.telecomNumbers = telecomNumbers;
	}

	/**
	 * 卫星通信号码集合
	 */
	public List<String> getSatcomNubmers() {
		return satcomNubmers;
	}

	/**
	 * 卫星通信号码集合
	 */
	public void setSatcomNubmers(List<String> satcomNubmers) {
		this.satcomNubmers = satcomNubmers;
	}

//	public TelaccsegmentAction getTa() {
//		return ta;
//	}
//
//	public void setTa(TelaccsegmentAction ta) {
//		this.ta = ta;
//	}

}
