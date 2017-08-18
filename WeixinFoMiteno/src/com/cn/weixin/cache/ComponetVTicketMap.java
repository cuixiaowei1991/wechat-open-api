package com.cn.weixin.cache;

import java.util.TreeMap;

/**
 * Created by cuixiaowei on 2016/4/22.
 */
public class ComponetVTicketMap
{
    private static TreeMap<String, ComponentVerifyTicket> verifyTicketList = new TreeMap<String, ComponentVerifyTicket>();


    public static void put(String appid,ComponentVerifyTicket cvt){
        verifyTicketList.put(appid, cvt);
    }

    public static ComponentVerifyTicket get(String appid) {

        try {
            return verifyTicketList.get(appid);
        } catch (NullPointerException e) {
            // TODO: handle exception
            return null;
        }
    }
}
