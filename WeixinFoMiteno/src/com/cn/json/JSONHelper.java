package com.cn.json;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * JSON����ģ��
 * 
 * @author admin
 *  
 */
public class JSONHelper {

	/**
	 * ��ȡJSON����
	 * @param jsonString
	 *            json��ʽ�ַ���
	 * @return JSONO����
	 * @throws JSONException
	 */
	public JSONObject getJSONObject(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		return jsonObject;
	}

	/**
	 * ��ȡJSON��
	 * @param jsonString
	 *            json��ʽ�ַ���
	 * @return JSONArray����
	 * @throws JSONException
	 */
	public JSONArray getJSONArray(String jsonString) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonString);
		return jsonArray;
	}

	/**
	 * ��ȡJSON�ַ���
	 * @param jsonObject json����
	 * @param key json�ؼ���
	 * @return
	 * @throws JSONException
	 */
	public String getJSONString(JSONObject jsonObject, String key) throws JSONException {
		String jsonString = jsonObject.getString(key);
		return jsonString;
	}

}
