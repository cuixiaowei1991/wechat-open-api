package com.cn.common.implement;

import java.util.List;

import org.apache.log4j.Logger;


public class CheckTypeImpl {
	/**
	 * �ֻ����뼯��
	 */
	public List<String> mobileNumbers;
	/**
	 * �й���ͨ���뼯��
	 */
	public List<String> unicomNumbers;
	/**
	 * �й����ź��뼯��
	 */
	public List<String> telecomNumbers;
	/**
	 * ����ͨ�ź��뼯��
	 */
	public List<String> satcomNubmers;

	public Logger logger = Logger.getLogger(CheckTypeImpl.class);
//	TelaccsegmentAction ta;

	/**
	 * ���ݵ绰��������Ӫ��
	 * 
	 * @param phone
	 *            �绰����
	 * @return ��Ӫ�̴���
	 */
	public int checkthePhone(String phone) {

		logger.debug("����checkTypeImpl");
//		int resultCode = 11;
//		// ȥ��ǰ׺ȡ���ֻ�����
//		if (phone.startsWith("0") || phone.startsWith("+860")) {
//			phone = phone.substring(phone.indexOf("1"), phone.length());
//		}
//		// mobile����Ϊ�ջ����ֻ����볤�Ȳ�Ϊ11������
//		if (phone == null || phone.trim().length() != 11) {
//			return -1;
//		}
//		logger.debug("ta.findTelaccsegment(phone):"
//				+ ta.findTelaccsegment(phone).get(0).getIsp_id());
//		switch (ta.findTelaccsegment(phone).get(0).getIsp_id()) {
//		case 0:
//			// ����Ƿ�Ϊ��ͨ�Ŷ�
//			resultCode = 10010;
//			break;
//		case 1:
//			// ����Ƿ�Ϊ�ƶ��Ŷ�
//			resultCode = 10086;
//			break;
//		case 2:
//			// ����Ƿ�Ϊ���źŶ�
//			resultCode = 10000;
//			break;
//		// case 3:
//		// // ����Ƿ�Ϊ��ͨ�Ŷ�
//		// resultCode = 11000;
//		// break;
//		default:
//			// δ֪��Ӫ��
//			resultCode = 0;
//			break;
//		}
//		return resultCode;
		return 0;
	}

	/**
	 * �ֻ����뼯��
	 */
	public List<String> getMobileNumbers() {
		return mobileNumbers;
	}

	/**
	 * �ֻ����뼯��
	 */
	public void setMobileNumbers(List<String> mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

	/**
	 * �й���ͨ���뼯��
	 */
	public List<String> getUnicomNumbers() {
		return unicomNumbers;
	}

	/**
	 * �й���ͨ���뼯��
	 */
	public void setUnicomNumbers(List<String> unicomNumbers) {
		this.unicomNumbers = unicomNumbers;
	}

	/**
	 * �й����ź��뼯��
	 */
	public List<String> getTelecomNumbers() {
		return telecomNumbers;
	}

	/**
	 * �й����ź��뼯��
	 */
	public void setTelecomNumbers(List<String> telecomNumbers) {
		this.telecomNumbers = telecomNumbers;
	}

	/**
	 * ����ͨ�ź��뼯��
	 */
	public List<String> getSatcomNubmers() {
		return satcomNubmers;
	}

	/**
	 * ����ͨ�ź��뼯��
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
