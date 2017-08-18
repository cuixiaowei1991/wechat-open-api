package com.cn.weixin.protocol;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cuixiaowei on 2016/4/22.
 */
public class httpsPostMethod {
    private static Logger logger = Logger
            .getLogger(httpsPostMethod.class);
    /**
     * httpPost请求
     *
     * @param url
     * @param param
     */
    public static String sendHttpsPost(String url, String param,
                                       String explanatory) {

        String str_return = null;
        HttpsURLConnection conn = null;
        logger.debug(explanatory + "  请求微信https开始:");
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
            URL console = new URL(url);
            conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            if("advert_1473755156372".equals(explanatory))
            {
                conn.setRequestProperty("distributionid","1473755156372");
            }

            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            if (param != null)
                out.write(param.getBytes("UTF-8"));

            out.flush();
            out.close();

            //
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            int code = conn.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == code) {
                String temp = in.readLine();

                while (temp != null) {
                    if (str_return != null)
                        str_return += temp;
                    else
                        str_return = temp;
                    temp = in.readLine();
                }
            } else {
                logger.debug(explanatory + "  出现异常,HTTP错误代码:" + code);
            }
            conn.disconnect();

        } catch (NoSuchAlgorithmException e) {
            str_return = "error";
            logger.debug(explanatory + "  出现异常");
            e.printStackTrace();
        } catch (KeyManagementException e) {
            str_return = "error";
            logger.debug(explanatory + "  出现异常");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            str_return = "error";
            logger.debug(explanatory + "  出现异常");
            e.printStackTrace();
        } catch (IOException e) {
            str_return = "error";
            logger.debug(explanatory + "  出现异常");
            e.printStackTrace();
        }
        logger.debug(explanatory + "  的返回为:" + str_return + "@@");
        return str_return;
    }

    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 设置响应头header
     * @param url
     * @param json
     * @param erp
     * @return
     */
    public static String doPostJson(String url, String json,String erp) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            if("advert_1473755156372".equals(erp))
            {
                httpPost.setHeader("distributionid","1473755156372");
            }
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
    /**
     *  httpPost访问请求
     *
     * @param marked
     * @param jsonStr
     * @return
     */
    public static String postHttp(String marked, String jsonStr, String url) {
        String responseMsg = "";
        // 1.构造HttpClient的实例
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("utf-8");
        // 接口地址
//        String urlServer = "http://192.168.15.138:8080/changeFunction/interfaceForApay?";
        String urlServer = url;
        // 2.构造PostMethod的实例

        PostMethod postMethod = new PostMethod(urlServer);
        // 3.把参数值放入到PostMethod对象中
        // 方式1：
		/*
		 * NameValuePair[] data = { new NameValuePair("param1", param1), new
		 * NameValuePair("param2", param2) }; postMethod.setRequestBody(data);
		 */
        // 方式2：
        /*if("balance_weichat_callback".equals(marked))
        {
            postMethod.addParameter("code", "10002");
            postMethod.addParameter("version", "1.0");
        }*/
        postMethod.addParameter("marked", marked);
        postMethod.addParameter("jsonStr", jsonStr);
        try {
            // 4.执行postMethod,调用http接口
            httpClient.executeMethod(postMethod);// 200
            // 5.读取内容
            responseMsg = postMethod.getResponseBodyAsString().trim();
            // responseMsg = new
            // String(responseMsg.getBytes("iso-8859-1"),"UTF-8");
            // 6.处理返回的内容
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 7.释放连接
            postMethod.releaseConnection();
        }
        return responseMsg;
    }

    public static String doGet(String url) {
        logger.debug("z转码之后@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+url);
        return doGet(url, null);
    }
    /**
     * 联机交易系统访问方法
     */
    public static String sendHttpPostA(String url, List<NameValuePair> parameters,
                                String explanatory) {
        logger.debug("进入ForApay.inApayCoupon()-------------");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000).setConnectTimeout(5000).build();// 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = null;
        String result = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));

            httpResponse = httpClient.execute(httpPost);

            int sc = httpResponse.getStatusLine().getStatusCode();
            switch (sc) {
                case 200:
                    // 第三步，使用getEntity方法活得返回结果
                    result = EntityUtils.toString(httpResponse.getEntity());

                    break;

                default:
                    // log打印真正的原因是后台返给你的错误代码
                    logger.debug(explanatory + "-返回错误StatusCode:" + sc);
                    logger.debug(EntityUtils.toString(httpResponse.getEntity()));
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug(explanatory + "返回:" + result);
        return result;
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

}
