package com.cn.common;

import java.io.IOException;
import java.util.Properties;

public interface PropertiesModule {

	/**
	 * ��ȡproperties��ȫ����Ϣ
	 * 
	 * @param �ļ�ȫ·��
	 * @throws IOException
	 */
	public abstract Properties getAllProperties(String filePath)
			throws IOException;

	public abstract Properties getDefaultProperties();

	/**
	 * ����key��ȡvalue
	 * 
	 * @param filePath �ļ�ȫ·��
	 * @param key ������
	 * @return ����keyֵ
	 * @throws IOException
	 */
	public abstract String getPropertiesByKey(String filePath, String key)
			throws IOException;

	/**
	 * д��properties��Ϣ
	 * 
	 * @param filePath�ļ�ȫ·��
	 * @param key������
	 * @param value����ֵ
	 * @throws IOException
	 */
	public abstract void writeProperties(String filePath, Properties properties)
			throws IOException;

	/**
	 * д��properties��Ϣ
	 * 
	 * @param filePath�ļ�ȫ·��
	 * @param key������
	 * @param value����ֵ
	 * @throws IOException
	 */
	public abstract void writePropertiesByKey(String filePath, String key,
			String value) throws IOException;

}