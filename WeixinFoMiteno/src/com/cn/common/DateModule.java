package com.cn.common;

import java.text.ParseException;
import java.util.Date;

public interface DateModule {
	/**
	 * convertToDate ���ַ���ת������������
	 * 
	 * @param dateString
	 *            String ���������ַ���
	 * @return Date �����������͵Ķ���
	 * @throws ParseException
	 *             �׳��쳣
	 */
	public Date convertToDate(String dateString) throws ParseException;

	/**
	 * convertToDate ����ָ����ʽ���ַ���ת������������
	 * 
	 * @param dateString
	 *            String ���������ַ���
	 * @param dateFormatString
	 *            String
	 * @return Date �����������͵Ķ���
	 * @throws ParseException
	 *             �׳��쳣
	 */
	public Date convertToDate(String dateString, String dateFormatString)
			throws ParseException;

	/**
	 * convertToFull ��������ת��Ϊ��ȫ�����ַ�������
	 * 
	 * @param date
	 *            Date ��������
	 * @return String ȫ�������ַ���
	 */
	public String convertToFull(Date date);

	/**
	 * @param dateString
	 *            �������ַ���
	 * @return ȫ���������ַ���
	 * @throws ParseException
	 */
	public String convertToFull(String dateString) throws ParseException;

	/**
	 * @param date
	 *            ����
	 * @return �����������ַ���
	 */
	public String convertToLong(Date date);

	/**
	 * @param dateString
	 *            �������ַ���
	 * @return �����������ַ���
	 * @throws ParseException
	 */
	public String convertToLong(String dateString) throws ParseException;

	/**
	 * convertToMedium ��������ת��Ϊ���С����ַ�������
	 * 
	 * @param date 
	 * @return String
	 */
	public String convertToMedium(Date date);

	/**
	 * convertToMedium ��������ת��Ϊ���С����ַ�������
	 * 
	 * @param dateString
	 *            Date
	 * @return String
	 * @throws ParseException
	 */
	public String convertToMedium(String dateString) throws ParseException;

	/**
	 * convertToShort ��������ת��Ϊ���̡����ַ�������
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public String convertToShort(Date date);

	/**
	 * convertToShort ��������ת��Ϊ���̡����ַ�������
	 * 
	 * @param dateString
	 *            Date
	 * @return String
	 * @throws ParseException
	 */
	public String convertToShort(String dateString) throws ParseException;

	/**
	 * convertToSQLDate ���ַ��������ڣ�ת����SQL DATE������
	 * 
	 * @param dateString
	 *            String ������ַ���������
	 * @return Date ����SQLDate������
	 * @throws ParseException
	 *             �׳��쳣
	 */
	public java.sql.Date convertToSQLDate(String dateString)
			throws ParseException;

	/**
	 * convertToString ����ע���ʱ���ʽ��������ת�����ַ���
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public String convertToString(Date date);

	/**
	 * convertToString ����ָ����ʱ���ʽ��������ת�����ַ���
	 * 
	 * @param date
	 *            Date
	 * @param dateFormatString
	 *            String
	 * @return String
	 */
	public String convertToString(Date date, String dateFormatString);

	public String getDateFormatString();

	/**
	 * getTodayBegin ���ؽ���0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ؽ���0ʱ0��0�������
	 */
	public Date getDayBegin(Date date);

	/**
	 * getYesterday ���ؽ���24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ؽ���24ʱ0��0�������
	 */
	public Date getDayEnd(Date date);

	/**
	 * getMonthBegin ���ص�ǰ�µ�1��0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�µ�1��0ʱ0��0�������
	 */
	public Date getMonthBegin(Date date);

	/**
	 * getMonthEnd ���ص�ǰ�����1��24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�µ�1��0ʱ0��0�������
	 */
	public Date getMonthEnd(Date date);

	/**
	 * getWeekBegin ���ص�ǰ�ܵ�1��0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�ܵ�1��0ʱ0��0�������
	 */
	public Date getWeekBegin(Date date);

	/**
	 * getWeekEnd ���ص�ǰ�ܵ�7��24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�ܵ�7��24ʱ0��0�������
	 */
	public Date getWeekEnd(Date date);

	/**
	 * getYearBegin ���ص�ǰ���1��0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ���1��0ʱ0��0�������
	 */
	public Date getYearBegin(Date date);

	/**
	 * getYearEnd ���ص�ǰ�����1��24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�����1��0ʱ0��0�������
	 */
	public Date getYearEnd(Date date);

}
