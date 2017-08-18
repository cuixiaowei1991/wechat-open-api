package com.cn.json;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * JSON处理模块
 * 
 * @author admin
 *  
 */
public class JSONHelper {

	/**
	 * 获取JSON对象
	 * @param jsonString
	 *            json格式字符串
	 * @return JSONO对象
	 * @throws JSONException
	 */
	public JSONObject getJSONObject(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		return jsonObject;
	}

	/**
	 * 获取JSON组
	 * @param jsonString
	 *            json格式字符串
	 * @return JSONArray串组
	 * @throws JSONException
	 */
	public JSONArray getJSONArray(String jsonString) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonString);
		return jsonArray;
	}

	/**
	 * 获取JSON字符串
	 * @param jsonObject json对象
	 * @param key json关键字
	 * @return
	 * @throws JSONException
	 */
	public String getJSONString(JSONObject jsonObject, String key) throws JSONException {
		String jsonString = jsonObject.getString(key);
		return jsonString;
	}

}
