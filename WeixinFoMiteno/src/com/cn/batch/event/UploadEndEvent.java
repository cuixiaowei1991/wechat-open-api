package com.cn.batch.event;

import org.springframework.context.ApplicationEvent;

/**
 * 当FTP上传文件完成时，发送此事件
 * 
 * @author admin
 * 
 */
public class UploadEndEvent extends ApplicationEvent {

	/**
	 * 序列化配置
	 */
	private static final long serialVersionUID = 1L;

	public UploadEndEvent(Object source) {
		super(source);
	}

}
