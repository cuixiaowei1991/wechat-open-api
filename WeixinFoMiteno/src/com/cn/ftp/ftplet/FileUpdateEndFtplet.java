package com.cn.ftp.ftplet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cn.batch.event.UploadEndEvent;

/**
 * �ļ��ϴ���ɺ�Ĵ�����
 * 
 * @author admin
 * 
 */
public class FileUpdateEndFtplet extends DefaultFtplet implements
		ApplicationContextAware {
	/**
	 * ��־
	 */
	public Logger logger = Logger.getLogger(FileUpdateEndFtplet.class);

	/**
	 * ��ȡspring����
	 */
	public ApplicationContext applicationContext;
	private String homeDirectory;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	/*
	 * ��������ʱ����
	 * 
	 * @see
	 * org.apache.ftpserver.ftplet.DefaultFtplet#onConnect(org.apache.ftpserver
	 * .ftplet.FtpSession)
	 */
	@Override
	public FtpletResult onConnect(FtpSession session) {
		return FtpletResult.DEFAULT;
	}

	@Override
	public FtpletResult onDisconnect(FtpSession session) throws FtpException,
			IOException {
		return super.onDisconnect(session);
	}

	/*
	 * �������ʱ����
	 * 
	 * @see
	 * org.apache.ftpserver.ftplet.DefaultFtplet#onDownloadEnd(org.apache.ftpserver
	 * .ftplet.FtpSession, org.apache.ftpserver.ftplet.FtpRequest)
	 */
	@Override
	public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) {

		return FtpletResult.DEFAULT;
	}

	/*
	 * �û���¼ʱ����
	 * 
	 * @see
	 * org.apache.ftpserver.ftplet.DefaultFtplet#onLogin(org.apache.ftpserver
	 * .ftplet.FtpSession, org.apache.ftpserver.ftplet.FtpRequest)
	 */
	@Override
	public FtpletResult onLogin(FtpSession session, FtpRequest request) {

		String homeDirectory = session.getUser().getHomeDirectory();
		File home = new File(homeDirectory);
		if (home.exists() == false && home.isDirectory()==false) {
			logger.debug("The home directory is not exit. Let's make it...");
			logger.debug(home.mkdir());
		}
		return FtpletResult.DEFAULT;
	}

	/*
	 * �ϴ����ʱ������ͨ��spring���������¼���Ϣ
	 * 
	 * @see
	 * org.apache.ftpserver.ftplet.DefaultFtplet#onUploadEnd(org.apache.ftpserver
	 * .ftplet.FtpSession, org.apache.ftpserver.ftplet.FtpRequest)
	 */
	@Override
	public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) {
		String username = session.getUser().getName();
		String input = session.getUser().getHomeDirectory() + "/"
				+ request.getArgument();
		String homeDirectory = session.getUser().getHomeDirectory();
		/**
		 * ����spring�¼�
		 */
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("homeDirectory", homeDirectory);
		map.put("input", input);
		logger.debug(map);
		applicationContext.publishEvent(new UploadEndEvent(map));

		return FtpletResult.DEFAULT;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

}
