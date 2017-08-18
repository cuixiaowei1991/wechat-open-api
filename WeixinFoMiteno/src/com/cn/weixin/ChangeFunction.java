package com.cn.weixin;

import com.cn.struts2.BaseAction;
import com.cn.weixin.protocol.commenUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.cn.weixin.protocol.commenUtil.gbEncoding;


/**
 * Created by cuixiaowei on 2016/6/21.
 */
public class ChangeFunction extends BaseAction {

    private static Logger logger = Logger
            .getLogger(ChangeFunction.class);
    /*private String jsonStr;
    private String marked;*/

    private ForWeiXin forWeixin;

    public ForWeiXin getForWeixin() {
        return forWeixin;
    }

    public void setForWeixin(ForWeiXin forWeixin) {
        this.forWeixin = forWeixin;
    }

    /*public String getJsonStr() {
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
    }*/

    //微信门店创建
    public void createStoreInfo(String jsonStr) {

        logger.debug("创建门店传入参数:" + jsonStr);
        //String callback = this.getRequest().getParameter("callbackparam");
        JSONObject returnJSON = new JSONObject();
        String result = "";
        try {
            JSONObject storeJSONO = new JSONObject(jsonStr);
            //String photoList = storeJSONO.getString("photoList");
            JSONArray photoList_array = storeJSONO.getJSONArray("photoList");
            JSONArray jsonArray=new JSONArray();

            if (!storeJSONO.isNull("shopId")) {
                for(int i=0;i<photoList_array.length();i++)
                {
                    JSONObject jo = photoList_array.getJSONObject(i);
                    JSONObject object=new JSONObject();
                    String uploadimg=forWeixin.uploadMedia(storeJSONO.getString("appid"),
                            storeJSONO.getString("shopId"), jo.getString("photo_url"), "门店上传图片",
                            "forever");
                    if(uploadimg.contains("url") && !uploadimg.contains("errmsg"))
                    {
                        JSONObject urlJSON = new JSONObject(uploadimg);

                        object.put("photo_url",urlJSON.getString("url"));
                    }
                    jsonArray.put(object);
                }


                /*String str_return = forWeixin.uploadMedia(storeJSONO.getString("appid"),
                        storeJSONO.getString("shopId"), photoList, "门店上传图片",
                        "forever");*/

                //JSONObject urlJSON = new JSONObject(str_return);
                //if(!urlJSON.isNull("url")){
                   // String photo_url = urlJSON.getString("url");
                    returnJSON = forWeixin.createTheStore(storeJSONO, jsonArray);
                    returnJSON.put("photo_url", jsonArray);
              //  }else{
                 //   returnJSON=urlJSON;
              //  }

            } else {
                returnJSON.put("errcode", 001);
                returnJSON.put("errmsg", "创建门店失败");
            }

        } catch (JSONException e) {
            logger.debug("创建门店拼返回值" + e.getMessage());
            result = "{\"errcode\":002,\"errmsg\":\"创建门店失败\"}";
            e.printStackTrace();
        }
        try {
            //result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            if (result.length() == 0) {
                getResponse().getWriter().append(returnJSON.toString());
            } else {
                getResponse().getWriter().append(result);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *
     * 功能：创建票券逻辑方法 说明：在微信创建门店，供内部方法逻辑调用 参数：jsonStr 在微信创建门店需要的参数集合（详见接口文档）
     */
    public void createCouponInfo(String jsonStr) {
        logger.debug("创建票券传入参数:" + jsonStr);
        //String callback = this.getRequest().getParameter("callbackparam");
        String result = "";
        JSONObject returnJSON = new JSONObject();
        try {
            JSONObject couponInfo = new JSONObject(jsonStr);
            String str_return = forWeixin.uploadMedia(couponInfo.getString("appid"),
                    couponInfo.getString("sid"),
                    couponInfo.getString("logo_url"), "票券上传图片", "forever");
            JSONObject urlJSON = new JSONObject(str_return);
            if(!urlJSON.isNull("url")){
                String photo_url = urlJSON.getString("url");

                String rStr = forWeixin.CreateCard(couponInfo, photo_url);
                if (!"error".equals(rStr)) {
                    returnJSON = new JSONObject(rStr);
                    returnJSON.put("photo_url", photo_url);
                } else {
                    returnJSON.put("errcode", 002);
                    returnJSON.put("errmsg", "创建票券失败");
                    returnJSON.put("photo_url", photo_url);
                }

            }else{
                returnJSON=urlJSON;
            }
            result = returnJSON.toString();
        } catch (JSONException e) {
            logger.debug("创建票券" + e.getMessage());
            result = "{\"errcode\":\"003\",\"errmsg\":\""
                    + gbEncoding(e.getMessage()) + "\"}";
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            result = "{\"errcode\":\"003\",\"errmsg\":\""
                    + gbEncoding(e.getMessage()) + "\"}";
            e.printStackTrace();
        }
        try {
            //result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 发红包
     * @param jsonStr
     */
    public void sendRedpackets(String jsonStr)
    {
        logger.debug("微信红包前台传的参数:" + jsonStr);
        //String orderNNo =  MoneyUtils.getOrderNo() ;
        Map<String, String> map = new HashMap<>();
        //map.put("nonce_str", MoneyUtils.buildRandom());//随机字符串
        map.put("mch_billno", "");//商户订单
        map.put("mch_id", "XXX");//商户号
        map.put("wxappid", "XXX");//商户appid
       /* map.put("nick_name", "自由数字科技");//提供方名称*/
        map.put("send_name", "自由数字科技");//用户名
        map.put("re_openid", "");//用户openid
        map.put("total_amount", "1");//付款金额
       /* map.put("min_value", "1");//最小红包
        map.put("max_value", "1");//最大红包*/
        map.put("total_num", "1");//红包发送总人数
        map.put("wishing", "新年快乐");//红包祝福语
        map.put("client_ip", "127.0.0.1");//ip地址
        map.put("act_name", "过年红包");//活动名称
        map.put("remark", "新年新气象");//备注
        map.put("sign", commenUtil.getSign("", map, "utf-8"));//签名

        String result = "";
        try {
            result = forWeixin.doSendMoney("", commenUtil.createXML(map));
            logger.debug("微信返回的发送红包数据--------------------------》"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result:" + result);
    }

    /**
     * 查询自定义菜单
     * @param jsonStr
     */
    public void getMenu(String jsonStr)
    {
        String result= forWeixin.getMenu(jsonStr);
        logger.debug("查询自定义菜单返回结果--------》" + result);
        String callback = this.getRequest().getParameter("callbackparam");
        try
        {
            result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取自定义菜单配置接口（如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置）
     * @param jsonStr
     */
    public void getMenuPeiZhi(String jsonStr)
    {
        String result= forWeixin.getPeiZhiMenu(jsonStr);
        logger.debug("查询自定义菜单配置接口返回结果--------》" + result);
        String callback = this.getRequest().getParameter("callbackparam");
        try
        {
            result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建自定义菜单
     */
    public void creatMenu(String jsonStr)
    {
        try
        {
            String callback = this.getRequest().getParameter("callbackparam");
            JSONObject info = new JSONObject(jsonStr);
            String appid=info.getString("appid");
            JSONObject menuinfo=info.getJSONObject("menuinfo");
            String deleteResponse=forWeixin.deleteMenu(appid);
            logger.debug("删除自定义菜单返回-----》"+deleteResponse);
            JSONObject deleteinfo = new JSONObject(deleteResponse);
            String result="";
            if(deleteinfo.getString("errmsg")!=null && "ok".equalsIgnoreCase(deleteinfo.getString("errmsg")))
            {
                result= forWeixin.createMenu(appid, menuinfo.toString());
            }
            else
            {
                result="{\"error\":\"创建之前删除失败！\"}";
            }

            logger.debug("查询自定义菜单配置接口返回结果--------》" + result);
            result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除自定义菜单
     * @param jsonStr
     */
    public void deleteMenu(String jsonStr)
    {
        try {
            String callback = this.getRequest().getParameter("callbackparam");
            String result = forWeixin.deleteMenu(jsonStr);
            logger.debug("删除自定义菜单--------》" + result);
            result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据poi查询门店信息
     * @param jsonStr
     */
    public void getMenDianBypoi(String jsonStr)
    {
        String result = forWeixin.getpoi(jsonStr);
        logger.debug("根据poid查询门店信息--------》"+result);
        try {

            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
            } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     *查询微信门店列表
     */
    public void getMenDianList(String jsonStr)
    {
        String result = forWeixin.getpoilist(jsonStr);
        logger.debug("微信门店列表--------》"+result);
        try {

            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 查询卡券详情
     * @param jsonStr
     */
    public void getCardByCardId(String jsonStr)
    {
        String result = forWeixin.cardGet(jsonStr);
        logger.debug("卡券详情--------》"+result);
        try {

            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 创建会员卡
     * @param jsonStr
     */
    public void CreateMemberCard(String jsonStr)
    {
        logger.debug("###########进入创建会员卡接口##########");
        String result = "";
        JSONObject returnJSON = new JSONObject();
        try {
            JSONObject couponInfo = new JSONObject(jsonStr);
            String str_return = forWeixin.uploadMedia(couponInfo.getString("appid"),
                    couponInfo.getString("sid"),
                    couponInfo.getString("logo_url"), "票券上传图片", "forever");
            JSONObject urlJSON = new JSONObject(str_return);
            if(!urlJSON.isNull("url")){
                String photo_url = urlJSON.getString("url");
                String rStr = forWeixin.CreateCard(couponInfo, photo_url);
                if (!"error".equals(rStr)) {
                    returnJSON = new JSONObject(rStr);
                    returnJSON.put("photo_url", photo_url);
                } else {
                    returnJSON.put("errcode", 002);
                    returnJSON.put("errmsg", "创建票券失败");
                    returnJSON.put("photo_url", photo_url);
                }

            }else{
                returnJSON=urlJSON;
            }
            result = returnJSON.toString();
        } catch (JSONException e) {
            logger.debug("创建票券" + e.getMessage());
            result = "{\"errcode\":\"003\",\"errmsg\":\""
                    + gbEncoding(e.getMessage()) + "\"}";
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            result = "{\"errcode\":\"003\",\"errmsg\":\""
                    + gbEncoding(e.getMessage()) + "\"}";
            e.printStackTrace();
        }
        try {
            //result = callback + "(" + result + ")";
            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addForverImage(String jsonStr)
    {
        logger.debug("###########进入上传永久素材##########"+jsonStr);
        String callback = this.getRequest().getParameter("callbackparam");
        JSONObject couponInfo = null;
        try {
            couponInfo = new JSONObject(jsonStr);
            String str_return = forWeixin.uploadMedia(couponInfo.getString("appid"),
                    "", couponInfo.getString("logo_url"), "上传永久素材", "foreverImage");
            logger.debug("上传永久素材--------》"+str_return);

            str_return = callback + "(" + str_return + ")";
            getResponse().setCharacterEncoding("UTF-8");

            getResponse().getWriter().append(str_return);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取永久素材列表
     * @param jsonStr
     */
    public void getMaterial(String jsonStr)
    {
        logger.debug("###########进入获取永久素材列表##########"+jsonStr);
        String callback = this.getRequest().getParameter("callbackparam");
        try {

            String str_count=forWeixin.getMediaListCount(jsonStr);
            String str_return = forWeixin.batchget_material(jsonStr);
            logger.debug("获取永久素材列表--------》" + str_return);
            JSONObject returnJSON = new JSONObject();
            returnJSON.put("list",str_return);
            returnJSON.put("cout", str_count);
            str_return = callback + "(" + returnJSON.toString() + ")";
            getResponse().setCharacterEncoding("UTF-8");

            getResponse().getWriter().append(str_return);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主动推送消息给微信用户
     * @param jsonStr
     */
    public void sendWeiXinMsg(String jsonStr)
    {

        logger.debug("###########主动推送消息给微信用户##########"+jsonStr);
        String str_count=forWeixin.sendWeiXinMsg(jsonStr);
        logger.debug("主动推送微信文本消息"+str_count);
        try {

            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(str_count);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendWeiFuTongMsg(String jsonStr)
    {
        logger.debug("###########威富通广告发送##########"+jsonStr);

        String str_count=forWeixin.sendWeiFuTong(jsonStr);
        logger.debug("威富通广告发送:"+str_count);
        try {

            getResponse().setCharacterEncoding("UTF-8");
            getResponse().getWriter().append(str_count);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void returnWeiFuTongAder(String jsonStr)
    {
        logger.debug("###########返回威富通广告图片到前端页面##########"+jsonStr);
        String callback = this.getRequest().getParameter("callbackparam");
        String str_count=forWeixin.returnWeiFuTongAder(jsonStr);
        logger.debug("威富通广告图片回传到前端页面:"+str_count);
        try {
            String result = callback + "(" + str_count + ")";
            this.getResponse().setCharacterEncoding("UTF-8");
            this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
            getResponse().getWriter().append(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
