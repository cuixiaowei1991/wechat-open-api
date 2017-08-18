package com.cn.common;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * Title: 文件名及文件路径处理接口
 * </p>
 * 
 * <p>
 * Description: 文件名及文件路径处理接口
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
	 * checkFileIsExist 检查文件是否存在
	 * 
	 * @param sourceName
	 *            String
	 * @return boolean
	 */
	public boolean checkFileIsExist(String sourceName);

	/**
	 * checkFilePath 文件路径检查
	 * 
	 * @param sourceName
	 *            String
	 * @throws FileNotFoundException
	 */
	public void checkFilePath(String sourceName) throws FileNotFoundException;

	/**
	 * getFileExtendName 获取文件扩展名
	 * 
	 * @param sourceName
	 *            String 输入带文件路径的文件名
	 * @return String 返回文件扩展名
	 * @throws FileNotFoundException
	 */
	public String getFileExtendName(String sourceName)
			throws FileNotFoundException;

	/**
	 * getFileMainName 获取文件的主文件名
	 * 
	 * @param sourceName
	 *            String 输入带文件路径的文件名
	 * @return String 返回文件的主文件名
	 * @throws FileNotFoundException
	 */
	public String getFileMainName(String sourceName)
			throws FileNotFoundException;

	/**
	 * getFileName 获取文件名称
	 * 
	 * @param sourceName
	 *            String 输入带文件路径的文件名
	 * @return String 返回文件的文件名
	 * @throws FileNotFoundException
	 */
	public String getFileName(String sourceName) throws FileNotFoundException;

	/**
	 * getFilePath 获得文件路径
	 * 
	 * @param sourceName
	 *            String 输入带文件路径的文件名
	 * @return String 返回文件路径
	 * @throws FileNotFoundException
	 */
	public String getFilePath(String sourceName) throws FileNotFoundException;

	/**
	 * getPathAndFimeMainName 获得文件路径和主文件名
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
