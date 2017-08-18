package com.cn.common.implement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cn.common.RandomModule;

/**
 * ������ݴ���ģ��ʵ��
 * 
 * 
 * ������ݴ���ģ��ʵ��
 * 
 * Copyright: Copyright (c) 2008 Company: com.cn 2010-03-29�����޸�����������ں���
 * 
 * @author Hartwell
 * @version 2.0
 */
public class RandomModuleImpl implements RandomModule {
	public static Logger logger = Logger.getLogger(RandomModuleImpl.class);

	public static Random random = new Random();

	public RandomModuleImpl() {
	}

	/**
	 * ����ʼʱ��ͽ���ʱ���ȡ���������
	 * 
	 * @param startDate��ʼʱ��
	 * @param endDate����ʱ��
	 * @return ���ʱ��
	 */
	@Override
	public Date getRandomDate(Date beginDate, Date endDate) {
		long begin = beginDate.getTime();
		long end = endDate.getTime();
		Date date = new Date(begin
				+ (long) (random.nextDouble() * (end - begin)));
		return date;
	}

	/**
	 * ����ʼʱ��ͽ���ʱ���ȡ���������
	 * 
	 * @param startDateString��ʼʱ��
	 * @param endDateString����ʱ��
	 * @param formatString���ڸ�ʽ
	 * @return ���ʱ��
	 * @throws ParseException
	 */
	@Override
	public Date getRandomDate(String beginDateString, String endDateString,
			String dateFormat) throws ParseException {
		DateFormat format = new SimpleDateFormat(dateFormat);
		Date startDate = format.parse(beginDateString);
		Date endDate = format.parse(endDateString);
		Date date = this.getRandomDate(startDate, endDate);
		return date;
	}

	/**
	 * getRandColor ȡ�������ɫ
	 * 
	 * @param fcǰ̨��ɫ
	 * @param bc��̨��ɫ
	 * @return Color
	 */
	private Color getRandomColor(int fc, int bc) {
		// Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

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
	@Override
	public void getRandomImage(HttpServletRequest request,
			HttpServletResponse response, String sRand) throws IOException {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		int length = sRand.length();
		// ���ڴ��д���ͼ��
		int width = 20 * length;
		int height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// ��ȡͼ��������
		Graphics g = image.getGraphics();

		// �趨����ɫ
		g.setColor(getRandomColor(200, 250));
		g.fillRect(0, 0, width, height);

		// �趨����
		g.setFont(new Font("Times New Roman", Font.BOLD, 20));

		// �������155�������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽
		g.setColor(getRandomColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		for (int i = 0; i < length; i++) {
			String rand = sRand.substring(i, i + 1);
			// ����֤����ʾ��ͼ����
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i, 16);
		}

		// ͼ����Ч
		g.dispose();
		ServletOutputStream responseOutputStream = response.getOutputStream();
		// ���ͼ��ҳ��
		ImageIO.write(image, "JPEG", responseOutputStream);
		// ���¹ر���������
		responseOutputStream.flush();

		responseOutputStream.close();
	}

	/**
	 * getRBoolean ȡ���������ֵ
	 * 
	 * @return Boolean ����������ɵĲ���ֵ
	 */
	@Override
	public boolean getRandomBoolean() {
		boolean myRBoolean = random.nextBoolean();
		return myRBoolean;
	}

	/**
	 * getRInt ȡ�õ��������
	 * 
	 * @return int ���ص��������
	 */
	@Override
	public int getRandomInt() {
		return random.nextInt();
	}

	/**
	 * getRInt ȡ��С��ָ����ֵ���������
	 * 
	 * @param myintָ������
	 * @return int ���ص��������
	 */
	@Override
	public int getRandomInt(int maxint) {
		return random.nextInt(maxint);
	}

	/**
	 * getRStr ȡ��ָ��λ��������ַ���
	 * 
	 * @param myint
	 *            int[] ָ������
	 * @param length
	 *            int �����������λ��
	 * @return String ��������ַ���
	 */
	@Override
	public String getRandomString(int[] myint, int length) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i = i + 1) {
			int r = random.nextInt(myint.length);
			buffer.append((char) myint[r]);
		}
		return buffer.toString();
	}

	/**
	 * getRStr ȡ��ָ��λ��������ַ���
	 * 
	 * @param myString
	 *            String[] ָ���ַ�����
	 * @param length
	 *            int �����������λ��
	 * @return String ��������ַ���
	 */
	@Override
	public String getRandomString(String[] myString, int length) {
		StringBuffer mystrbuf = new StringBuffer();
		for (int i = 0; i < length; i = i + 1) {
			int r = random.nextInt(myString.length);
			mystrbuf.append(myString[r]);
		}
		return mystrbuf.toString();
	}

	/**
	 * getUUID ȡ��32λ�ַ�����UUID�ַ���
	 * 
	 * @return String ����������ɵ�UUID�ַ���
	 */
	@Override
	public String getUUID() {
		String uUIDString = UUID.randomUUID().toString();
		uUIDString = uUIDString.replaceAll("-", "");
		return uUIDString;
	}

	public static void main(String[] arg) throws ParseException {
		RandomModuleImpl random = new RandomModuleImpl();
		for (int i = 0; i < 100; ++i) {
			Date date = random.getRandomDate("2009-03-01", "2010-03-31",
					"yyyy-MM-dd");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd");
			logger.debug(dateFormat.format(date));
		}
	}

}
