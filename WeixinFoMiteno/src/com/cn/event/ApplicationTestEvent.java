package com.cn.event;

import org.springframework.context.ApplicationEvent;

/**
 * Spring�������¼�
 * 
 * @author admin
 * 
 */
public class ApplicationTestEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��������
	 */
	private String testData;

	/**
	 * ���캯��
	 * 
	 * @param source
	 */
	public ApplicationTestEvent(Object source) {
		super(source);
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

}
