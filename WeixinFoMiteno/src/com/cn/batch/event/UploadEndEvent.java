package com.cn.batch.event;

import org.springframework.context.ApplicationEvent;

/**
 * ��FTP�ϴ��ļ����ʱ�����ʹ��¼�
 * 
 * @author admin
 * 
 */
public class UploadEndEvent extends ApplicationEvent {

	/**
	 * ���л�����
	 */
	private static final long serialVersionUID = 1L;

	public UploadEndEvent(Object source) {
		super(source);
	}

}
