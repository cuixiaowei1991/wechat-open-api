package com.cn.common.implement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cn.common.XmlModule;

/**
 * <p>
 * Title: Xml�ļ�����ģ��
 * </p>
 * 
 * <p>
 * Description: Xml�ļ�����ģ��
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: com.cn
 * </p>
 * 
 * @author Hartwell
 * @version 1.0
 */
public class XmlModuleImpl implements XmlModule {
	public static Logger logger = Logger.getLogger(XmlModuleImpl.class);

	/**
	 * main ����ʵ��
	 * 
	 * @param args
	 *            String[]
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws DocumentException,
			IOException {
		XmlModuleImpl xmlModule = new XmlModuleImpl();
		Document document = xmlModule.createXml("root");
		xmlModule.addAttribute(document, "//root", "id", "123");
		xmlModule.addElement(document, "//root", "property", "1235");
		xmlModule.addAttribute(document, "//root//property", "name", "first");
		xmlModule.addElement(document, "//root", "property", "12356");
		xmlModule.writeXml("d:\\a.xml", document);
		List<Element> list = xmlModule.readElements("d:\\a.xml",
				"//root//property[@name='second']");
		for (Iterator<Element> it = list.iterator(); it.hasNext();) {
			Element element = it.next();
			logger.debug(element.getText());
		}
	}

	/**
	 * xPath ����·��
	 */
	public String xPath;

	/**
	 * list ���ؽ����
	 */
	public List<?> list;

	/**
	 * encode ���ֱ���
	 */
	public String encode;

	/**
	 * formatType xml�ļ������ʽ
	 */
	public String formatType;

	public XmlModuleImpl() {
	}

	/**
	 * addAttribute ����Ԫ�ص�����
	 * 
	 * @param document
	 *            Document
	 * @param xPath
	 *            String
	 * @param attributeName
	 *            String
	 * @param attributeValue
	 *            String
	 * @throws DocumentException
	 * @return Document
	 */
	@Override
	public Document addAttribute(Document document, String xPath,
			String attributeName, String attributeValue)
			throws DocumentException {
		List<?> list = document.selectNodes(xPath);
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			element.addAttribute(attributeName, attributeValue);
		}
		return document;
	}

	/**
	 * addElement ����Ԫ��
	 * 
	 * @param document
	 *            Document
	 * @param xPath
	 *            String
	 * @param elementName
	 *            String
	 * @param elementValue
	 *            String
	 * @throws DocumentException
	 * @return Document
	 */
	@Override
	public Document addElement(Document document, String xPath,
			String elementName, String elementValue) throws DocumentException {
		List<?> list = document.selectNodes(xPath);
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			Element newElement = element.addElement(elementName);
			newElement.setText(elementValue);
		}
		return document;
	}

	/**
	 * addElementAndAttribute ����Ԫ�غ�����
	 * 
	 * @param document
	 *            Document
	 * @param xPath
	 *            String
	 * @param elementName
	 *            String
	 * @param elementValue
	 *            String
	 * @param attributeName
	 *            String
	 * @param attributeValue
	 *            String
	 * @return Document
	 */
	@Override
	public Document addElementAndAttribute(Document document, String xPath,
			String elementName, String elementValue, String attributeName,
			String attributeValue) {

		List<?> list = document.selectNodes(xPath);
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			Element newElement = element.addElement(elementName);
			newElement.setText(elementValue);
			newElement.addAttribute(attributeName, attributeValue);
		}
		return document;
	}

	/**
	 * createXml ����xml�ļ�
	 * 
	 * @param root
	 *            String
	 * @throws IOException
	 * @return Document
	 */
	@Override
	public Document createXml(String root) throws IOException {
		Document document = DocumentHelper.createDocument();
		document.addElement(root);
		return document;
	}

	public String getEncode() {
		return encode;
	}

	public String getFormatType() {
		return formatType;
	}

	public List<?> getList() {
		return list;
	}

	/**
	 * readAttributes ��ȡxml����
	 * 
	 * @param document
	 *            String
	 * @param xPath
	 *            String
	 * @return List
	 * @throws DocumentException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Attribute> readAttributes(Document document, String xPath) {
		List<Attribute> attributes = document.selectNodes(xPath);
		return attributes;
	}

	/**
	 * readAttributes ��ȡxml����
	 * 
	 * @param filename
	 *            String
	 * @param xPath
	 *            String
	 * @return List
	 * @throws DocumentException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Attribute> readAttributes(String filename, String xPath)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(filename);
		List<?> attributes = document.selectNodes(xPath);
		return (List<Attribute>) attributes;
	}

	/**
	 * readDocument ��ȡxml�ļ�
	 * 
	 * @param file
	 *            String
	 * @return Document
	 * @throws DocumentException
	 */
	@Override
	public Document readDocument(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

	/**
	 * readDocument ��ȡxml�ļ�
	 * 
	 * @param filename
	 *            String
	 * @return Document
	 * @throws DocumentException
	 */
	@Override
	public Document readDocument(String filename) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(filename);
		return document;
	}

	/**
	 * readElements ��ȡxmlԪ��
	 * 
	 * @param document
	 *            String
	 * @param xPath
	 *            String
	 * @return List
	 * @throws DocumentException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Element> readElements(Document document, String xPath) {
		List<Element> elements = document.selectNodes(xPath);
		return elements;
	}

	/**
	 * readElements ��ȡxmlԪ��
	 * 
	 * @param filename
	 *            String
	 * @param xPath
	 *            String
	 * @return List
	 * @throws DocumentException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Element> readElements(String filename, String xPath)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(filename);
		List<Element> elements = document.selectNodes(xPath);
		return elements;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	/**
	 * writeXml д��xml�ļ�
	 * 
	 * @param filename
	 *            String
	 * @param document
	 *            Document
	 * @throws IOException
	 */
	@Override
	public void writeXml(String filename, Document document) throws IOException {
		OutputFormat format;
		if (this.formatType == "Pretty") {
			format = OutputFormat.createPrettyPrint();
		} else if (this.formatType == "Compact") {
			format = OutputFormat.createCompactFormat();
		} else {
			format = OutputFormat.createCompactFormat();
		}
		format.setEncoding(encode);
		XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)),
				format);
		writer.write(document);
		writer.close();
	}

}
