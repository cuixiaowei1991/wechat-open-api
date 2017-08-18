package com.cn.security;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cn.struts2.BaseAction;
import com.opensymphony.xwork2.Action;

/**
 * 通过HTTP访问获取Spring-Security容器中的用户信息
 * 
 * @author LinPeng
 * @date 2014/11/11
 *
 */
public class SecurityAction extends BaseAction implements SessionAware {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -2393913908278318401L;

	private Logger logger = Logger.getLogger(this.getClass());
	public Authentication authentication;
	public AuthenticationManager authenticationManager;
	public String username;
	public String password;

	public Authentication getAuthentication() {
		return authentication;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	/**
	 * 获取用户认证信息
	 * 
	 * @return
	 */
	public String getAuthInfo() {
		SecurityContext securityContext = (SecurityContext) super.getRequest()
				.getSession(true).getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication authentication = null;
		if (securityContext == null) {
			authentication = null;
		} else {
			authentication = securityContext.getAuthentication();
		}
		setAuthentication(authentication);
		logger.debug(authentication);
		return Action.SUCCESS;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 登录函数
	 * 
	 * @return
	 */
	public String login() {
		try {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					username, password);
			Authentication authentication = authenticationManager
					.authenticate(usernamePasswordAuthenticationToken);
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);
			setAuthentication(authentication);
			logger.debug(authentication);
			super.getRequest()
					.getSession(true)
					.setAttribute("SPRING_SECURITY_CONTEXT",
							SecurityContextHolder.getContext());
		} catch (AuthenticationException e) {
			logger.debug(e.getMessage());
			setAuthentication(null);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return Action.SUCCESS;
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public String logout() {
		this.authentication = null;
		SecurityContextHolder.getContext().setAuthentication(null);
		super.getRequest().getSession(true)
				.setAttribute("SPRING_SECURITY_CONTEXT", null);
		return Action.SUCCESS;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

	public void setUsername(String username) {
		this.username = username;
	}
}
