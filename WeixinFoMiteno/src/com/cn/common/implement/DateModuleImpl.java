package com.cn.common.implement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.cn.common.DateModule;

/**
 * <p>
 * Title: ���ڴ�����
 * </p>
 * 
 * <p>
 * Description: ���ڴ�����
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
public class DateModuleImpl implements DateModule {
	/**
	 * main ����
	 * 
	 * @param params
	 *            String[]
	 * @throws ParseException
	 */
	public static void main(String[] params) throws ParseException {
		DateModuleImpl dateModule = new DateModuleImpl();
		dateModule.dateFormatString = "yyyyMMdd";
		// Date date = dateModule.convertToDate("20070801");
		// Date dayBegin = dateModule.getDayBegin(date);
		// Date dayEnd = dateModule.getDayEnd(date);
		// Date weekBegin = dateModule.getWeekBegin(date);
		// Date weekEnd = dateModule.getWeekEnd(date);
		// Date monthBegin = dateModule.getMonthBegin(date);
		// Date monthEnd = dateModule.getMonthEnd(date);
		// Date yearBegin = dateModule.getYearBegin(date);
		// Date yearEnd = dateModule.getYearEnd(date);
		// logger.debug("DayBegin:" + (dayBegin));
		// logger.debug("DayEnd:" + (dayEnd));
		// logger.debug("WeekBegin:" + (weekBegin));
		// logger.debug("WeekEnd:" + (weekEnd));
		// logger.debug("MonthBegin:" + (monthBegin));
		// logger.debug("MonthEnd:" + (monthEnd));
		// logger.debug("YearBegin:" + (yearBegin));
		// logger.debug("YearEnd:" + (yearEnd));
		// logger.debug(dateModule.convertToMedium(date));
		logger.debug(dateModule.getRemainYearToString("2011-02-01",
				"2013-01-02"));
	}

	/**
	 * dateFormatString ���ڸ�ʽ��
	 */
	public String dateFormatString;
	public static Logger logger = Logger.getLogger(DateModuleImpl.class);

	private static Calendar calS = Calendar.getInstance();

	/**
	 * ����������ʽ
	 */
	private static Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

	/**
	 * ת�� dataAndTime 2013-12-31 23:59:59 �� date 2013-12-31
	 */
	public static String getDate(String dateAndTime) {
		if (dateAndTime != null && !"".equals(dateAndTime.trim())) {
			Matcher m = p.matcher(dateAndTime);
			if (m.find()) {
				return dateAndTime.subSequence(m.start(), m.end()).toString();
			}
		}
		return "data error";
	}

	public DateModuleImpl() {
	}

	/**
	 * convertToDate ���ַ���ת������������
	 * 
	 * @param dateString
	 *            String ���������ַ���
	 * @return Date �����������͵Ķ���
	 * @throws ParseException
	 *             �׳��쳣
	 */
	@Override
	public java.util.Date convertToDate(String dateString)
			throws ParseException {
		if (dateString == null || "".equals(dateString)) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setLenient(false);
		Date timeDate = dateFormat.parse(dateString);
		return timeDate;
	}

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
	@Override
	public java.util.Date convertToDate(String dateString,
			String dateFormatString) throws ParseException {
		if (dateString == null || "".equals(dateString)) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setLenient(false);
		Date timeDate = dateFormat.parse(dateString);
		return timeDate;
	}

	/**
	 * convertToFull ��������ת��Ϊ��ȫ�����ַ�������
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	@Override
	public String convertToFull(Date date) {
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.FULL);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToFull ��������ת��Ϊ��ȫ�����ַ�������
	 * 
	 * @param dateString
	 *            Date
	 * @return String
	 * @throws ParseException
	 */
	@Override
	public String convertToFull(String dateString) throws ParseException {
		Date date = this.convertToDate(dateString);
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.FULL);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToLong ��������ת��Ϊ���������ַ�������
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	@Override
	public String convertToLong(Date date) {
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToLong ��������ת��Ϊ���������ַ�������
	 * 
	 * @param dateString
	 *            Date
	 * @return String
	 * @throws ParseException
	 */
	@Override
	public String convertToLong(String dateString) throws ParseException {
		Date date = this.convertToDate(dateString);
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToMedium ��������ת��Ϊ���С����ַ�������
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	@Override
	public String convertToMedium(Date date) {
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToMedium ��������ת��Ϊ���С����ַ�������
	 * 
	 * @param dateString
	 *            Date
	 * @return String
	 * @throws ParseException
	 */
	@Override
	public String convertToMedium(String dateString) throws ParseException {
		Date date = this.convertToDate(dateString);
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToShort ��������ת��Ϊ���̡����ַ�������
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	@Override
	public String convertToShort(Date date) {
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.SHORT);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToShort ��������ת��Ϊ���̡����ַ�������
	 * 
	 * @param dateString
	 *            Date
	 * @return String
	 * @throws ParseException
	 */
	@Override
	public String convertToShort(String dateString) throws ParseException {
		Date date = this.convertToDate(dateString);
		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.SHORT);
		String datestring = dateformat.format(date);
		return datestring;
	}

	/**
	 * convertToSQLDate ���ַ��������ڣ�ת����SQL DATE������
	 * 
	 * @param dateString
	 *            String ������ַ���������
	 * @return Date ����SQLDate������
	 * @throws ParseException
	 *             �׳��쳣
	 */
	@Override
	public java.sql.Date convertToSQLDate(String dateString)
			throws ParseException {
		if (dateString == null || "".equals(dateString)) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		dateFormat.setLenient(false);
		Date timeDate = dateFormat.parse(dateString);
		java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());
		return dateTime;
	}

	/**
	 * convertToString ����ע���ʱ���ʽ��������ת�����ַ���
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	@Override
	public String convertToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(date);
	}

	/**
	 * convertToString ����ָ����ʱ���ʽ��������ת�����ַ���
	 * 
	 * @param date
	 *            Date
	 * @param dateFormatString
	 *            String
	 * @return String
	 */
	@Override
	public String convertToString(Date date, String dateFormatString) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(date);
	}

	@Override
	public String getDateFormatString() {
		return dateFormatString;
	}

	/**
	 * getTodayBegin ���ؽ���0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ؽ���0ʱ0��0�������
	 */
	@Override
	public Date getDayBegin(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * getYesterday ���ؽ���24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ؽ���24ʱ0��0�������
	 */
	@Override
	public Date getDayEnd(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * getMonthBegin ���ص�ǰ�µ�1��0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�µ�1��0ʱ0��0�������
	 */
	@Override
	public Date getMonthBegin(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * getMonthEnd ���ص�ǰ�����1��24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�µ�1��0ʱ0��0�������
	 */
	@Override
	public Date getMonthEnd(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public String getRemainYearToString(String startDateStr, String endDateStr) {

		java.util.Date startDate = null;
		java.util.Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}

		calS.setTime(startDate);
		int startY = calS.get(Calendar.YEAR);

		calS.setTime(endDate);
		int endY = calS.get(Calendar.YEAR);
		int endM = calS.get(Calendar.MONTH);

		if (endM >= 8) {
			return "�����:" + (endY - startY) + 1;
		} else {
			return "�����:" + (endY - startY);
		}

	}

	/**
	 * ����ʣ��ʱ��
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * 
	 * @return ʣ��ʱ���ַ�
	 */
	public String getRemainDateToString(String startDateStr, String endDateStr) {
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		calS.setTime(startDate);
		int startY = calS.get(Calendar.YEAR);
		int startM = calS.get(Calendar.MONTH);
		int startD = calS.get(Calendar.DATE);
		int startDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

		calS.setTime(endDate);
		int endY = calS.get(Calendar.YEAR);
		int endM = calS.get(Calendar.MONTH);
		// ����2011-01-10��2011-01-10����Ϊ����Ϊһ��
		int endD = calS.get(Calendar.DATE) + 1;
		int endDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

		StringBuilder sBuilder = new StringBuilder();
		if (endDate.compareTo(startDate) < 0) {
			return sBuilder.append("����").toString();
		}
		int lday = endD - startD;
		if (lday < 0) {
			endM = endM - 1;
			lday = startDayOfMonth + lday;
		}
		// �����������⣬�磺2011-01-01 �� 2013-12-31 2��11����31�� ʵ���Ͼ���3��
		if (lday == endDayOfMonth) {
			endM = endM + 1;
			lday = 0;
		}
		int mos = (endY - startY) * 12 + (endM - startM);
		int lyear = mos / 12;
		int lmonth = mos % 12;
		if (lyear > 0) {
			sBuilder.append(lyear + "��");
		}
		if (lmonth > 0) {
			sBuilder.append(lmonth + "����");
		}
		if (lday > 0) {
			sBuilder.append(lday + "��");
		}
		return sBuilder.toString();
	}

	/**
	 * getWeekBegin ���ص�ǰ�ܵ�1��0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�ܵ�1��0ʱ0��0�������
	 */
	@Override
	public Date getWeekBegin(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		// ����һ�����ڵĵ�1��Ϊ����1��Ĭ����������
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * getWeekEnd ���ص�ǰ�ܵ�7��24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�ܵ�7��24ʱ0��0�������
	 */
	@Override
	public Date getWeekEnd(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		// ����һ�����ڵĵ�1��Ϊ����1��Ĭ����������
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * getYearBegin ���ص�ǰ���1��0ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ���1��0ʱ0��0�������
	 */
	@Override
	public Date getYearBegin(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * getYearEnd ���ص�ǰ�����1��24ʱ0��0�������
	 * 
	 * @param date
	 *            Date
	 * @return Date ���ص�ǰ�����1��0ʱ0��0�������
	 */
	@Override
	public Date getYearEnd(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public void setDateFormatString(String dateFormatString) {
		this.dateFormatString = dateFormatString;
	}

}
