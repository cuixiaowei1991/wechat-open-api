package com.cn.weixin;

import com.cn.dao.daoimpl.getAuthorizeByAuthappidDaoimpl;
import com.cn.entity.WeiXinAuthorization;
import com.cn.struts2.BaseAction;
import com.cn.weixin.cache.ComponetVTicketMap;
import com.cn.weixin.protocol.httpsPostMethod;

import java.util.List;

/**
 * 根据授权码获取商户公众号授权token
 * Created by cuixiaowei on 2016/4/27.
 */
public class toRegisterAction extends BaseAction
{
    private String app_id;//第三方平台appid
    private getAuthorizeByAuthappidDaoimpl gabadaoimpl;


    /**
     * 根据授权码和第三方平台appid换取公众号的接口调用凭据和授权信息
     * @return
     *//*
    public String getAuthAccesstoken()
    {
        thirdPartyPlatformFunction tf=new thirdPartyPlatformFunction();
        String authaccess_appid=getRequest().getParameter("appid");//授权方的appid
        List<WeiXinAuthorization> walist= gabadaoimpl.listByCriteria(WeiXinAuthorization.class,authaccess_appid,true);
        String auth_code="";//授权码
        if(walist==null || walist.size()<=0)
        {//数据库中没有该商户的appid 直接跳到授权

            tf.entranceWinXin();
            return "授权失败！";
        }
        else
        {
            if(Long.parseLong(walist.get(0).getAuthorizationCodeExpiredTime())<System.currentTimeMillis()/1000-300)
            {//授权码过期，重新获取授权码
                tf.entranceWinXin();
                return "授权失败！";
            }
            if(walist.get(0).getAuthorization_zhuangtai()==null || (walist.get(0).getAuthorization_zhuangtai().equals("2")))
            {//已取消授权，重新获取授权码
                tf.entranceWinXin();
                return "已取消授权，重新获取授权码！";
            }
            auth_code=walist.get(0).getWeixin_Authorizer_code();
            String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+getComponentAccessToken(),
                    "{\"component_appid\":\""+app_id+"\"}","获取预授权码pre_auth_code");
            logger.debug("获取第三方平台token微信返回-----------》"+responseStr);
        }
        return "";
    }*/

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public getAuthorizeByAuthappidDaoimpl getGabadaoimpl() {
        return gabadaoimpl;
    }

    public void setGabadaoimpl(getAuthorizeByAuthappidDaoimpl gabadaoimpl) {
        this.gabadaoimpl = gabadaoimpl;
    }
}
