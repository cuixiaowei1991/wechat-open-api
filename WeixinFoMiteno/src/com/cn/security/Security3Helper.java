package com.cn.security;

import java.util.Map;

import org.springframework.flex.security3.AuthenticationResultUtils;

public class Security3Helper {
	/**
	 * ΪFlex�ṩ��ȫ��֤
	 * @return
	 */
	public Map<String, Object> getAuthentication() {
		return AuthenticationResultUtils.getAuthenticationResult();
	}
}
