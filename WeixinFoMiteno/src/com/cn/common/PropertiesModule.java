package com.cn.common;

import java.io.IOException;
import java.util.Properties;

public interface PropertiesModule {

	/**
	 * 读取properties的全部信息
	 * 
	 * @param 文件全路径
	 * @throws IOException
	 */
	public abstract Properties getAllProperties(String filePath)
			throws IOException;

	public abstract Properties getDefaultProperties();

	/**
	 * 根据key读取value
	 * 
	 * @param filePath 文件全路径
	 * @param key 属性名
	 * @return 返回key值
	 * @throws IOException
	 */
	public abstract String getPropertiesByKey(String filePath, String key)
			throws IOException;

	/**
	 * 写入properties信息
	 * 
	 * @param filePath文件全路径
	 * @param key属性名
	 * @param value属性值
	 * @throws IOException
	 */
	public abstract void writeProperties(String filePath, Properties properties)
			throws IOException;

	/**
	 * 写入properties信息
	 * 
	 * @param filePath文件全路径
	 * @param key属性名
	 * @param value属性值
	 * @throws IOException
	 */
	public abstract void writePropertiesByKey(String filePath, String key,
			String value) throws IOException;

}