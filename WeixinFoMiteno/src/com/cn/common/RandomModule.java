package com.cn.common;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RandomModule {
	/**
	 * myint_09 ��������0-9
	 */
	public static int[] randomData_09 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };

	/**
	 * myint_AZ ������ĸA-Z
	 */
	public static int[] randomData_AZ = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
			75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };

	/**
	 * myint_az ������ĸa-z
	 */
	public static int[] randomData_az = { 97, 98, 99, 100, 101, 102, 103, 104, 105,
			106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
			119, 120, 121, 122 };

	/**
	 * myint_all ����A-z
	 */
	public static int[] randomData_all = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
			97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 65, 66, 67, 68,
			69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85,
			86, 87, 88, 89, 90, 108, 109, 110, 111, 112, 113, 114, 115, 116,
			117, 118, 119, 120, 121, 122 };

	/**
	 * mystring_china ���峣�ú���
	 */
	public static String[] randomData_chinese = { "��", "��", "��", "��", "��", "�",
			"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
			"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
			"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
			"��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��", "��",
			"��", "��", "��", "¬", "³", "½", "·", "��", "��", "��", "��", "÷", "��",
			"Ī", "ĸ", "��", "��", "��", "ŷ", "��", "��", "��", "��", "Ƥ", "��", "��",
			"Ǯ", "ǿ", "��", "��", "��", "��", "��", "��", "ʢ", "ʩ", "ʯ", "ʱ", "ʷ",
			"��", "��", "̷", "��", "��", "��", "��", "ͯ", "Ϳ", "��", "Σ", "Τ", "��",
			"κ", "��", "��", "��", "��", "��", "��", "��", "��", "ϯ", "��", "��", "л",
			"��", "��", "��", "��", "Ѧ", "��", "��", "��", "Ҷ", "��", "��", "��", "��",
			"��", "��", "��", "Ԫ", "Ԭ", "��", "��", "��", "ղ", "��", "��", "��", "֣",
			"��", "��", "��", "��", "��", "ׯ", "׿" };

	/**
	 * getRandomImage ���������ͼƬ
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param sRand
	 *            String ������ַ�
	 * @throws IOException
	 */
	public void getRandomImage(HttpServletRequest request,
			HttpServletResponse response, String sRand) throws IOException;

	/**
	 * getRBoolean ȡ���������ֵ
	 * 
	 * @return Boolean ����������ɵĲ���ֵ
	 */
	public boolean getRandomBoolean();

	/**
	 * getRInt ȡ�õ��������
	 * 
	 * @return int ���ص��������
	 */
	public int getRandomInt();

	/**
	 * getRInt ȡ��С��ָ����ֵ���������
	 * 
	 * @param myint
	 *            int ָ������
	 * @return int ���ص��������
	 */
	public int getRandomInt(int myint);

	/**
	 * getRStr ȡ��ָ��λ��������ַ���
	 * 
	 * @param myint
	 *            int[] ָ������
	 * @param length
	 *            int �����������λ��
	 * @return String ��������ַ���
	 */
	public String getRandomString(int[] myint, int length);

	/**
	 * getRStr ȡ��ָ��λ��������ַ���
	 * 
	 * @param myString
	 *            String[] ָ���ַ�����
	 * @param length
	 *            int �����������λ��
	 * @return String ��������ַ���
	 */
	public String getRandomString(String[] myString, int length);

	/**
	 * getUUID ȡ��32λ�ַ�����UUID�ַ���
	 * 
	 * @return String ����������ɵ�UUID�ַ���
	 */
	public String getUUID();

	/**
	 * ����ʼʱ��ͽ���ʱ���ȡ���������
	 * 
	 * @param startDate��ʼʱ��
	 * @param endDate����ʱ��
	 * @return �������
	 */
	public Date getRandomDate(Date beginDate, Date endDate);

	/**
	 * ����ʼʱ��ͽ���ʱ���ȡ���������
	 * 
	 * @param startDateString��ʼʱ��
	 * @param endDateString����ʱ��
	 * @param formatString���ڸ�ʽ
	 * @return �������
	 * @throws ParseException
	 */
	public Date getRandomDate(String beginDateString, String endDateString,
			String dateFormat) throws ParseException;
}
