package com.cn.common.implement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cn.common.PropertiesModule;

/**
 * Property�ļ�����ģ��
 * 
 * @author Administrator
 * 
 */
public class PropertiesModuleImpl implements PropertiesModule {

	/**
	 * ��־
	 */
	public static Logger logger = Logger.getLogger(PropertiesModuleImpl.class);

	/**
	 * ���Ժ���
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		PropertiesModule propertiesModuleImpl = new PropertiesModuleImpl();
		Properties properties = new Properties();
		properties.setProperty("name", "123");
		properties.setProperty("age", "345");
		properties.setProperty("address", "Beijing");
		propertiesModuleImpl.writeProperties("d:/info.properties", properties);

		// ��ȡ��������ֵ�����鷽��
		// Properties properties = propertiesModuleImpl
		// .getAllProperties("d:/info.properties");
		// Enumeration<String> enume = (Enumeration<String>) properties
		// .propertyNames();
		// while (enume.hasMoreElements()) {
		// String key = enume.nextElement();
		// String value = properties.getProperty(key);
		// logger.debug(key + "=" + value);
		// }

	}

	/**
	 * Ĭ������
	 */
	public Properties defaultProperties;

	/**
	 * checkFileIsExist ����ļ��Ƿ����
	 * 
	 * @param sourceName���ļ�·�����ļ���
	 * @return boolean
	 */
	public boolean checkFileIsExist(String sourceName) {
		File file = new File(sourceName);
		if (file.exists() && file.isFile()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * ȡ�����е�������������ֵ
	 * 
	 * @see
	 * com.cn.common.implement.PropertiesFileModule#getAllProperties(java.lang
	 * .String)
	 */
	@Override
	public Properties getAllProperties(String filePath) throws IOException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(filePath);
		InputStream inputStream = new BufferedInputStream(fileInputStream);
		properties.load(inputStream);
		return properties;
	}

	/*
	 * �õ�Ĭ�ϵ�properties��ֵ
	 * 
	 * @see com.cn.common.implement.PropertiesFileModule#getDefaultProperties()
	 */
	@Override
	public Properties getDefaultProperties() {
		return defaultProperties;
	}

	/*
	 * ����������key�õ�valueֵ
	 * 
	 * @see
	 * com.cn.common.implement.PropertiesFileModule#getPropertiesByKey(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public String getPropertiesByKey(String filePath, String key)
			throws IOException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = new FileInputStream(filePath);
		InputStream inputStream = new BufferedInputStream(fileInputStream);
		properties.load(inputStream);
		return properties.getProperty(key);
	}

	public void setDefaultProperties(Properties defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	/*
	 * ����������properties��д����дproperties�ļ�
	 * 
	 * @see
	 * com.cn.common.implement.PropertiesFileModule#writeProperties(java.lang
	 * .String, java.util.Properties)
	 */
	@Override
	public void writeProperties(String filePath, Properties properties)
			throws IOException {
		// InputStream in = new FileInputStream(filePath);
		// properties.load(in);
		OutputStream out = new FileOutputStream(filePath);
		properties.store(out, "Rewrite All Properties value");
	}

	/*
	 * ����keyֵ��д����Ӹ�keyֵ��value
	 * 
	 * @see
	 * com.cn.common.implement.PropertiesFileModule#writePropertiesByKey(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void writePropertiesByKey(String filePath, String key, String value)
			throws IOException {
		Properties properties = new Properties();
		InputStream in = new FileInputStream(filePath);
		properties.load(in);
		OutputStream out = new FileOutputStream(filePath);
		properties.setProperty(key, value);
		properties.setProperty("age", "123");
		properties.store(out, "Update '" + key + "' value");
	}
}
