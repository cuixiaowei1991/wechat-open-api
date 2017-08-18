package com.cn.weixin.cache;

public class MerchantTokens {

	private String sid;
	private String appid;
	private String secret;
	private String access_token;
	private String jsapi_ticket;
	private String api_ticket;
	private long access_tokenExpires=0;
	private long jsapi_ticketExpires=0;
	private long api_ticketExpires=0;
	
	public MerchantTokens(String sid,String appid,String secret,String access_token,long expires_in) {
		setSid(sid);
		setAppid(appid);
		setSecret(secret);
		setAccess_token(access_token);
		setAccess_tokenExpires(expires_in);
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	/**
	 * 后台调用接口凭证
	 * @return
	 */
	public String getAccess_token() {
		return access_token;
	}
	/**
	 * 后台调用接口凭证
	 * @param access_token
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	/**
	 * 调用微信JS接口的临时票据 用于生成JS请求微信的签名
	 * @return
	 */
	public String getJsapi_ticket() {
		return jsapi_ticket;
	}
	/**
	 * 调用微信JS接口的临时票据 用于生成JS请求微信的签名
	 * @param jsapi_ticket
	 */
	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}
	/**
	 * 用于调用微信JS API 的临时票据
	 * @return
	 */
	public String getApi_ticket() {
		return api_ticket;
	}
	/**
	 * 用于调用微信JS API 的临时票据
	 * @param api_ticket
	 */
	public void setApi_ticket(String api_ticket) {
		this.api_ticket = api_ticket;
	}
	public long getAccess_tokenExpires() {
		return access_tokenExpires;
	}
	public void setAccess_tokenExpires(long expires_in) {
		this.access_tokenExpires = System.currentTimeMillis()/1000+expires_in-1800;
		
	}
	public long getJsapi_ticketExpires() {
		return jsapi_ticketExpires;
	}
	public void setJsapi_ticketExpires(long expires_in) {
		this.jsapi_ticketExpires = System.currentTimeMillis()/1000+expires_in-1800;
	}
	public long getApi_ticketExpires() {
		return api_ticketExpires;
	}
	public void setApi_ticketExpires(long expires_in) {
		this.api_ticketExpires = System.currentTimeMillis()/1000+expires_in-1800;
	}
	
	public boolean access_tokenExpires() {
		if(System.currentTimeMillis()/1000>access_tokenExpires){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean jsapi_ticketExpires() {
		if(System.currentTimeMillis()/1000>jsapi_ticketExpires){
			return true;
		}else{
			return false;
		}
	}
	public boolean api_ticketExpires() {
		if(System.currentTimeMillis()/1000>api_ticketExpires){
			return true;
		}else{
			return false;
		}
	}
}
