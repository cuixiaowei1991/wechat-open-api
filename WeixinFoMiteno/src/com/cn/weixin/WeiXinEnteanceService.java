package com.cn.weixin;

import com.cn.struts2.BaseAction;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 微信全部接口入口
 * Created by cuixiaowei on 2016/7/5.
 */
public class WeiXinEnteanceService extends BaseAction {
    private static Logger logger = Logger
            .getLogger(WeiXinEnteanceService.class);
    private String jsonStr;
    private String marked;
    private ChangeFunction cfunction;


    /**
     * 微信相关接口入口
     */

    public void interfaceForWeiXin()
    {
        logger.debug("------marked--------" + marked);
        logger.debug("------jsonStr--------" + jsonStr);
        try {
            if(jsonStr.equals( new String(jsonStr.getBytes("iso-8859-1"), "iso-8859-1")))
            {
                jsonStr = new String(jsonStr.getBytes("iso-8859-1"), "UTF-8");
                logger.debug("WeiXinEntranceService------转码之后——————————jsonStr:" + jsonStr);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == marked) {
            try {
                JSONObject returnJSON = new JSONObject();
                returnJSON.put("rspCode", "41011");
                returnJSON.put("rspDesc", "必填字段不完整或不合法，参考相应接口。");
                getResponse().getWriter().append(returnJSON.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            //上传图片及微信门店创建
            if ("CreateStroe".equals(marked)) {
                cfunction.createStoreInfo(jsonStr);
                return;
            }
            //上传logo及卡券创建
            if ("CreateCoupon".equals(marked)) {
                cfunction.createCouponInfo(jsonStr);
                return;
            }
            //微信红包
            if ("RedPackets".equals(marked)) {
                cfunction.sendRedpackets(jsonStr);
                return;
            }
            //查询自定义菜单
            if ("getMenu".equals(marked)) {
                cfunction.getMenu(jsonStr);
                return;
            }
            //创建自定义菜单
            if ("creatMenu".equals(marked)) {
                cfunction.creatMenu(jsonStr);
                return;
            }
            //获取自定义菜单配置接口（如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置）
            if ("getMenuPeiZhi".equals(marked)) {
                cfunction.getMenuPeiZhi(jsonStr);
                return;
            }
            //删除自定义菜单
            if ("deleteMenu".equals(marked)) {
                cfunction.deleteMenu(jsonStr);
                return;
            }
            //根据门店poid查询门店信息
            if ("getMenDianBypoi".equals(marked)) {
                cfunction.getMenDianBypoi(jsonStr);
                return;
            }
            //查询门店列表信息
            if ("getMenDianList".equals(marked)) {
                cfunction.getMenDianList(jsonStr);
                return;
            }
            //查询卡券详情
            if ("getCardByCardId".equals(marked)) {
                cfunction.getCardByCardId(jsonStr);
                return;
            }
            //创建会员卡
            if ("CreateMemberCard".equals(marked)) {
                cfunction.getCardByCardId(jsonStr);
                return;
            }
            //上传永久素材
            if ("foreverImage".equals(marked)) {
                cfunction.addForverImage(jsonStr);
                return;
            }
            //获取永久素材列表
            if ("getMaterialList".equals(marked)) {
                cfunction.getMaterial(jsonStr);
                return;
            }
            //微信主动发送消息
            if ("sendWinXinMsg".equals(marked)) {
                cfunction.sendWeiXinMsg(jsonStr);
                return;
            }
            //发送威富通广告
            if ("sendWeiFuTongMsg".equals(marked)) {
                cfunction.sendWeiFuTongMsg(jsonStr);
                return;
            }
            //获取威富通广告返回到前端页面
            if ("returnWeiFuTongAder".equals(marked)) {
                cfunction.returnWeiFuTongAder(jsonStr);
                return;
            }
        }
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

    public ChangeFunction getCfunction() {
        return cfunction;
    }

    public void setCfunction(ChangeFunction cfunction) {
        this.cfunction = cfunction;
    }
}
