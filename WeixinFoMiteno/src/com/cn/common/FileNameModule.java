package com.cn.common;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * Title: �ļ������ļ�·������ӿ�
 * </p>
 * 
 * <p>
 * Description: �ļ������ļ�·������ӿ�
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: com.cn.IPortal
 * </p>
 * 
 * @author LinPeng
 * @version 1.0
 */
public interface FileNameModule {
	/**
	 * checkFileIsExist ����ļ��Ƿ����
	 * 
	 * @param sourceName
	 *            String
	 * @return boolean
	 */
	public boolean checkFileIsExist(String sourceName);

	/**
	 * checkFilePath �ļ�·�����
	 * 
	 * @param sourceName
	 *            String
	 * @throws FileNotFoundException
	 */
	public void checkFilePath(String sourceName) throws FileNotFoundException;

	/**
	 * getFileExtendName ��ȡ�ļ���չ��
	 * 
	 * @param sourceName
	 *            String ������ļ�·�����ļ���
	 * @return String �����ļ���չ��
	 * @throws FileNotFoundException
	 */
	public String getFileExtendName(String sourceName)
			throws FileNotFoundException;

	/**
	 * getFileMainName ��ȡ�ļ������ļ���
	 * 
	 * @param sourceName
	 *            String ������ļ�·�����ļ���
	 * @return String �����ļ������ļ���
	 * @throws FileNotFoundException
	 */
	public String getFileMainName(String sourceName)
			throws FileNotFoundException;

	/**
	 * getFileName ��ȡ�ļ�����
	 * 
	 * @param sourceName
	 *            String ������ļ�·�����ļ���
	 * @return String �����ļ����ļ���
	 * @throws FileNotFoundException
	 */
	public String getFileName(String sourceName) throws FileNotFoundException;

	/**
	 * getFilePath ����ļ�·��
	 * 
	 * @param sourceName
	 *            String ������ļ�·�����ļ���
	 * @return String �����ļ�·��
	 * @throws FileNotFoundException
	 */
	public String getFilePath(String sourceName) throws FileNotFoundException;

	/**
	 * getPathAndFimeMainName ����ļ�·�������ļ���
	 * 
	 * @param sourceName
	 *            String
	 * @return String
	 * @throws FileNotFoundException
	 */
	public String getPathAndFileMainName(String sourceName)
			throws FileNotFoundException;

	public String getInputBase64(String sourceName)
			throws FileNotFoundException, UnsupportedEncodingException;
}
