package com.cn.struts2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cn.common.DateModule;
import com.cn.common.RandomModule;
import com.cn.crypto.CryptoModule;
import com.cn.dao.util.HibernateDAO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ����Action����࣬��ɸ�����ϵͳģ���ע�� 2010-03-30�޸Ļ���û���¼Ȩ�޺���֤��Ϣ�ĺ���
 * 
 * @author Hartwell
 * 
 * @version 2010.03.30
 */
@SuppressWarnings("serial")
public abstract class BaseAction extends ActionSupport implements
		ApplicationContextAware {

	/**
	 * ��ȡSpring����
	 * 
	 */
	private ApplicationContext applicationContext;

	/**
	 * ��ȡStruts2����
	 */
	private ActionContext context;

	/**
	 * ��ȡhttp�����request
	 */
	private HttpServletRequest request;

	/**
	 * ��ȡhttp�����response
	 */
	private HttpServletResponse response;

	/**
	 * ����ע����ʱ�����ģ��ʵ��
	 */
	public DateModule dateModule;

	/**
	 * ����ע�������������ģ��ʵ��
	 */
	public RandomModule randomModule;

	/**
	 * ����ע���ü��ܽ���ģ��ʵ��
	 */
	public CryptoModule cryptoModule;

	/**
	 * ����ע����ʵ�������ģ��ʵ��
	 */
	public HibernateDAO hibernateDao;

	/**
	 * ���캯��
	 */
	public BaseAction() {
	}

	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * ��ȡSpring Security����֤��Ϣ
	 * 
	 * @return Ȩ�޶���
	 */
	public Authentication getAuthentication() {
		Authentication result = SecurityContextHolder.getContext()
				.getAuthentication();
		return result;
	}

	/**
	 * ��ȡ��ȡSpring_Security��Ȩ���ַ�
	 * 
	 * @return Ȩ���б�
	 */
	public List<String> getAuthoritiesString() {
		List<String> authorityList = new ArrayList<String>();
		Collection<?> grantedAuthorities = SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		for (Object authority : grantedAuthorities) {

			authorityList.add(((GrantedAuthority) authority).getAuthority());
		}
		return authorityList;
	}

	public ActionContext getContext() {
		context = ActionContext.getContext();
		return context;
	}

	public CryptoModule getCryptoModule() {
		return cryptoModule;
	}

	public DateModule getDateModule() {
		return dateModule;
	}

	public HibernateDAO getHibernateDao() {
		return hibernateDao;
	}

	public RandomModule getRandomModule() {
		return randomModule;
	}

	public HttpServletRequest getRequest() {
		request = (HttpServletRequest) getContext().get(
				StrutsStatics.HTTP_REQUEST);
		return request;
	}

	public HttpServletResponse getResponse() {
		response = (HttpServletResponse) getContext().get(
				StrutsStatics.HTTP_RESPONSE);
		return response;
	}

	/**
	 * @param applicationContext
	 *            the applicationContext to set
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setContext(ActionContext context) {
		this.context = context;
	}

	public void setCryptoModule(CryptoModule cryptoModule) {
		this.cryptoModule = cryptoModule;
	}

	public void setDateModule(DateModule dateModule) {
		this.dateModule = dateModule;
	}

	public void setHibernateDao(HibernateDAO hibernateDao) {
		this.hibernateDao = hibernateDao;
	}

	public void setRandomModule(RandomModule randomModule) {
		this.randomModule = randomModule;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
