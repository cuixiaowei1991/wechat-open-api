package com.cn.security;

import java.util.Map;

import org.springframework.flex.security3.AuthenticationResultUtils;

public class Security3Helper {
	/**
	 * 为Flex提供安全认证
	 * @return
	 */
	public Map<String, Object> getAuthentication() {
		return AuthenticationResultUtils.getAuthenticationResult();
	}
}
