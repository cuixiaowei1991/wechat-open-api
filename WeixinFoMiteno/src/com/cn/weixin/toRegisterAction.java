package com.cn.weixin;

import com.cn.dao.daoimpl.getAuthorizeByAuthappidDaoimpl;
import com.cn.entity.WeiXinAuthorization;
import com.cn.struts2.BaseAction;
import com.cn.weixin.cache.ComponetVTicketMap;
import com.cn.weixin.protocol.httpsPostMethod;

import java.util.List;

/**
 * ������Ȩ���ȡ�̻����ں���Ȩtoken
 * Created by cuixiaowei on 2016/4/27.
 */
public class toRegisterAction extends BaseAction
{
    private String app_id;//������ƽ̨appid
    private getAuthorizeByAuthappidDaoimpl gabadaoimpl;


    /**
     * ������Ȩ��͵�����ƽ̨appid��ȡ���ںŵĽӿڵ���ƾ�ݺ���Ȩ��Ϣ
     * @return
     *//*
    public String getAuthAccesstoken()
    {
        thirdPartyPlatformFunction tf=new thirdPartyPlatformFunction();
        String authaccess_appid=getRequest().getParameter("appid");//��Ȩ����appid
        List<WeiXinAuthorization> walist= gabadaoimpl.listByCriteria(WeiXinAuthorization.class,authaccess_appid,true);
        String auth_code="";//��Ȩ��
        if(walist==null || walist.size()<=0)
        {//���ݿ���û�и��̻���appid ֱ��������Ȩ

            tf.entranceWinXin();
            return "��Ȩʧ�ܣ�";
        }
        else
        {
            if(Long.parseLong(walist.get(0).getAuthorizationCodeExpiredTime())<System.currentTimeMillis()/1000-300)
            {//��Ȩ����ڣ����»�ȡ��Ȩ��
                tf.entranceWinXin();
                return "��Ȩʧ�ܣ�";
            }
            if(walist.get(0).getAuthorization_zhuangtai()==null || (walist.get(0).getAuthorization_zhuangtai().equals("2")))
            {//��ȡ����Ȩ�����»�ȡ��Ȩ��
                tf.entranceWinXin();
                return "��ȡ����Ȩ�����»�ȡ��Ȩ�룡";
            }
            auth_code=walist.get(0).getWeixin_Authorizer_code();
            String responseStr= httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="+getComponentAccessToken(),
                    "{\"component_appid\":\""+app_id+"\"}","��ȡԤ��Ȩ��pre_auth_code");
            logger.debug("��ȡ������ƽ̨token΢�ŷ���-----------��"+responseStr);
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
