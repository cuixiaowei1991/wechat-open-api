package com.cn.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface FileUploadModule {

	public void uploadFile(HttpServletRequest request) throws IOException;

}