package com.cn.weixin.common;

import com.cn.weixin.protocol.commenUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by cuixiaowei on 2016/5/5.
 */
public class WeiXinDecode {
    private static Logger logger = Logger
            .getLogger(WeiXinDecode.class);

    /**
     * 对微信推送的加密数据进行解密，同时将解密后的xml转换为json格式
     * @param token 公众号消息校验Token
     * @param key 公众号配置key
     * @param app_id 第三方平台appid
     * @param msg_signature 签名串
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param postStr post过来的加密数据串
     * @return
     */
    public static String decode(String token,String key,String app_id,String msg_signature,String timestamp,String nonce,String postStr)
    {
        String xml_miwen=postStr;


        /**ticket解密**/
        System.out.println("加密后: " + xml_miwen);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        StringReader sr = new StringReader(xml_miwen);
        InputSource is = new InputSource(sr);
        Document document = null;
        try {
            document = db.parse(is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");
        String encrypt = nodelist1.item(0).getTextContent();
        //String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String fromXML = String.format(format, encrypt);
        //
        // 公众平台发送消息给第三方，第三方处理
        //
        String xml="";
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, key, app_id);
           /* xml= pc.DecryptMsg(getRequest().getParameter("msg_signature"), getRequest().getParameter("timestamp"),
                      getRequest().getParameter("nonce"),xml_miwen);*/
            // 第三方收到公众号平台发送的消息
            xml = pc.decryptMsg(msg_signature, timestamp, nonce, fromXML);

        } catch (AesException e) {
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
            logger.debug("转换格式错误！");
        }

        String result_json= commenUtil.xml2JSON(xml);
        logger.debug("xml-------->json:" + result_json);
        return result_json;
    }
}
