package com.cn.weixin;

import com.alibaba.fastjson.JSONArray;
import com.cn.dao.daoimpl.*;
import com.cn.entity.*;
import com.cn.struts2.BaseAction;
import com.cn.weixin.cache.*;
import com.cn.weixin.common.AesException;
import com.cn.weixin.common.WXBizMsgCrypt;
import com.cn.weixin.common.WeiXinDecode;
import com.cn.weixin.protocol.commenUtil;
import com.cn.weixin.protocol.httpsPostMethod;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import net.sf.json.xml.XMLSerializer;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Element;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cuixiaowei on 2016/4/22.
 */
public class thirdPartyPlatformFunction extends BaseAction {

    private String app_id;
    private String app_secret;
    private String token;//公众号消息校验Token
    private String key;
    private String marked;//定义接口访问标记
    private String jsonStr;//请求json串
    private static Logger logger = Logger
            .getLogger(thirdPartyPlatformFunction.class);
    private getAuthorizeByAuthappidDaoimpl gabadaoimpl;
    private orderInfoDaoimpl infoDaoimpl;
    private String pay_notify_url;
    private getWeiXinTuiguangByWXTDDaoimpl getwxtgimpl;
    private static String bb = null;
    private String cardCherkReturn_url;
    private String sotreCheckReturn_url;
    private String apayUrl;//微信用户领券地址
    private String asyncPayResult_url;//微信支付回调支付结果
    private AutoMessageDaoImpl autoMessageDaoimpl;
    private AdvertOrderDaoImpl advertOrderDaoimpl;
    private String transitUrl;//威富通广告过度地址
    /**
     * 每隔十分钟获取每10分钟推送一次的安全ticket（用于获取预授权码）
     *
     * @return
     */
    public void getComponentVerifyTicket()
    {

        logger.debug("接受的微信返回的路径地址---------------》" + getRequest().getRequestURL());


        Map<String, String[]> requestMap = getRequest().getParameterMap();
        JSONObject requestJSON = new JSONObject();
        for (Object key : requestMap.keySet()) {
            for (int i = 0; i < requestMap.get(key).length; i++) {
                logger.debug("微信第三方平台授权或者获取每十分钟推送验证ticket结果_GET的键= " + key + "; 值" + i
                        + "= " + requestMap.get(key)[i]);
                // parameters.add(new BasicNameValuePair(key.toString(),
                // requestMap.get(key)[i]));
                try {
                    if("audit_desc".equals(key.toString())){
                        String audit_desc=requestMap.get(key)[i];
                        String[] encodeArr = new String[] { "UTF-8", "iso-8859-1", "gbk",
                                "gb2312" };

                        for (int q = 0; q < encodeArr.length; q++) {
                            for (int w = 0; w < encodeArr.length; w++) {
                                String stg;
                                try {
                                    stg = new String(audit_desc.getBytes(encodeArr[w]),
                                            encodeArr[q]);
                                    System.out.println(encodeArr[w] + " --" + encodeArr[q]
                                            + "      msg:" + stg);
                                } catch (UnsupportedEncodingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        requestJSON.put(key.toString(), audit_desc);
                    }else{
                        requestJSON.put(key.toString(), requestMap.get(key)[i]);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        BufferedReader br;
        String postStr = null;
        try {
            br = getRequest().getReader();
            String buffer = null;
            StringBuffer buff = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                buff.append(buffer + "\n");
            }
            br.close();
            postStr = buff.toString();
            logger.debug("接收post发送数据:\n" + postStr);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();

        }




        logger.debug("授权事件接收URL返回的xml(密文):" + postStr);
        logger.debug("签名串------------:" + getRequest().getParameter("msg_signature"));
        logger.debug("时间戳------------:" + getRequest().getParameter("timestamp"));
        logger.debug("随机数------------:" + getRequest().getParameter("nonce"));
        String result_json= WeiXinDecode.decode(token, key, app_id, getRequest().getParameter("msg_signature"), getRequest().getParameter("timestamp"), getRequest().getParameter("nonce")
            ,postStr);
        try
        {
            JSONObject json=new JSONObject(result_json);
            String appid=json.getString("AppId");//第三方平台appid
            String createTime=json.getString("CreateTime");//时间戳
            String infoType=json.getString("InfoType");//component_verify_ticket
            if(infoType!=null && "component_verify_ticket".equalsIgnoreCase(infoType))
            {//每十分钟推一次验证ticket
                String componentVerifyTicket=json.getString("ComponentVerifyTicket");//Ticket内容
                ComponentVerifyTicket componentVerifyTicket_l=new ComponentVerifyTicket(appid,createTime,infoType,componentVerifyTicket);
                ComponetVTicketMap.put(appid,componentVerifyTicket_l);
                System.out.println("到这了==================================");
                //output(this.getResponse(), "success");
                getResponse().getWriter().write("success");
            }
            if(infoType!=null && "authorized".equalsIgnoreCase(infoType))
            {//授权成功回调方法
                String authorizerAppid=json.getString("AuthorizerAppid");//需授权的公众号appid
                String authorizationCode=json.getString("AuthorizationCode");//需授权公众号授权码
                String CreateTime=json.getString("CreateTime");//微信服务端授权码创建日期（毫秒）存入数据库后转为正常日期
                String authorizationCodeExpiredTime=json.getString("AuthorizationCodeExpiredTime");//过期时间
                List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, authorizerAppid, true);
                //获取商户公众号信息
                String shopnumInfo= getShopPublicNumberInfo(authorizerAppid);
                JSONObject body_js = new JSONObject(shopnumInfo);
                String authorizer_info=body_js.get("authorizer_info")+"";
                JSONObject bb = new JSONObject(authorizer_info);
                String nick_name= bb.getString("nick_name");
                String head_img=bb.getString("head_img");
                //String qrcode_url=bb.getString("qrcode_url");//二维码地址
                WeiXinAuthorization wan;
                if(weiXinAuthorizationList==null || weiXinAuthorizationList.size()<=0)
                {
                    logger.debug("new新对象----------------authorizerAppid="+authorizerAppid);
                     wan=new WeiXinAuthorization();

                }
                else
                {
                    logger.debug("查询的存在对象----------------authorizerAppid="+weiXinAuthorizationList.get(0).getWeixin_Authorizer_Appid());
                     wan= weiXinAuthorizationList.get(0);
                     wan.setWeixin_auth_id(wan.getWeixin_auth_id());

                }
                wan.setAuthorizationCodeExpiredTime(authorizationCodeExpiredTime);
                wan.setCreateTime(CreateTime);
                wan.setWeixin_Authorizer_Appid(authorizerAppid);
                wan.setWeixin_Authorizer_code(authorizationCode);
                wan.setAuthorization_zhuangtai("1");
                wan.setNickname(nick_name);
                wan.setImage_url(head_img);
                //wan.setCode_url(qrcode_url);
                gabadaoimpl.insert(wan);
                getAuthAccesstoken(authorizerAppid);

               }
            if(infoType!=null && "unauthorized".equalsIgnoreCase(infoType))
            {   //取消授权接入
                String authorizerAppid=json.getString("AuthorizerAppid");//取消授权的公众号appid
                String CreateTime=json.getString("CreateTime");//微信服务端授权码创建日期（毫秒）存入数据库后转为正常日期
                List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, authorizerAppid, true);

                WeiXinAuthorization wan=weiXinAuthorizationList.get(0);
                wan.setAuthorization_zhuangtai("2");//取消授权
                wan.setWeixin_Authorizer_code("");
                wan.setAuthorizationCodeExpiredTime("");
                wan.setCreateTime(CreateTime);
                wan.setAuthorizer_refresh_token("");
                gabadaoimpl.insert(wan);
            }
            if(infoType!=null && "updateauthorized".equalsIgnoreCase(infoType))
            {//更新授权接入
                String authorizerAppid=json.getString("AuthorizerAppid");//需授权的公众号appid
                String authorizationCode=json.getString("AuthorizationCode");//需授权公众号授权码
                String CreateTime=json.getString("CreateTime");//微信服务端授权码创建日期（毫秒）存入数据库后转为正常日期
                String authorizationCodeExpiredTime=json.getString("AuthorizationCodeExpiredTime");//过期时间
                List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, authorizerAppid, true);

                WeiXinAuthorization wan=weiXinAuthorizationList.get(0);
                wan.setAuthorization_zhuangtai("3");//取消授权
                wan.setWeixin_Authorizer_code(authorizationCode);
                wan.setAuthorizationCodeExpiredTime(authorizationCodeExpiredTime);
                wan.setCreateTime(CreateTime);
                gabadaoimpl.insert(wan);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
     }


    /**
     * 获取第三方平台component_access_token
     * @return
     */
    public String getComponentAccessToken()
    {
        try {
            ComponentVerifyTicket cvt = ComponentAccessTokenMap.get(app_id);
            if(null==cvt)
            {
                logger.debug("第三方平台token（1）---------------》:"+ComponetVTicketMap.get(app_id).getComponentVerifyTicket());
                String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_component_token",
                        "{\"component_appid\":\""+app_id+"\",\"component_appsecret\":\""+app_secret+"\",\"component_verify_ticket\":\""+ComponetVTicketMap.get(app_id).getComponentVerifyTicket()+"\"}","获取第三方平台component_access_token");
                logger.debug("获取第三方平台token微信返回-----------》"+responseStr);

                JSONObject rjson = new JSONObject(responseStr);
                if(rjson.isNull("component_access_token"))
                {
                    return null;
                }
                String component_access_token=rjson.getString("component_access_token");
                long expires_in=rjson.getLong("expires_in");
                ComponentVerifyTicket componentVerifyTicket=new ComponentVerifyTicket(app_id,component_access_token,expires_in);
                ComponentAccessTokenMap.put(app_id,componentVerifyTicket);
                logger.debug("验证第三方平台component_access_token是否存入(1)-----------》"+ComponentAccessTokenMap.get(app_id).getComponent_access_token());

            }
            else
            {
                if(cvt.component_access_tokenExprise())
                {
                    String component_verify_ticket=ComponentAccessTokenMap.get(app_id).getComponentVerifyTicket();
                    logger.debug("第三方平台token（2）---------------》:"+component_verify_ticket);
                    String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_component_token",
                            "{\"component_appid\":\""+app_id+"\",\"component_appsecret\":\""+app_secret+"\",\"component_verify_ticket\":\""+ComponetVTicketMap.get(app_id).getComponentVerifyTicket()+"\"}","获取第三方平台component_access_token");
                    logger.debug("获取第三方平台token微信返回（本地过期）-----------》"+responseStr);
                    JSONObject rjson = new JSONObject(responseStr);
                    String component_access_token=rjson.getString("component_access_token");
                    long expires_in=rjson.getLong("expires_in");
                    ComponentVerifyTicket componentVerifyTicket=new ComponentVerifyTicket(app_id,component_access_token,expires_in);
                    ComponentAccessTokenMap.put(app_id,componentVerifyTicket);
                    logger.debug("验证第三方平台component_access_token是否存入(2)-----------》" + ComponentAccessTokenMap.get(app_id).getComponent_access_token());


                }
            }
            return ComponentAccessTokenMap.get(app_id).getComponent_access_token();


        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取预授权码pre_auth_code 预授权码用于公众号授权时的第三方平台方安全验证
     * @return
     */
    public String getPreAuthCode()
    {
        ComponentVerifyTicket cvt = PreAuthCodeMap.get(app_id);
    try {
        if(null==cvt)
        {
            logger.debug("获取到的第三方平台-------------》token（1）"+getComponentAccessToken());
            String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+getComponentAccessToken(),
                    "{\"component_appid\":\""+app_id+"\"}","获取预授权码pre_auth_code");
            logger.debug("获取预授权码pre_auth_code微信返回----------------》"+responseStr);
            JSONObject rjson = new JSONObject(responseStr);
            if(rjson.isNull("pre_auth_code"))
            {
                return null;
            }
            else
            {
                String pre_auth_code=rjson.getString("pre_auth_code");//预授权码
                long expires_in=rjson.getLong("expires_in");//有效期20分钟
                ComponentVerifyTicket componentVerifyTicket=new ComponentVerifyTicket(app_id,expires_in,pre_auth_code);
                PreAuthCodeMap.put(app_id, componentVerifyTicket);
                logger.debug("验证预授权码pre_auth_code（1）是否存入-----------》" + PreAuthCodeMap.get(app_id).getPre_auth_code());

            }
        }
        else {
            if (cvt.preauthcode_Exprise()) {
                String token=getComponentAccessToken();
                logger.debug("获取到的第三方平台-------------》token（2）"+token);
                String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+getComponentAccessToken(),
                        "{\"component_appid\":\""+app_id+"\"}","获取预授权码pre_auth_code");
                logger.debug("获取预授权码pre_auth_code微信返回（本地过期）----------------》"+responseStr);
                JSONObject rjson = new JSONObject(responseStr);
                String pre_auth_code=rjson.getString("pre_auth_code");//预授权码
                long expires_in=rjson.getLong("expires_in");//有效期20分钟
                ComponentVerifyTicket componentVerifyTicket=new ComponentVerifyTicket(app_id,expires_in,pre_auth_code);
                PreAuthCodeMap.put(app_id,componentVerifyTicket);
                logger.debug("验证预授权码pre_auth_code（2）是否存入-----------》" + PreAuthCodeMap.get(app_id).getPre_auth_code());

            }

        }
        return PreAuthCodeMap.get(app_id).getPre_auth_code();
    }
    catch (Exception e)
    {
        e.printStackTrace();;
        return null;
    }
    }

    /**
     * 微信第三方平台授权的入口
     * @return
     */
    public void entranceWinXin()
    {
        String callback = this.getRequest().getParameter("callbackparam");
        String url="https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+app_id+"&pre_auth_code="
                +getPreAuthCode()+"&redirect_uri=http://www.all-pay.cn/";
        logger.debug("返回到前台的地址为：---------------》"+url);
        String result="{\"url\":\""+url+"\"}";

        try {
            //getResponse().sendRedirect(url);
            //getResponse().getWriter().append(  result );
            getResponse().setCharacterEncoding("UTF-8");
            this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
            this.getResponse().setContentType("text/json;charset=UTF-8");
            getResponse().getWriter().append(callback + "(" + result + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信回调接口
     *
     * @return
     */
    public String returnForWeiXin()
    {
        logger.debug("——————————————————————微信回调返回的授权信息---------------");

        String auth_code=getRequest().getParameter("auth_code");//授权码
        String expires_in=getRequest().getParameter("expires_in");//过期时间
        logger.debug("微信回调返回的授权码auth_code------------》"+auth_code);
        logger.debug("微信回调返回的过期时间expires_in------------》"+expires_in);
        return null;
    }

    /**
     * 根据授权码和第三方平台appid换取公众号的接口调用凭据和授权信息
     * @return
     */
    public String getAuthAccesstoken(String authaccess_appid)
    {

        //String authaccess_appid=getRequest().getParameter("appid");//授权方的appid
        logger.debug("授权方的appid--------------》"+authaccess_appid);
        AuthorizerInfo authorizerInfo=AuthorizerInfoMap.get(authaccess_appid);
        List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, authaccess_appid, true);
        WeiXinAuthorization war=weiXinAuthorizationList.get(0);
        if(null==authorizerInfo)
        {
            authorizerInfo=new AuthorizerInfo();
        }
        if( (null==war.getAuthorizer_refresh_token()|| "".equals(war.getAuthorizer_refresh_token())))
        {
        List<WeiXinAuthorization> walist= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, authaccess_appid, true);
        String auth_code="";//授权码
        if(walist==null || walist.size()<=0)
        {//数据库中没有该商户的appid 直接跳到授权

            entranceWinXin();
            return "数据库中没有该商户的appid 直接跳到授权,授权失败！";
        }
        else
        {
            try
            {

                if(walist.get(0).getAuthorization_zhuangtai()==null || (walist.get(0).getAuthorization_zhuangtai().equals("2")))
                {//已取消授权，重新获取授权码
                    entranceWinXin();
                    return "已取消授权，重新获取授权码！";
                }
                auth_code=walist.get(0).getWeixin_Authorizer_code();
                String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+getComponentAccessToken(),
                        "{\"component_appid\":\""+app_id+"\",\"authorization_code\":\""+auth_code+"\"}","根据授权码和第三方平台appid换取公众号的接口调用凭据和授权信息");
                logger.debug("根据授权码和第三方平台appid换取公众号的接口调用凭据和授权信息-----------》"+responseStr);

                JSONObject rjson = new JSONObject(responseStr);
                String authorization_info=rjson.get("authorization_info")+"";
                JSONObject js_authorization = new JSONObject(authorization_info);
                String authorizer_appid=js_authorization.getString("authorizer_appid");//授权方appid
                String authorizer_access_token=js_authorization.getString("authorizer_access_token");//授权方token
                long expires_in=js_authorization.getLong("expires_in");
                String authorizer_refresh_token=js_authorization.getString("authorizer_refresh_token");
                org.json.JSONArray func_info=js_authorization.getJSONArray("func_info");
                /*AuthorizerInfo ai=new AuthorizerInfo(app_id,authorizer_appid,authorizer_access_token,
                        expires_in,authorizer_refresh_token,func_info.toString());*/
                AuthorizerInfo ai1=new AuthorizerInfo();
                ai1.setFunc_info(func_info.toString());
                ai1.setAuthorizer_refresh_token(authorizer_refresh_token);
                ai1.setAuthorizer_access_token(authorizer_access_token);
                ai1.setAuthorizer_appid(authorizer_appid);
                ai1.setAuthorizer_access_tokenExprise(expires_in);
                ai1.setAppid(app_id);
                AuthorizerInfoMap.put(authorizer_appid, ai1);
                //将Authorizer_refresh_token存入数据库保存
                //List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, authorizer_appid, true);
                if(weiXinAuthorizationList!=null && weiXinAuthorizationList.size()>0)
                {
                    //WeiXinAuthorization war=weiXinAuthorizationList.get(0);
                    war.setAuthorizer_refresh_token(authorizer_refresh_token);
                    gabadaoimpl.insert(war);
                }


                logger.debug("存入获取的授权方相关信息（相关token和过期时间）------------>第一次进入" + AuthorizerInfoMap.get(authorizer_appid).getAuthorizer_access_token());
                logger.debug("存入获取的授权方相关信息（相关refreshtoken和过期时间）------------>第一次进入" + AuthorizerInfoMap.get(authorizer_appid).getAuthorizer_refresh_token());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
        else
        {
            if( authorizerInfo.authorizer_access_tokenExprise())
            {
                  try
                    {
                        //从数据库中查询出刷新令牌用的refreshtoken
                        //logger.debug("刷新令牌用的refreshtoken------------------>"+AuthorizerInfoMap.get(authaccess_appid).getAuthorizer_refresh_token());
                        logger.debug("刷新令牌用的refreshtoken------------------>"+war.getAuthorizer_refresh_token());
                       String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=" + getComponentAccessToken(),
                               "{\"component_appid\":\"" + app_id + "\",\"authorizer_appid\":\"" + authaccess_appid + "\",\"authorizer_refresh_token\":\"" + war.getAuthorizer_refresh_token() + "\"}", "根据授权码和第三方平台appid换取公众号的接口调用凭据和授权信息");
                        logger.debug("根据授权码和第三方平台appid换取公众号的接口调用凭据和授权信息(本地过期根据authorizer_refresh_token重新获取token)-----------》"+responseStr);

                        JSONObject rjson = new JSONObject(responseStr);

                        String authorizer_access_token=rjson.getString("authorizer_access_token");//授权方token
                        long expires_in=rjson.getLong("expires_in");
                        String authorizer_refresh_token=rjson.getString("authorizer_refresh_token");
                        AuthorizerInfo ai=new AuthorizerInfo(app_id,authaccess_appid,authorizer_access_token,
                                expires_in,authorizer_refresh_token);
                        AuthorizerInfoMap.put(authaccess_appid,ai);
                        war.setAuthorizer_refresh_token(authorizer_refresh_token);
                        gabadaoimpl.insert(war);
                        logger.debug("存入获取的授权方相关信息（相关token和过期时间）------------>本地过期"+AuthorizerInfoMap.get(authaccess_appid).getAuthorizer_access_token());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }
       return AuthorizerInfoMap.get(authaccess_appid).getAuthorizer_access_token();
    }

    /**
     * 调用微信JS接口的临时票据 用于生成JS请求微信的签名
     *
     * @return
     */
    public String getJsapi_ticket(String authaccess_appid) {
        //String authaccess_appid = getRequest().getParameter("appid");//授权方的appid
        JsapiticketInfo js = JsapiticketInfoMap.get(authaccess_appid);

        logger.debug("获取的授权token----------------》"+getAuthAccesstoken(authaccess_appid));
        logger.debug("AuthorizerInfo----------------》"+js);
        if (js==null || js.jsapi_ticketExpires()) {
            String rJsapi = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                            + getAuthAccesstoken(authaccess_appid), "&type=jsapi",
                    "获取jsapi_ticket");
            if (!"error".equals(rJsapi)) {
                try {
                    JSONObject jsapiJSON = new JSONObject(rJsapi);
                    int errcode = -1;
                    errcode = jsapiJSON.getInt("errcode");
                    if (errcode == 0) {
                        String jsapi_ticket = jsapiJSON.getString("ticket");
                        long expires_in = jsapiJSON.getInt("expires_in");
                        JsapiticketInfo af1=new JsapiticketInfo();
                        af1.setJsapi_ticket(jsapi_ticket);
                        af1.setJsapi_ticketExpires(expires_in);
                        JsapiticketInfoMap.put(authaccess_appid, af1);
                       /* af.setJsapi_ticket(jsapi_ticket);
                        af.setJsapi_ticketExpires(expires_in);*/
                    } else {
                        return null;
                    }
                } catch (JSONException e) {
                    logger.debug("获取jsapi_ticket,authaccess_appid=" + authaccess_appid + "抛异常:"
                            + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
            else
            {
                return null;
            }
         }
        return JsapiticketInfoMap.get(authaccess_appid).getJsapi_ticket();
    }

    /**
     * h5签名注册
     * @throws IOException
     */
    public void getSignPackage() throws IOException {

        logger.debug("vvvvvvvvvvvvvvvvvvvvvvvvvvH5获取JS访问条件接口vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        logger.debug("----------------------------------隐藏打印---------------------------------------");
        String url = getRequest().getHeader("referer");
        logger.debug("----------------ULR:"+url+"-----------------------");
        String authaccess_appid = getRequest().getParameter("appid");//授权方的appid
        this.getResponse().setCharacterEncoding("UTF-8");
        if (null == authaccess_appid) {
            this.getResponse().getWriter().append("error");
            return;
        }

        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = commenUtil.createSuiJi();

        String rawstring = "jsapi_ticket=" + getJsapi_ticket(authaccess_appid)
                + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url="
                + url + "";
        logger.debug("H5获取JS访问条件接口--生成的rawstring结果为：---" + rawstring);
        String signature = commenUtil.getSHA1(rawstring);

        JSONObject jj2 = new JSONObject();
        try {
            jj2.put("appId", authaccess_appid);

            jj2.put("timestamp", timestamp);
            jj2.put("nonceStr", nonceStr);
            jj2.put("signature", signature);
        } catch (JSONException e) {
            logger.debug("H5获取JS访问条件接口" + e.getMessage());
        }
        String result = jj2.toString();

        String callback = this.getRequest().getParameter("callbackparam");
        result = callback + "(" + jj2.toString() + ")";
        this.getResponse().setCharacterEncoding("UTF-8");
        this.getResponse().addHeader("Access-Control-Allow-Origin", "*");

        logger.debug("H5获取JS访问条件接口--response数据为：---" + result);
        this.getResponse().getWriter().append(result);
        logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^H5获取JS访问条件接口结束^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    /**
     * 微信支付入口
     * @return
     */
    public void weixinForPay()
    {
        logger.debug("--------------------------进入微信支付接口-------------------------");

        String callback = this.getRequest().getParameter("callbackparam");
        String jsonStr1= this.getRequest().getParameter("jsonStr");
        try {
        if(jsonStr1.equals( new String(jsonStr1.getBytes("iso-8859-1"), "iso-8859-1")))
        {
            jsonStr1 = new String(jsonStr1.getBytes("iso-8859-1"), "UTF-8");
            logger.debug("微信支付前台传过来的参数------转码之后(iso-8859-1--->utf-8)——————————jsonStr:" + jsonStr1);
        }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //String openid=getOpenId(jsonStr1);//获取商户openid

        logger.debug("进入微信支付接口前台给传的jsonStr-------------》"+jsonStr1);
        try {
            JSONObject json=new JSONObject(jsonStr1);
            JSONObject return_json=new JSONObject();
            String openid="";
            try
            {
                openid=json.getString("openid");
            }catch (Exception e )
            {

            }
            //String ticketid=json.getString("ticketid");
            String out_trade_no=json.getString("out_trade_no");//订单号
            String body=json.getString("body");//商品内容
            String total_fee=json.getString("total_fee");//总金额
            String allpay_trade_type="";
            try {
                allpay_trade_type = json.getString("allpay_trade_type");//用来区分H5支付（MWEB）还是公众号支付 （JSAPI）
            }catch (Exception e)
            {
                allpay_trade_type="";
            }
            List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class,json.getString("app_id"),true);

            /*String shop_appsecret=json.getString("appsecret");*/
            //String shop_appsecret=weiXinAuthorizationList.get(0).getNum_secret();//商户appsecret
            String shop_appsecret=weiXinAuthorizationList.get(0).getAlias();//商户api秘钥
            String shop_mch_id=weiXinAuthorizationList.get(0).getMuch_id();
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("appid",json.getString("app_id"));//商户id
            hashMap.put("mch_id",shop_mch_id);//商户号
            hashMap.put("device_info","WEB");
            //hashMap.put("nonce_str","1add1a30ac87aa2db72f57a2375d8fec");
            hashMap.put("nonce_str",commenUtil.createSuiJi());
            hashMap.put("body",body);
            //hashMap.put("detail","");
            hashMap.put("attach",weiXinAuthorizationList.get(0).getNickname()==null?"":weiXinAuthorizationList.get(0).getNickname());
            hashMap.put("out_trade_no",out_trade_no);//测试数据
            hashMap.put("fee_type","CNY");
            hashMap.put("total_fee", total_fee);//测试数据
            if (getRequest().getHeader("x-forwarded-for") == null)
            {
                hashMap.put("spbill_create_ip", getRequest().getRemoteAddr());
                logger.debug("ip--------------------->" + getRequest().getRemoteAddr());
            }
            else
            {
                hashMap.put("spbill_create_ip", getRequest().getHeader("x-forwarded-for"));
                logger.debug("ip--------------------->"+getRequest().getHeader("x-forwarded-for"));
            }

            //hashMap.put("spbill_create_ip","121.69.5.6");
            /*hashMap.put("time_start","");
            hashMap.put("time_expire","");*/
            //hashMap.put("goods_tag","");
            hashMap.put("notify_url",pay_notify_url);
            if(allpay_trade_type.equals("MWEB"))
            {
                hashMap.put("trade_type","MWEB");
                JSONObject scene_info=new JSONObject();
                JSONObject h5_info=new JSONObject();
                h5_info.put("type","Wap");
                h5_info.put("wap_url",json.getString("wap_url"));
                h5_info.put("wap_name",json.getString("wap_name"));
                scene_info.put("h5_info",h5_info);
                hashMap.put("scene_info",scene_info.toString());
            }else
            {
                hashMap.put("trade_type","JSAPI");
                hashMap.put("openid",openid);
            }

            //hashMap.put("product_id","");
            //hashMap.put("limit_pay","");


            //String aa=commenUtil.getSign("fe528a709478634bb317652581143afd", hashMap, "utf-8");
            String aa=commenUtil.getSign(shop_appsecret, hashMap, "utf-8");
            hashMap.put("sign", aa);
            String xml=commenUtil.map2xml(hashMap);
            logger.debug("统一下单转换成的map转换成的xml为----------------------》"+xml);
            String responseStr= httpsPostMethod.sendHttpsPost("https://api.mch.weixin.qq.com/pay/unifiedorder",
                    xml,"微信支付统一下单");

            logger.debug("提交到微信支付统一下单，微信返回的数据为----------------------》" + responseStr);
            JSONObject rjson = new JSONObject(commenUtil.xml2JSON(responseStr));
            String return_code=rjson.getString("return_code");
            String return_msg=rjson.getString("return_msg");

            if("SUCCESS".equals(return_code))
            {
                String result_code=rjson.getString("result_code");
                if("SUCCESS".equalsIgnoreCase(result_code))
                {
                    Map<String, String> return_map = new HashMap<>();
                    String prepay_id=rjson.getString("prepay_id");
                    String appid=rjson.getString("appid");

                    String timeStamp=String.valueOf(System.currentTimeMillis() / 1000);
                    String nonceStr=commenUtil.createSuiJi();
                    return_map.put("appId",appid);
                    return_map.put("timeStamp",timeStamp);
                    return_map.put("signType","MD5");
                    return_map.put("nonceStr",nonceStr);
                    return_map.put("package","prepay_id="+prepay_id);
                    //String sign=commenUtil.getSign("fe528a709478634bb317652581143afd", return_map, "utf-8");
                    String sign="";
                    if(!allpay_trade_type.equals("MWEB"))
                    {
                        sign=commenUtil.getSign(shop_appsecret, return_map, "utf-8");
                    }


                    if(allpay_trade_type.equals("MWEB"))
                    {
                        return_json.put("mweb_url",rjson.getString("mweb_url"));
                    }
                    return_json.put("appId",appid);
                    return_json.put("nonceStr",nonceStr);
                    return_json.put("signType","MD5");
                    return_json.put("timeStamp",timeStamp);
                    return_json.put("package","prepay_id="+prepay_id);
                    return_json.put("paySign",sign);
                    return_json.put("rspCode","000");
                    return_json.put("rspDesc","下单成功");
                    //return return_json.toString();

                }
                else
                {
                    String err_code=rjson.getString("err_code");
                    String err_code_des=rjson.getString("err_code_des");
                    return_json.put("rspCode",err_code);
                    return_json.put("rspDesc",err_code_des);

                }
            }
            else
            {
                return_json.put("rspCode", "321009");
                return_json.put("rspDesc", return_msg);


            }
            logger.debug("统一下单返回数据------------》》》》"+return_json);
            String result = callback + "(" + return_json.toString() + ")";
            this.getResponse().setCharacterEncoding("UTF-8");
            this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
            logger.debug("统一下单完成，通过jssdk呼出微信支付页面，所传数据：--------》" + result);
            this.getResponse().getWriter().append(result);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //return "";
    }


    /**
     *微信支付异步通知回调地址
     * @return
     */
    public void payReturn_url()
    {
        logger.debug("接受的微信支付结果返回的路径地址---------------》" + getRequest().getRequestURL());
//        String aa= getRequest().getParameter("appid");
        Map<String, String[]> requestMap = getRequest().getParameterMap();
        JSONObject requestJSON = new JSONObject();
        for (Object key : requestMap.keySet()) {
            for (int i = 0; i < requestMap.get(key).length; i++) {
                logger.debug("微信支付回调结果_GET的键= " + key + "; 值" + i
                        + "= " + requestMap.get(key)[i]);
                try {
                    if("audit_desc".equals(key.toString())){
                        String audit_desc=requestMap.get(key)[i];
                        String[] encodeArr = new String[] { "UTF-8", "iso-8859-1", "gbk",
                                "gb2312" };

                        for (int q = 0; q < encodeArr.length; q++) {
                            for (int w = 0; w < encodeArr.length; w++) {
                                String stg;
                                try {
                                    stg = new String(audit_desc.getBytes(encodeArr[w]),
                                            encodeArr[q]);
                                    System.out.println(encodeArr[w] + " --" + encodeArr[q]
                                            + "      msg:" + stg);
                                } catch (UnsupportedEncodingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        requestJSON.put(key.toString(), audit_desc);
                    }else{
                        requestJSON.put(key.toString(), requestMap.get(key)[i]);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        BufferedReader br;
        String postStr = null;
        try {
            br = getRequest().getReader();
            String buffer = null;
            StringBuffer buff = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                buff.append(buffer + "\n");
            }
            br.close();
            postStr = buff.toString();
            logger.debug("接收-----------------------微信支付-------------post发送数据:\n" + postStr);
            if(postStr.equals( new String(postStr.getBytes("iso-8859-1"), "iso-8859-1")))
            {
                postStr = new String(postStr.getBytes("iso-8859-1"), "UTF-8");
                logger.debug("微信支付回调------转码之后(iso-8859-1--->utf-8)——————————jsonStr:" + postStr);
            }
            if(postStr.equals( new String(postStr.getBytes("GBK"), "GBK")))
            {
                postStr = new String(postStr.getBytes("GBK"), "UTF-8");
                logger.debug("微信支付回调------转码之后(GBK-->utf-8)——————————jsonStr:" + postStr);
            }
            String result_json=commenUtil.xml2JSON(postStr);
            logger.debug("xml-------->json:" + result_json);
            JSONObject rjson = new JSONObject(result_json);
            String return_code=rjson.getString("return_code");
           // String return_msg=rjson.getString("return_msg");
            if("success".equalsIgnoreCase(return_code))
            {
                String result_code=rjson.getString("result_code");
                if("SUCCESS".equalsIgnoreCase(result_code))
                {
                    String appid=rjson.getString("appid");
                    List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, appid, true);


                    //String shop_appsecret=weiXinAuthorizationList.get(0).getNum_secret();//商户appsecret
                    String shop_appsecret=weiXinAuthorizationList.get(0).getAlias();//商户api秘钥

                    String attach=rjson.getString("attach");
                    String bank_type=rjson.getString("bank_type");
                    String cash_fee=rjson.getString("cash_fee");
                    String device_info=rjson.getString("device_info");
                    String fee_type=rjson.getString("fee_type");
                    String is_subscribe=rjson.getString("is_subscribe");
                    String mch_id=rjson.getString("mch_id");
                    String nonce_str=rjson.getString("nonce_str");
                    String openid=rjson.getString("openid");
                    String out_trade_no=rjson.getString("out_trade_no");
                    String time_end=rjson.getString("time_end");
                    String total_fee=rjson.getString("total_fee");
                    String trade_type=rjson.getString("trade_type");
                    String transaction_id=rjson.getString("transaction_id");
                    String sign=rjson.getString("sign");
                    /*Map<String, String> return_map = new HashMap<>();
                    return_map.put("appid",appid);
                    return_map.put("attach",attach);
                    return_map.put("bank_type",bank_type);
                    return_map.put("cash_fee",cash_fee);
                    return_map.put("device_info",device_info);
                    return_map.put("fee_type",fee_type);
                    return_map.put("is_subscribe",is_subscribe);
                    return_map.put("mch_id",mch_id);
                    return_map.put("nonce_str",nonce_str);
                    return_map.put("openid",openid);
                    return_map.put("out_trade_no",out_trade_no);
                    return_map.put("time_end",time_end);
                    return_map.put("total_fee",total_fee);
                    return_map.put("trade_type",trade_type);
                    return_map.put("transaction_id",transaction_id);
                    return_map.put("return_code",return_code);
                    return_map.put("result_code",result_code);*/

                    Document doc = DocumentHelper.parseText(postStr);
                    Element root = doc.getRootElement();
                    Map<String, String> return_map = (Map<String, String>) commenUtil.xml2map(root);
                    return_map.remove("sign");
                    String sign_local = commenUtil.getSign(shop_appsecret, return_map, "utf-8");
                    Map<String, String> return_map_xml = new HashMap<>();
                    //List<OrderInfo> orderInfos=infoDaoimpl.listByCriteria(OrderInfo.class, out_trade_no, true);
                    Date date=new Date();
                    String dateStr_new="";
                    try
                    {
                        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = oldFormat.parse(time_end);
                        dateStr_new= newFormat.format(date);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    if(sign_local.equals(sign))
                    {
                        logger.debug("支付回调签名验证通过");
                        return_map_xml.put("return_code", "SUCCESS");
                        return_map_xml.put("return_msg","OK");
                        try {
                            getResponse().getWriter().write("<xml>\n" +
                                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                                    "</xml>");
                            if(weiXinAuthorizationList.get(0).getReturn_url() !=null && !weiXinAuthorizationList.get(0).getReturn_url().equals(""))
                            {
                                String response=httpsPostMethod.postHttp("balance_weichat_callback", "{\"orderNo\":\"" + out_trade_no + "\",\"weichatOpenid\":\"" + openid + "\",\"remoteOrderNo\":\"" + transaction_id + "\",\"appid\":\"" + appid + "\",\"amount\":" + Long.parseLong(cash_fee) + "}", weiXinAuthorizationList.get(0).getReturn_url());
                            }
                            else
                            {
                                String pay_result=httpsPostMethod.postHttp("asyncPayResult", "{\"orderId\":\"" + out_trade_no + "\",\"payResult\":\"1\"}", asyncPayResult_url);
                            }
                            //openid，attach（自定义数据包--商品介绍，商户名称），total_fee（支付金额），transaction_id（微信支付订单号）
                            // ,appid（商户appid），shopid(商户id)
                            //威富通广告投放
                            String getuserinfo = httpsPostMethod.sendHttpsPost(
                                    "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAuthAccesstoken(appid) + "&openid=" + openid
                                            + ""
                                    , "&lang=zh_CN",
                                    "获取获取openid");
                            if(weiXinAuthorizationList.get(0).getPrepare1() !=null && "1".equals(weiXinAuthorizationList.get(0).getPrepare1()))
                            {
                                String advert = getAdvert(openid,appid,getuserinfo);
                                JSONObject advert_json = new JSONObject(advert);
                                String imgPath="";//广告地址
                                String url="";//外链地址
                                String desc="";//广告介绍
                                String imgWeixinPath="";//微信端地址
                                if(advert_json.getBoolean("success"))
                                {
                                    imgPath=advert_json.getString("imgPath");//广告地址
                                    url=advert_json.getString("url");//外链地址
                                    desc=advert_json.getString("desc");//广告介绍
                                    imgWeixinPath=advert_json.getString("imgWeixinPath");//微信端地址

                                    //保存威富通回传的外链地址及广告地址
                                    Conjunction conj = Restrictions.conjunction();
                                    conj.add(Restrictions.eq("advertOrder_picurl", imgPath));
                                    List<AdvertOrder> advertOrders=advertOrderDaoimpl.listByCriteria(AdvertOrder.class,conj,true);
                                    AdvertOrder advertOrder;
                                    if(advertOrders != null && advertOrders.size() > 0)
                                    {
                                        advertOrder=advertOrders.get(0);
                                    }
                                    else
                                    {
                                        advertOrder=new AdvertOrder();
                                    }
                                    //advertOrder.setAdvertOrder_num(transaction_id);
                                    advertOrder.setAdvertOrder_picurl(imgPath);
                                    advertOrder.setAdvertOrder_shopid(weiXinAuthorizationList.get(0).getShop_id() == null ? ""
                                            : weiXinAuthorizationList.get(0).getShop_id());
                                    advertOrder.setAdvertOrder_shopname(attach);
                                    advertOrder.setAdvertOrder_goodsIntroduction(desc);
                                    advertOrder.setAdvertOrder_url(url);
                                    advertOrder.setAdvertOrder_imgWeixinPath(imgWeixinPath);
                                    advertOrderDaoimpl.insert(advertOrder);

                                    JSONObject userinfo_json=new JSONObject(getuserinfo);
                                    String sex="1";
                                    String nickname="";
                                    try
                                    {
                                        sex=userinfo_json.get("sex").toString();
                                        nickname=userinfo_json.getString("nickname");
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }


                                    //保存用户订单信息及广告链接地址
                                    AdvertUser advertUser=new AdvertUser();
                                    advertUser.setAdvertOrder_picurl(imgPath);
                                    advertUser.setAdvertUser_sex(sex);
                                    advertUser.setAdvertUser_openid(openid);
                                    advertUser.setAdvertUser_name(nickname);
                                    advertUser.setAdvertUser_is_click(2);
                                    advertUser.setAdvertUser_cash(Double.parseDouble(total_fee));
                                    advertUser.setAdvertOrder_appid(appid);
                                    advertUser.setAdvertOrder_imgWeixinPath(imgWeixinPath);
                                    advertUser.setAdvertOrder_num(transaction_id);
                                    advertUser.setAdvertOrder_url(url);
                                    advertUser.setSendTime(new Date());
                                    advertOrderDaoimpl.insert(advertUser);
                                }
                                else
                                {
                                    imgWeixinPath="http://www.pandaonline.com.cn/image/20170406155337.jpg";
                                    url="http://www.pandaonline.com.cn/2017_cms/login.asp?qd=41";
                                }
                                Date nowTime = new Date(System.currentTimeMillis());
                                SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String retStrFormatNowDate = sdFormatter.format(nowTime);
                                if(retStrFormatNowDate.compareTo("2017-06-17")<0)
                                {
                                    imgWeixinPath="https://advert.swiftpass.cn/pic/adpic/adbody/2017/06/12/542d261d-6846-433b-9684-511aa37b519d.jpg";
                                    url="http://adfarm.mediaplex.com/ad/ck/10592-210881-62740-0";
                                }

                                String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+getAuthAccesstoken(appid),
                                        creatNews(openid,transaction_id,attach,total_fee,dateStr_new,imgWeixinPath,url),"");
                                getResponse().getWriter().write("");
                            }




                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        logger.debug("支付回调签名验证失败");
                        return_map_xml.put("return_code","FAIL");
                        return_map_xml.put("return_msg","签名验证失败");

                        String pay_result=httpsPostMethod.postHttp("asyncPayResult","{\"orderId\":\""+out_trade_no+"\",\"payResult\":\"2\"}",asyncPayResult_url);
                        try {
                            getResponse().getWriter().write(commenUtil.map2xml(return_map_xml));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }



            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e2)
        {
            e2.printStackTrace();
        }

    }

    /**
     * 获取用户openid
     * @param jsonStr 包含商户app_id 商户appsecret 商户code 券id
     * @return
     */
    public String getOpenId(String jsonStr)
    {
        Map<String, String> hashMap = new HashMap<>();

        String shop_appid="";
        String code="";
        String getopenid="";
        String openid="";
        try {
            logger.debug("获取openid前台传给的参数jsonStr-------------》"+jsonStr);
            JSONObject json=new JSONObject(jsonStr);
            shop_appid=json.getString("app_id");
            List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, shop_appid, true);

            /*String shop_appsecret=json.getString("appsecret");*/
            String shop_appsecret=weiXinAuthorizationList.get(0).getNum_secret();
            code=json.getString("code");
            logger.info("获取openid所需APPsecret："+shop_appsecret);
            getopenid = httpsPostMethod.sendHttpsPost(
                  "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                         + shop_appid + "&secret=" + shop_appsecret + "&code=" + code, "&grant_type=authorization_code",
                   "获取获取openid");

                 /*getopenid=httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=" + shop_appid +
                        "&code=" + code +
                        "&grant_type=authorization_code&component_appid=" + app_id +
                        "&component_access_token=" + getComponentAccessToken(), "", "");*/

            /*String getopenid = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                            + shop_appid + "&secret=" + shop_appsecret + "&code=" + code, "&grant_type=authorization_code",
                    "获取获取openid");*/



            logger.debug("获取open_id 微信返回的数据getopenid---------------------》" + getopenid);
            JSONObject openid_js=new JSONObject(getopenid);
            openid=openid_js.getString("openid");
            hashMap.put(shop_appid, openid);
        } catch (Exception e)
        {
            getopenid=httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=" + shop_appid +
                    "&code=" + code +
                    "&grant_type=authorization_code&component_appid=" + app_id +
                    "&component_access_token=" + getComponentAccessToken(), "", "");
            logger.debug("获取open_id 微信返回的数据getopenid---------------------》" + getopenid);
            JSONObject openid_js= null;
            try {
                openid_js = new JSONObject(getopenid);
                openid=openid_js.getString("openid");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            hashMap.put(shop_appid, openid);
        }
        return hashMap.get(shop_appid);
    }

    /**
     * 获取微信用户信息
     * @return
     */
    public void getUserInfo()
    {
        String callback = this.getRequest().getParameter("callbackparam");
        String jsonStr1= this.getRequest().getParameter("jsonStr");
        JSONObject requestJSON = new JSONObject();
        JSONObject json= null;
        String shop_appid="";//商户appid
        String openid="";
        //String shop_appid="wxa2b490a571ba7962";
        try {
            json = new JSONObject(jsonStr1);
            shop_appid=json.getString("app_id");
            openid=json.getString("openid");
            logger.debug("前台页面回传的openid-------------》"+openid);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e)
        {
            openid="";
            e.printStackTrace();
        }
        String getuserinfo="";
        if(openid==null || "".equals(openid))
        {
             getuserinfo = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+getAuthAccesstoken(shop_appid)+"&openid="+getOpenId(jsonStr1)
                            +""
                    , "&lang=zh_CN",
                    "获取获取openid");
        }
        else
        {
             getuserinfo = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+getAuthAccesstoken(shop_appid)+"&openid="+openid
                            +""
                    , "&lang=zh_CN",
                    "获取获取openid");
        }


        logger.debug("通过openid获取用户信息 微信返回的数据userinfo---------------------》" + getuserinfo);

        try {
            JSONObject userinfo_json=new JSONObject(getuserinfo);
            //String nickname=userinfo_json.getString("nickname");
            requestJSON.put("userinfo",userinfo_json);
            requestJSON.put("rspCode","000");
            requestJSON.put("rspDesc","成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = callback + "(" + requestJSON + ")";
        this.getResponse().setCharacterEncoding("UTF-8");
        this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
        logger.debug("H通过openid获取用户信息userinfo--response数据为：---" + result);
        try {
            this.getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // return "";
    }


    /**
     * 获取公众号基本信息
     * @return
     */
    public String getShopPublicNumberInfo(String authorizer_appid)
    {

        String ShopPublicNumberInfo = httpsPostMethod.sendHttpsPost(
                "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=" + getComponentAccessToken()
                , "{\"component_appid\":\"" + app_id + "\",\"authorizer_appid\":\"" + authorizer_appid + "\"}",
                "获取公众号基本信息");

        logger.debug("通过authorizer_appid获取的商户公众号信息---------------------》" + ShopPublicNumberInfo);

        return ShopPublicNumberInfo;

    }

    /**
     * 微信通用接口回调
     */
    public void commonReturn_url() {

        logger.debug("接受的微信支付结果返回的路径地址---------------》" + getRequest().getRequestURL());
        String aa="";
        try {
             aa = getRequest().getParameter("appid");
            logger.debug("传过来的appid为------------------》" +aa);
        } catch (Exception e) {
            logger.debug("@@@@@@@@@压根就收不到@@@@@@@");

            e.printStackTrace();
        }

        Map<String, String[]> requestMap = getRequest().getParameterMap();
        JSONObject requestJSON = new JSONObject();
        for (Object key : requestMap.keySet()) {
            for (int i = 0; i < requestMap.get(key).length; i++) {
                logger.debug("微信支付回调结果_GET的键= " + key + "; 值" + i
                        + "= " + requestMap.get(key)[i]);
                try {
                    if ("audit_desc".equals(key.toString())) {
                        String audit_desc = requestMap.get(key)[i];
                        String[] encodeArr = new String[]{"UTF-8", "iso-8859-1", "gbk",
                                "gb2312"};

                        for (int q = 0; q < encodeArr.length; q++) {
                            for (int w = 0; w < encodeArr.length; w++) {
                                String stg;
                                try {
                                    stg = new String(audit_desc.getBytes(encodeArr[w]),
                                            encodeArr[q]);
                                    System.out.println(encodeArr[w] + " --" + encodeArr[q]
                                            + "      msg:" + stg);
                                } catch (UnsupportedEncodingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        requestJSON.put(key.toString(), audit_desc);
                    } else {
                        requestJSON.put(key.toString(), requestMap.get(key)[i]);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        BufferedReader br;
        String postStr = null;
        try {
            br = getRequest().getReader();
            String buffer = null;
            StringBuffer buff = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                buff.append(buffer + "\n");
            }
            //getResponse().getWriter().write("success");
            br.close();
            /*getResponse().setContentType("text/html;charset=UTF-8");*/

            postStr = buff.toString();
            logger.debug("接收-------------（授权后）公众号消息与事件接收---------post发送数据:\n" + postStr);
            if (postStr.equals(new String(postStr.getBytes("iso-8859-1"), "iso-8859-1"))) {
                postStr = new String(postStr.getBytes("iso-8859-1"), "UTF-8");
                logger.debug("（授权后）公众号消息与事件接收------转码之后(iso-8859-1--->utf-8)——————————jsonStr:" + postStr);
            }
            if (postStr.equals(new String(postStr.getBytes("GBK"), "GBK"))) {
                postStr = new String(postStr.getBytes("GBK"), "UTF-8");
                logger.debug("（授权后）公众号消息与事件接收------转码之后(GBK-->utf-8)——————————jsonStr:" + postStr);
            }
            try {
                getResponse().getWriter().append("success");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           String result_json= WeiXinDecode.decode(token, key, app_id, getRequest().getParameter("msg_signature"), getRequest().getParameter("timestamp"), getRequest().getParameter("nonce")
                   , postStr);
            getResponse().getWriter().write("");
            logger.debug("公众号消息与事件接收post的数据json---------------》"+result_json);
            JSONObject json_tick=new JSONObject(result_json);
            String MsgType=json_tick.getString("MsgType");
            String ToUserName=json_tick.getString("ToUserName");//微信公众号
            String FromUserName=json_tick.getString("FromUserName");//用户openid


            String event="";
            if("event".equalsIgnoreCase(MsgType))
            {
                event=json_tick.getString("Event");
            }
            if("text".equalsIgnoreCase(MsgType))
            {
                event="text";
            }
            if("image".equalsIgnoreCase(MsgType))
            {
                event="image";
            }
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, key, app_id);
            if(ToUserName.equals("gh_3c884a361561"))
            {
                String Content="";
                if(json_tick.toString().contains("Content"))
                {
                    Content=json_tick.getString("Content");
                }
                //String

                if ("event".equals(MsgType)) {
                    logger.debug("------------------------------第一步-------------------");
                    event = json_tick.getString("Event");

                    String quanwang_event = event + "from_callback";

                    Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
                    StringBuffer sb = new StringBuffer();
                    sb.append("<xml>");
                    sb.append("<ToUserName><![CDATA["+FromUserName+"]]></ToUserName>");
                    sb.append("<FromUserName><![CDATA["+ToUserName+"]]></FromUserName>");
                    sb.append("<CreateTime>"+createTime+"</CreateTime>");
                    sb.append("<MsgType><![CDATA[text]]></MsgType>");
                    sb.append("<Content><![CDATA[" + quanwang_event + "]]></Content>");
                    sb.append("</xml>");
                    String replyMsg = sb.toString();

                    logger.debug("测试全网发布=========event（加密之前）=======" + replyMsg);
                    String quanwang_event_xml_miwen = pc.encryptMsg(replyMsg, createTime.toString(), getRequest().getParameter("nonce"));
                    logger.debug("\"测试全网发布=========event（加密之后）=======\"" + quanwang_event_xml_miwen);
                    logger.debug("----bb---------"+bb);
                    String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+bb,
                            "{\"touser\":\""+FromUserName+"\",\"msgtype\":\""+"text"+"\",\"text\":{\"content\":\""+quanwang_event+"\"}}","");
                    String aa_event="{\"touser\":\""+FromUserName+"\",\"msgtype\":\""+"text"+"\",\"text\":{\"content\":\""+quanwang_event+"\"}}";
                    logger.debug("全网发布测试event-----------》" + responseStr+"--------------实例字符串-----------------》"+aa_event);

                    this.getResponse().getWriter().write("");
                    this.getResponse().getWriter().write(quanwang_event_xml_miwen);
                    this.getResponse().getWriter().flush();
                    this.getResponse().getWriter().close();

                }
                else if ("text".equals(MsgType) && !Content.contains("QUERY_AUTH_CODE:")) {
                    logger.debug("------------------------------第二步-------------------");
                    String quanwang_content = json_tick.getString("Content");

                    if ("TESTCOMPONENT_MSG_TYPE_TEXT".equals(quanwang_content)) {
                        quanwang_content = "TESTCOMPONENT_MSG_TYPE_TEXT_callback";


                        Long createTime = System.currentTimeMillis() / 1000;
                        StringBuffer sb = new StringBuffer(512);
                        sb.append("<xml>");
                        sb.append("<ToUserName><![CDATA["+FromUserName+"]]></ToUserName>");
                        sb.append("<FromUserName><![CDATA["+ToUserName+"]]></FromUserName>");
                        sb.append("<CreateTime>"+createTime+"</CreateTime>");
                        sb.append("<MsgType><![CDATA[text]]></MsgType>");
                        sb.append("<Content><![CDATA["+quanwang_content+"]]></Content>");
                        sb.append("</xml>");
                        String replyMsg = sb.toString();
                        logger.info("确定发送的XML为：" + replyMsg);//千万别加密

                        //String quanwang_content_xml = commenUtil.map2xml(hashMap);
                        logger.debug("测试全网发布=========text（加密之前）=======" + replyMsg);
                        String quanwang_content_xml_miwen = pc.encryptMsg(replyMsg, createTime.toString(), getRequest().getParameter("nonce"));
                        logger.debug("\"测试全网发布=========text（加密之后）=======\"" + quanwang_content_xml_miwen);
                        getResponse().setContentType("text/html;charset=UTF-8");
                        getResponse().getWriter().write("");
                        this.getResponse().getWriter().flush();
                        this.getResponse().getWriter().close();
                        getResponse().getWriter().write(quanwang_content_xml_miwen);
                        this.getResponse().getWriter().flush();
                        this.getResponse().getWriter().close();
                        //returnJSON(quanwang_content_xml_miwen, this.getResponse());

                        logger.debug("----bb---------"+bb);
                        String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+bb,
                                "{\"touser\":\""+FromUserName+"\",\"msgtype\":\""+"text"+"\",\"text\":{\"content\":\""+quanwang_content+"\"}}","");
                        String aa_event="{\"touser\":\""+FromUserName+"\",\"msgtype\":\""+"text"+"\",\"text\":{\"content\":\""+quanwang_content+"\"}}";
                        logger.debug("全网发布测试test-----------》" + responseStr + "--------------实例字符串-----------------》" + aa_event);

                        this.getResponse().getWriter().write(quanwang_content_xml_miwen);
                        this.getResponse().getWriter().flush();
                        this.getResponse().getWriter().close();
                    }


                }


            //3.全网发布第三步
                else if(Content.contains("QUERY_AUTH_CODE:"))

                {
                    logger.debug("------------------------------第三步-------------------");
                    String queryAuthCode=Content.substring(16);
                    logger.debug("------截取的QUERY_AUTH_CODE为--------"+queryAuthCode);
                    String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="+getComponentAccessToken(),
                            "{\"component_appid\":\""+app_id+"\",\"authorization_code\":\""+queryAuthCode+"\"}","全网发布根据微信传的QUERY_AUTH_CODE获取token");
                    JSONObject rjson = new JSONObject(responseStr);
                    String authorization_info=rjson.get("authorization_info")+"";
                    JSONObject js_authorization = new JSONObject(authorization_info);
                    String authorizer_appid=js_authorization.getString("authorizer_appid");//授权方appid
                    String authorizer_access_token=js_authorization.getString("authorizer_access_token");//授权方token
                    long expires_in=js_authorization.getLong("expires_in");
                    String authorizer_refresh_token=js_authorization.getString("authorizer_refresh_token");
                    org.json.JSONArray func_info=js_authorization.getJSONArray("func_info");
                    String kefu_response_1= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + authorizer_access_token,
                            "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + "" + "\"}}", "");
                    logger.debug("全网发布第三步----1-----" + kefu_response_1);
                    bb=authorizer_access_token;
                    logger.debug("----bb---------"+bb);
                    this.getResponse().getWriter().write("");
                    this.getResponse().getWriter().flush();
                    this.getResponse().getWriter().close();
                    String kefu_response_2= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + authorizer_access_token,
                            "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + queryAuthCode+"_from_api" + "\"}}", "");
                    logger.debug("全网发布第三步----2-----" + kefu_response_2);



                }
            }
            //String event=json_tick.getString("Event");



            if("subscribe".equals(event))
            {
                WeiXin_TuiGuang_Details wtd=new WeiXin_TuiGuang_Details();
                String EventKey="";
                try {
                    EventKey=json_tick.get("EventKey")+"";//二维码参数值
                    logger.debug("关注之后返回数据：微信公众号：-----------》"+ToUserName+"用户openid：————————》"+FromUserName+"二维码参数值：——————————》"+EventKey);
                    logger.debug("@@@@@@@@@@@@@@@@@@@欢迎订阅@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+aa);
                    if(EventKey.contains("last_trade_no"))
                    {
                        //POS机扫码支付发送广告
                        //威富通广告投放
                        String getuserinfo = httpsPostMethod.sendHttpsPost(
                                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAuthAccesstoken(aa.substring(1)) + "&openid=" + FromUserName
                                        + ""
                                , "&lang=zh_CN",
                                "获取获取openid");
                        String trade_no=EventKey.substring(14);//微信端订单号
                        List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, aa.substring(1), true);
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("appid",aa.substring(1));
                        hashMap.put("mch_id",weiXinAuthorizationList.get(0).getMuch_id());
                        hashMap.put("transaction_id",trade_no);
                        hashMap.put("nonce_str",commenUtil.createSuiJi());
                        logger.debug("生成查询订单的字符串"+hashMap.toString());
                        String sign=commenUtil.getSign(weiXinAuthorizationList.get(0).getAlias(), hashMap, "utf-8");
                        hashMap.put("sign",sign);
                        String xml=commenUtil.map2xml(hashMap);
                        logger.debug("查询订单转换成的map转换成的xml为----------------------》"+xml);
                        String responseStr= httpsPostMethod.sendHttpsPost("https://api.mch.weixin.qq.com/pay/orderquery",
                                xml,"微信查询订单");
                        getResponse().getWriter().write("");
                        logger.debug("微信查询订单，微信返回的数据为----------------------》" + responseStr);
                        JSONObject rjson = new JSONObject(commenUtil.xml2JSON(responseStr));
                        String return_code=rjson.getString("return_code");
                        String return_msg=rjson.getString("return_msg");
                        /*String result_code=rjson.getString("result_code");*/
                        String total_fee="";
                        String attach="";
                        String time_end="";
                        if("SUCCESS".equals(return_code))
                        {
                            String result_code=rjson.getString("result_code");
                            if("SUCCESS".equalsIgnoreCase(result_code))
                            {
                                total_fee=rjson.get("total_fee").toString();
                                attach=rjson.getString("attach");
                                Date date=new Date();
                                time_end=rjson.getString("time_end");
                                try
                                {
                                    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    date = oldFormat.parse(time_end);
                                    time_end= newFormat.format(date);
                                    if(weiXinAuthorizationList.get(0).getPrepare1() !=null && "1".equals(weiXinAuthorizationList.get(0).getPrepare1()))
                                    {
                                        logger.debug("进入威富通广告================");
                                        String advert = getAdvert(FromUserName,aa.substring(1),getuserinfo);
                                        JSONObject advert_json = new JSONObject(advert);
                                        String imgPath="";//广告地址
                                        String url="";//外链地址
                                        String desc="";//广告介绍
                                        String imgWeixinPath="";//微信端地址
                                        if(advert_json.getBoolean("success"))
                                        {
                                            imgPath=advert_json.getString("imgPath");//广告地址
                                            url=advert_json.getString("url");//外链地址
                                            desc=advert_json.getString("desc");//广告介绍
                                            imgWeixinPath=advert_json.getString("imgWeixinPath");//微信端地址

                                            //保存威富通回传的外链地址及广告地址
                                            Conjunction conj = Restrictions.conjunction();
                                            conj.add(Restrictions.eq("advertOrder_picurl", imgPath));
                                            List<AdvertOrder> advertOrders=advertOrderDaoimpl.listByCriteria(AdvertOrder.class,conj,true);
                                            AdvertOrder advertOrder;
                                            if(advertOrders != null && advertOrders.size() > 0)
                                            {
                                                advertOrder=advertOrders.get(0);
                                            }
                                            else
                                            {
                                                advertOrder=new AdvertOrder();
                                            }
                                            //advertOrder.setAdvertOrder_num(transaction_id);
                                            advertOrder.setAdvertOrder_picurl(imgPath);
                                            advertOrder.setAdvertOrder_shopid(weiXinAuthorizationList.get(0).getShop_id() == null ? ""
                                                    : weiXinAuthorizationList.get(0).getShop_id());
                                            advertOrder.setAdvertOrder_shopname(attach);
                                            advertOrder.setAdvertOrder_goodsIntroduction(desc);
                                            advertOrder.setAdvertOrder_url(url);
                                            advertOrder.setAdvertOrder_imgWeixinPath(imgWeixinPath);
                                            advertOrderDaoimpl.insert(advertOrder);

                                            JSONObject userinfo_json=new JSONObject(getuserinfo);
                                            String sex="1";
                                            String nickname="";
                                            try
                                            {
                                                sex=userinfo_json.get("sex").toString();
                                                nickname=userinfo_json.getString("nickname");
                                            }catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }


                                            //保存用户订单信息及广告链接地址
                                            AdvertUser advertUser=new AdvertUser();
                                            advertUser.setAdvertOrder_picurl(imgPath);
                                            advertUser.setAdvertUser_sex(sex);
                                            advertUser.setAdvertUser_openid(FromUserName);
                                            advertUser.setAdvertUser_name(nickname);
                                            advertUser.setAdvertUser_is_click(2);
                                            advertUser.setAdvertUser_cash(Double.parseDouble(total_fee));
                                            advertUser.setAdvertOrder_appid(aa.substring(1));
                                            advertUser.setAdvertOrder_imgWeixinPath(imgWeixinPath);
                                            advertUser.setAdvertOrder_num(trade_no);
                                            advertUser.setAdvertOrder_url(url);
                                            advertUser.setSendTime(new Date());
                                            advertOrderDaoimpl.insert(advertUser);
                                        }
                                        else
                                        {
                                            imgWeixinPath="http://www.pandaonline.com.cn/image/20170406155337.jpg";
                                            url="http://www.pandaonline.com.cn/2017_cms/login.asp?qd=41";
                                        }
                                       String responseStr_Sub= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+getAuthAccesstoken(aa.substring(1)),
                                                creatNews(FromUserName,trade_no,attach,total_fee,time_end,imgWeixinPath,url),"");
                                    }
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }


                    }
                    else {

                        if(EventKey.contains("qrscene_"))
                        {
                            logger.debug("该次关注是扫描带参数的二维码,场景关注");
                            String tuandui_num =EventKey.substring(8);//推广团队号
                            wtd.setAppid(aa.substring(1));
                            wtd.setWeixin_num(ToUserName);
                            wtd.setDetails_openid(FromUserName);
                            wtd.setDetails_tuiguang_num(tuandui_num);
                            wtd.setDetails_CREATED(new Date());
                            gabadaoimpl.insert(wtd);
                        }
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    logger.debug("该次关注不是扫描带参数的二维码，正常关注");
                }
                String content="";
                Conjunction conj = Restrictions.conjunction();
                conj.add(Restrictions.eq("appid", aa.substring(1)));
                //conj.add(Restrictions.eq("replyType", "1"));
                conj.add(Restrictions.eq("messageTypeNum", "1"));
                conj.add(Restrictions.eq("isStart", "1"));
                List<AutoMessage> autoMessageList=autoMessageDaoimpl.listByCriteria(AutoMessage.class, conj, true);
                if(autoMessageList==null || autoMessageList.size()==0)
                {
                    /*content="";
                    String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                            "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + content + "\"}}", "");
                    getResponse().getWriter().write("");*/
                }
                else
                {
                    if ("1".equals(autoMessageList.get(0).getReplyType())) {
                        String responseStr = httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                                "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + content + "\"}}", "");
                        getResponse().getWriter().write("");
                    }
                    if ("2".equals(autoMessageList.get(0).getReplyType())) {
                        content = autoMessageList.get(0).getMediaId();
                        if (content == null || content.equals("")) {
                            content = "mu2k6VhUoWnIObzykjSQHaHoU-Oze9DGeNj6UGY_LgTtZSuJu2UJtZU-DNDvA82Z";
                        }
                        String responseStr = httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                                "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "image" + "\",\"image\":{\"media_id\":\"" + content + "\"}}", "");
                        getResponse().getWriter().write("");
                    }
                    if("3".equals(autoMessageList.get(0).getReplyType()))
                    {
                        //回复图文
                        String news=creatReplyNews(FromUserName,autoMessageList.get(0).getDescription(),
                                autoMessageList.get(0).getMediaurl(),autoMessageList.get(0).getOutside_url(),
                                autoMessageList.get(0).getTitle());
                        String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),news, "");
                        getResponse().getWriter().write("");
                    }
                }

            }
            else if("card_pass_check".equalsIgnoreCase(event))
            {//卡券审核通过
                logger.debug("卡券通过审核");

                JSONObject requestCardJSON = new JSONObject();
                try {
                    requestCardJSON.put("CardId", json_tick.getString("CardId"));
                    requestCardJSON.put("Result", "success");
                } catch (JSONException e) {
                    logger.debug("处理微信推送的XML-卡券通过审核" + e.getMessage());
                    e.printStackTrace();
                }
                String response=httpsPostMethod.postHttp("CheckCoupon",requestCardJSON.toString(),cardCherkReturn_url);

            }
            else if ("card_not_pass_check".equals(event)) {
                logger.debug("卡券未通过审核");
                JSONObject requestCardJSON = new JSONObject();
                try {
                    requestCardJSON.put("CardId", json_tick.getString("CardId"));
                    requestCardJSON.put("Result", "fail");

                } catch (JSONException e) {
                    logger.debug("处理微信推送的XML-卡券未通过审核" + e.getMessage());
                    e.printStackTrace();
                }
                String response=httpsPostMethod.postHttp("CheckCoupon",requestCardJSON.toString(),cardCherkReturn_url);

            }
            else if ("poi_check_notify".equals(event)) {
                logger.debug("门店审核结果事件");
                JSONObject requestMenDianJSON = new JSONObject();
                try {
                    requestMenDianJSON.put("CreateTime", json_tick.getString("CreateTime"));
                    requestMenDianJSON.put("UniqID", json_tick.getString("UniqId"));
                    requestMenDianJSON.put("PoiId", json_tick.getString("PoiId"));
                    requestMenDianJSON.put("Result", json_tick.getString("result"));
                    if(!"succ".equals(json_tick.getString("result")))
                    {
                        requestMenDianJSON.put("Msg", json_tick.getString("msg"));
                    }
                    else
                    {
                        requestMenDianJSON.put("Msg", "");
                    }

                } catch (JSONException e) {
                    logger.debug("处理微信推送的XML-门店审核结果事件" + e.getMessage());
                    e.printStackTrace();
                }
                String response=httpsPostMethod.postHttp("CheckStore",requestMenDianJSON.toString(),sotreCheckReturn_url);
            }
            else if ("user_get_card".equals(event)) {
                logger.debug("用户领取卡券");
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("out_act_id",json_tick.getString("CardId")));
                parameters.add(new BasicNameValuePair("coupon_id",json_tick.getString("UserCardCode")));
                parameters.add(new BasicNameValuePair("verificationFlag","false"));
                parameters.add(new BasicNameValuePair("channelId", "21"));
                // 微信-接口-apay 微信用户领券后回调通知
                httpsPostMethod.sendHttpPostA(apayUrl+"/apay-ws/coupon/couponClaim.do", parameters, "微信用户领券");
            }
            else if ("text".equals(event)&&!(ToUserName.equals("gh_3c884a361561"))) {
                logger.debug("用户回复文本消息");
                String content="";
                Conjunction conj = Restrictions.conjunction();
                conj.add(Restrictions.eq("appid", aa.substring(1)));
                conj.add(Restrictions.eq("messageTypeNum", "2"));
                conj.add(Restrictions.eq("isStart", "1"));
                List<AutoMessage> autoMessageList=autoMessageDaoimpl.listByCriteria(AutoMessage.class, conj, true);
                if(autoMessageList==null || autoMessageList.size()==0)
                {
                    /*content="您好！";
                    String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                            "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + content + "\"}}", "");
                    getResponse().getWriter().write("");*/
                }
                else
                {
                    if ("1".equals(autoMessageList.get(0).getReplyType())) {
                        String responseStr = httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                                "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + content + "\"}}", "");
                        getResponse().getWriter().write("");
                    }
                    else if ("2".equals(autoMessageList.get(0).getReplyType())) {
                        content = autoMessageList.get(0).getMediaId();
                        if (content == null || content.equals("")) {
                            content = "mu2k6VhUoWnIObzykjSQHaHoU-Oze9DGeNj6UGY_LgTtZSuJu2UJtZU-DNDvA82Z";
                        }
                        String responseStr = httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                                "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "image" + "\",\"image\":{\"media_id\":\"" + content + "\"}}", "");
                        getResponse().getWriter().write("");
                    }
                    else if("3".equals(autoMessageList.get(0).getReplyType()))
                    {
                        //回复图文
                        String news=creatReplyNews(FromUserName,autoMessageList.get(0).getDescription(),
                                autoMessageList.get(0).getMediaurl(),autoMessageList.get(0).getOutside_url(),
                                autoMessageList.get(0).getTitle());
                        String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),news, "");
                        getResponse().getWriter().write("");
                    }
                }
            }
            else if ("image".equals(event)&&!(ToUserName.equals("gh_3c884a361561"))) {
                logger.debug("用户回复图片消息");
                String content="";
                Conjunction conj = Restrictions.conjunction();
                conj.add(Restrictions.eq("appid", aa.substring(1)));
                //conj.add(Restrictions.eq("replyType", "2"));
                conj.add(Restrictions.eq("messageTypeNum", "2"));
                conj.add(Restrictions.eq("isStart", "1"));
                List<AutoMessage> autoMessageList=autoMessageDaoimpl.listByCriteria(AutoMessage.class, conj, true);
                if(autoMessageList==null || autoMessageList.size()==0)
                {
                    /*content="您好！";
                    String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                            "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + content + "\"}}", "");
                    getResponse().getWriter().write("");*/
                }
                else
                {
                    if ("1".equals(autoMessageList.get(0).getReplyType())) {
                        String responseStr = httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                                "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "text" + "\",\"text\":{\"content\":\"" + content + "\"}}", "");
                        getResponse().getWriter().write("");
                    }
                    if ("2".equals(autoMessageList.get(0).getReplyType())) {
                        content = autoMessageList.get(0).getMediaId();
                        if (content == null || content.equals("")) {
                            content = "mu2k6VhUoWnIObzykjSQHaHoU-Oze9DGeNj6UGY_LgTtZSuJu2UJtZU-DNDvA82Z";
                        }
                        String responseStr = httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),
                                "{\"touser\":\"" + FromUserName + "\",\"msgtype\":\"" + "image" + "\",\"image\":{\"media_id\":\"" + content + "\"}}", "");
                        getResponse().getWriter().write("");
                    }
                    if("3".equals(autoMessageList.get(0).getReplyType()))
                    {
                        //回复图文
                        String news=creatReplyNews(FromUserName,autoMessageList.get(0).getDescription(),
                                autoMessageList.get(0).getMediaurl(),autoMessageList.get(0).getOutside_url(),
                                autoMessageList.get(0).getTitle());
                        String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(aa.substring(1)),news, "");
                        getResponse().getWriter().write("");
                    }
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 创建支付图文消息回复格式
     * @param toUserId 用户openid
     * @param transaction_id 微信端订单号
     * @param attach 商户商品相关信息
     * @param total_fee 支付金额
     * @param dateStr_new 支付完成时间
     * @param imgPath 广告地址
     * @param url 外链地址
     * @return
     */
    public String creatNews(String toUserId,String transaction_id,String attach,String total_fee,String dateStr_new
    ,String imgPath,String url)
    {

        JSONObject jo_all = new JSONObject();

        try {
            jo_all.put("touser",toUserId);
            jo_all.put("msgtype","news");
            JSONObject jo_news = new JSONObject();
            JSONArray newsArray = new JSONArray();
            JSONObject jo_newsarray = new JSONObject();
            jo_newsarray.put("title","支付确认");
            jo_newsarray.put("url",transitUrl+"?url="+url+"&transactionid="+transaction_id);
            jo_newsarray.put("picurl",imgPath);
            jo_newsarray.put("description",
                    "付款金额："+Double.parseDouble(total_fee)/100
                    +"\n商户名称："+attach
                    //+"\n商户名称："+info[0]
                    +"\n交易订单："+transaction_id
                    +"\n交易时间："+dateStr_new);
            newsArray.add(jo_newsarray);
            jo_news.put("articles", newsArray);
            jo_all.put("news",jo_news);
            logger.debug("支付图文消息字符串为---------------------->"+jo_all.toString());
            } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo_all.toString();
    }


    /**
     * 正常图文消息回复格式
     * @param toUserId
     * @param description
     * @param mediaurl
     * @param outside_url
     * @return
     */
    public String creatReplyNews(String toUserId,String description,String mediaurl,String outside_url,String title)
    {

        JSONObject jo_all = new JSONObject();

        try {
            jo_all.put("touser",toUserId);
            jo_all.put("msgtype","news");
            JSONObject jo_news = new JSONObject();
            JSONArray newsArray = new JSONArray();
            JSONObject jo_newsarray = new JSONObject();
            jo_newsarray.put("title",title);
            jo_newsarray.put("url",outside_url);
            jo_newsarray.put("picurl",mediaurl);
            jo_newsarray.put("description",description);
            newsArray.add(jo_newsarray);
            jo_news.put("articles", newsArray);
            jo_all.put("news",jo_news);
            logger.debug("图文回复消息字符串为---------------------->"+jo_all.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo_all.toString();
    }

    /**
     *威富通获取广告链接
     * @return
     */
    public String getAdvert(String openid,String appid,String getuserinfo)
    {
        JSONObject jo_Advert = new JSONObject();

        String advert_return="";
        try {
            if("".equals(getuserinfo))
            {
                getuserinfo = httpsPostMethod.sendHttpsPost(
                        "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAuthAccesstoken(appid) + "&openid=" + openid
                                + ""
                        , "&lang=zh_CN",
                        "获取获取openid");
            }

            JSONObject userinfo_json=new JSONObject(getuserinfo);
            logger.debug("威富通获取用户信息，微信返回：" + userinfo_json);
            try
            {
                jo_Advert.put("sex",userinfo_json.get("sex"));
                jo_Advert.put("province",userinfo_json.get("province"));
                jo_Advert.put("city",userinfo_json.get("city"));
            }catch (Exception e)
            {
                jo_Advert.put("sex","1");
                jo_Advert.put("province","");
                jo_Advert.put("city","");
            }
            /*jo_Advert.put("sex",userinfo_json.get("sex"));
            jo_Advert.put("province",userinfo_json.get("province"));
            jo_Advert.put("city",userinfo_json.get("city"));*/
            jo_Advert.put("request_type",0);
            jo_Advert.put("openid",openid);
            jo_Advert.put("advert_type", 0);
            logger.debug("发送给威富通的后台拼接参数&&&&&&&&&&&&&&&&" + jo_Advert.toString());
            advert_return= httpsPostMethod.sendHttpsPost(
                    "https://advert.swiftpass.cn/channel/v2",jo_Advert.toString(),
                    "advert_1473755156372");
//            advert_return=httpsPostMethod.doPostJson("http://advert.test.swiftpass.cn/channel/v2",jo_Advert.toString(),"advert_1473755156372");
            Date nowTime = new Date(System.currentTimeMillis());
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String retStrFormatNowDate = sdFormatter.format(nowTime);
            if(retStrFormatNowDate.compareTo("2017-06-17 00:00:00")<0)
            {
                advert_return="{\"adNumber\":\"AD1704101413\",\"desc\":\"戴尔游匣\",\"imgPath\":\"https://advert.swiftpass.cn/pic/adpic/adbody/2017/06/12/542d261d-6846-433b-9684-511aa37b519d.jpg\",\"msg\":\"50000\",\"success\":true,\"url\":\"http://adfarm.mediaplex.com/ad/ck/10592-210881-62740-0\",\"imgWeixinPath\":\"https://advert.swiftpass.cn/pic/adpic/adbody/2017/06/12/542d261d-6846-433b-9684-511aa37b519d.jpg\"}\n";

            }
            logger.debug("威富通返回的广告链接及外链链接***************"+advert_return);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return advert_return;
    }
    /**
     * 方法描述: 返回数据到请求方
     * @param data 数据
     * @param response
     * @author Andy 2015年9月1日  下午1:06:54
     */
    public void returnJSON(Object data,HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonEncoding encoding = JsonEncoding.UTF8;
            response.setContentType("application/json");
            org.codehaus.jackson.JsonGenerator generator = objectMapper.getJsonFactory().
                    createJsonGenerator(response.getOutputStream(), encoding);
            objectMapper.writeValue(generator, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工具类：回复微信服务器文本消息
     * @param response
     * @param returnvaleue
     */
    public void output(HttpServletResponse response,String returnvaleue){
        try {
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
//          System.out.println(****************returnvaleue***************=+returnvaleue);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ticket
     *
     * @param tuiguangnum 推广团队号
     * @param shop_appid 商户微信appid
     */
    public String getTicket(String tuiguangnum,String shop_appid)
    {
        logger.debug("获取ticket用来生成永久二维码，推广团队代号：------》"+tuiguangnum+"商户appid--------------》"+shop_appid);
        String ticketinfo = httpsPostMethod.sendHttpsPost(
                "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + getAuthAccesstoken(shop_appid)
                , "{\"action_name\":\""+"QR_LIMIT_SCENE"+"\",\"action_info\":{\"scene\":{\"scene_id\":"+tuiguangnum+"}}}",
                "获取ticket");
        logger.debug("请求获取ticket，微信返回的数据：-------------------->" + ticketinfo);
        //String json_ticketinfo= commenUtil.xml2JSON(ticketinfo);
        String ticket="";
        try {
            JSONObject json_tick=new JSONObject(ticketinfo);
            ticket=json_tick.getString("ticket");

        } catch (JSONException e) {
            e.printStackTrace();

        }

        return ticket;
    }
    /**
     * 获取ticket生成永久二维码
     */
    public void getCodeByTicket() throws JSONException, UnsupportedEncodingException {
        String jsonStr1= this.getRequest().getParameter("jsonStr");
        JSONObject json_tick_l=new JSONObject(jsonStr1);

        String callback = this.getRequest().getParameter("callbackparam");
        String weixinnum=json_tick_l.getString("weixinnum");
        String tuiguangnum=json_tick_l.getString("tuiguangnum");
        String shop_appid=json_tick_l.getString("shop_appid");
        logger.debug("获取ticket用来生成永久二维码，微信号为：------------》" + weixinnum + "推广团队代号：------》" + tuiguangnum);
        /*String getuserinfo = httpsPostMethod.sendHttpsPost(
                "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(getTicket(tuiguangnum,shop_appid),"utf-8"), "",
                "获取永久二维码");*/

        //String getuserinfo= httpsPostMethod.doGet("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(getTicket(tuiguangnum, shop_appid),"utf-8"));


        //logger.debug("获取ticket生成永久二维码,微信返回的数据为：@@@@@@@@@@@@@@@@@@" + getuserinfo);
        //String json_code=commenUtil.xml2JSON(getuserinfo);
        String url="";
        String return_url="";

        //JSONObject json_tick=new JSONObject(getuserinfo);
        url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(getTicket(tuiguangnum, shop_appid),"utf-8");
        return_url="{\"url\":\""+url+"\"}";

        String result = callback + "(" + return_url + ")";
        this.getResponse().setCharacterEncoding("UTF-8");
        this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
        logger.debug("H通过openid获取用户信息userinfo--response数据为：---" + result);
        try {
            this.getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义消息格式封装
     * @param FromUserName
     * @param ToUserName
     * @param content_id
     * @return
     */
    public String reDefineMsg(String FromUserName,String ToUserName,int createTime,String content_id)
    {

        StringBuffer sb = new StringBuffer(1024);
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA["+FromUserName+"]]></ToUserName>");
        sb.append("<FromUserName><![CDATA["+ToUserName+"]]></FromUserName>");
        sb.append("<CreateTime>"+createTime+"</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA[" + content_id + "]]></Content>");
        sb.append("</xml>");
        String replyMsg = sb.toString();
        logger.info("确定发送的XML为：" + replyMsg);//千万别加密

        return replyMsg;
    }

    /**
     *外部调用推送威富通广告
     * @param openid 微信用户openid
     * @param appid 微信appid
     * @param attach 支付备注
     * @param total_fee 支付金额
     * @param transaction_id 交易单号
     * @param dateStr_new 交易时间（yyyyMMddHHmmss）
     * @return
     * @throws JSONException
     */
    public String sendWeiFuTong(String openid,String appid,String attach,String total_fee,String transaction_id,
                                String dateStr_new) throws JSONException, IOException, ParseException {
        String getuserinfo = httpsPostMethod.sendHttpsPost(
                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + getAuthAccesstoken(appid) + "&openid=" + openid
                        + ""
                , "&lang=zh_CN",
                "获取获取openid");
        Date date=new Date();
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = oldFormat.parse(dateStr_new);
        dateStr_new= newFormat.format(date);
        String imgPath="";//广告地址
        String url="";//外链地址
        String desc="";//广告介绍
        String imgWeixinPath="";//微信端地址
        String advert = getAdvert(openid,appid,getuserinfo);
        JSONObject advert_json = new JSONObject(advert);

        if(advert_json.getBoolean("success"))
        {
            imgPath=advert_json.getString("imgPath");//广告地址
            url=advert_json.getString("url");//外链地址
            desc=advert_json.getString("desc");//广告介绍
            imgWeixinPath=advert_json.getString("imgWeixinPath");//微信端地址

            //保存威富通回传的外链地址及广告地址
            Conjunction conj = Restrictions.conjunction();
            conj.add(Restrictions.eq("advertOrder_picurl", imgPath));
            List<AdvertOrder> advertOrders=advertOrderDaoimpl.listByCriteria(AdvertOrder.class,conj,true);
            List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, appid, false);
            AdvertOrder advertOrder;
            if(advertOrders != null && advertOrders.size() > 0)
            {
                advertOrder=advertOrders.get(0);
            }
            else
            {
                advertOrder=new AdvertOrder();
            }
            //advertOrder.setAdvertOrder_num(transaction_id);
            advertOrder.setAdvertOrder_picurl(imgPath);
            advertOrder.setAdvertOrder_shopid(weiXinAuthorizationList.get(0).getShop_id() == null ? ""
                    : weiXinAuthorizationList.get(0).getShop_id());
            advertOrder.setAdvertOrder_shopname(attach);
            advertOrder.setAdvertOrder_goodsIntroduction(desc);
            advertOrder.setAdvertOrder_url(url);
            advertOrder.setAdvertOrder_imgWeixinPath(imgWeixinPath);
            advertOrderDaoimpl.insert(advertOrder);

            JSONObject userinfo_json=new JSONObject(getuserinfo);
            String sex="1";
            String nickname="";
            try
            {
                sex=userinfo_json.get("sex").toString();
                nickname=userinfo_json.getString("nickname");
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            //保存用户订单信息及广告链接地址
            AdvertUser advertUser=new AdvertUser();
            advertUser.setAdvertOrder_picurl(imgPath);
            advertUser.setAdvertUser_sex(sex);
            advertUser.setAdvertUser_openid(openid);
            advertUser.setAdvertUser_name(nickname);
            advertUser.setAdvertUser_is_click(2);
            advertUser.setAdvertUser_cash(Double.parseDouble(total_fee));
            advertUser.setAdvertOrder_appid(appid);
            advertUser.setAdvertOrder_imgWeixinPath(imgWeixinPath);
            advertUser.setAdvertOrder_num(transaction_id);
            advertUser.setAdvertOrder_url(url);
            advertUser.setSendTime(new Date());
            advertOrderDaoimpl.insert(advertUser);
        }
        else
        {
            imgWeixinPath="http://www.pandaonline.com.cn/image/20170406155337.jpg";
            url="http://www.pandaonline.com.cn/2017_cms/login.asp?qd=41";
        }
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        if(retStrFormatNowDate.compareTo("2017-06-17 00:00:00")<0)
        {
            imgWeixinPath="https://advert.swiftpass.cn/pic/adpic/adbody/2017/06/12/542d261d-6846-433b-9684-511aa37b519d.jpg";
            url="http://adfarm.mediaplex.com/ad/ck/10592-210881-62740-0";
        }
        String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAuthAccesstoken(appid),
                creatNews(openid, transaction_id, attach, total_fee, dateStr_new, imgWeixinPath, url), "");
        getResponse().getWriter().write("");
        return "success";
    }
    /**
     * 签名验证方法
     * @param
     */
   /* public static void main(String [] args)
    {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("appid","wxa2b490a571ba7962");
        hashMap.put("attach","支付测试");
        hashMap.put("bank_type","CFT");
        hashMap.put("cash_fee","1");
        hashMap.put("device_info","WEB");
        hashMap.put("fee_type","CNY");
        hashMap.put("is_subscribe","Y");
        hashMap.put("mch_id","1250879501");
        hashMap.put("nonce_str","R3skPw6Je2cf1xLm");
        hashMap.put("openid","oSUlnuMXqmVGC6k9TQhrGYmw5MoU");
        hashMap.put("out_trade_no","1462417917229");
        hashMap.put("result_code","SUCCESS");
        hashMap.put("return_code","SUCCESS");
        hashMap.put("time_end","20160505111232");
        hashMap.put("total_fee","1");
        hashMap.put("trade_type","JSAPI");
        hashMap.put("transaction_id","4003082001201605055525317704");

        String aa=commenUtil.getSign("fe528a709478634bb317652581143afd",hashMap,"utf-8");
        System.out.println("签名----------------"+aa);
    }*/

    public String getAppIdByShopId(String shopid)
    {
        logger.info("进入获取商户appid------");
        JSONObject jo_Advert = new JSONObject();
        JSONArray newsArray = new JSONArray();
        List<WeiXinAuthorization> weiXinAuthorizationList= gabadaoimpl.listByCriteria(WeiXinAuthorization.class, shopid, false);
        try {
            for(WeiXinAuthorization wa:weiXinAuthorizationList)
            {
                JSONObject jo = new JSONObject();
                jo.put("appid",wa.getWeixin_Authorizer_Appid());
                jo.put("secret",wa.getNum_secret());
                jo.put("muchid",wa.getMuch_id());
                jo.put("shopid",wa.getShop_id());
                newsArray.add(jo);
            }
            jo_Advert.put("lists", newsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo_Advert.toString();
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getMarked() {
        return marked;
    }

    public void setMarked(String marked) {
        this.marked = marked;
    }
    public getAuthorizeByAuthappidDaoimpl getGabadaoimpl() {
        return gabadaoimpl;
    }

    public void setGabadaoimpl(getAuthorizeByAuthappidDaoimpl gabadaoimpl) {
        this.gabadaoimpl = gabadaoimpl;
    }

    public orderInfoDaoimpl getInfoDaoimpl() {
        return infoDaoimpl;
    }

    public void setInfoDaoimpl(orderInfoDaoimpl infoDaoimpl) {
        this.infoDaoimpl = infoDaoimpl;
    }

    public String getPay_notify_url() {
        return pay_notify_url;
    }

    public void setPay_notify_url(String pay_notify_url) {
        this.pay_notify_url = pay_notify_url;
    }

    public getWeiXinTuiguangByWXTDDaoimpl getGetwxtgimpl() {
        return getwxtgimpl;
    }

    public void setGetwxtgimpl(getWeiXinTuiguangByWXTDDaoimpl getwxtgimpl) {
        this.getwxtgimpl = getwxtgimpl;
    }

    public String getCardCherkReturn_url() {
        return cardCherkReturn_url;
    }

    public void setCardCherkReturn_url(String cardCherkReturn_url) {
        this.cardCherkReturn_url = cardCherkReturn_url;
    }

    public String getSotreCheckReturn_url() {
        return sotreCheckReturn_url;
    }

    public void setSotreCheckReturn_url(String sotreCheckReturn_url) {
        this.sotreCheckReturn_url = sotreCheckReturn_url;
    }

    public String getApayUrl() {
        return apayUrl;
    }

    public void setApayUrl(String apayUrl) {
        this.apayUrl = apayUrl;
    }

    public String getAsyncPayResult_url() {
        return asyncPayResult_url;
    }

    public void setAsyncPayResult_url(String asyncPayResult_url) {
        this.asyncPayResult_url = asyncPayResult_url;
    }

    public AutoMessageDaoImpl getAutoMessageDaoimpl() {
        return autoMessageDaoimpl;
    }

    public void setAutoMessageDaoimpl(AutoMessageDaoImpl autoMessageDaoimpl) {
        this.autoMessageDaoimpl = autoMessageDaoimpl;
    }

    public AdvertOrderDaoImpl getAdvertOrderDaoimpl() {
        return advertOrderDaoimpl;
    }

    public void setAdvertOrderDaoimpl(AdvertOrderDaoImpl advertOrderDaoimpl) {
        this.advertOrderDaoimpl = advertOrderDaoimpl;
    }

    public String getTransitUrl() {
        return transitUrl;
    }

    public void setTransitUrl(String transitUrl) {
        this.transitUrl = transitUrl;
    }
}

