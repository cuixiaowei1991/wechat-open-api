package com.cn.weixin;

import com.cn.dao.daoimpl.lotteryTicketDaoimpl;
import com.cn.entity.Lottery_Ticket;
import com.cn.struts2.BaseAction;
import com.cn.weixin.protocol.commenUtil;
import com.cn.weixin.protocol.httpsPostMethod;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

/**
 * Created by cuixiaowei on 2016/5/30.
 */
public class lotteryTicketFunction  extends BaseAction {
    private static Logger logger = Logger
            .getLogger(lotteryTicketFunction.class);
    private lotteryTicketDaoimpl ltDaoimpl;
    /**
     * 彩票下注
     */
    public void lotteryTicketEntrance() throws IOException, JSONException {
        logger.debug("进入彩票下注");
        String callback = this.getRequest().getParameter("callbackparam");
        String jsonStr1= this.getRequest().getParameter("jsonStr");
        logger.debug("*****************下注前台传的jsonStr***************"+jsonStr1);
        JSONObject requestJSON = new JSONObject();
        JSONObject json= null;
        String shop_appid="";//商户appid
        String openid="";
        String openid_appid="";
        String token_caipiao="";
        String order_id="";
        JSONObject json_return = new JSONObject();
        try {
            json = new JSONObject(jsonStr1);
            shop_appid=json.getString("app_id");//wxa2b490a571ba7962
            openid=json.getString("openid");//oSUlnuMXqmVGC6k9TQhrGYmw5MoU
            //betn=json.getString("betn");//1
            token_caipiao=json.getString("token");
            order_id=json.getString("order_id");
            logger.debug("前台页面回传的openid-------------》"+openid+"----商户appid---"+shop_appid+"-------充值token----"+token_caipiao+"---------订单id（我们自己的-------"+order_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e)
        {

            e.printStackTrace();
        }
        openid_appid=openid+"--"+shop_appid+"--"+order_id;

        //测试数据
        //String openid="oSUlnuHUAkHGcIIGODAu0UkZ37RE-wxa2b490a571ba7962";
        String betn="5";
        //////////////每次点击领取彩票先查该张优惠券里边是否有token有的话 取原来，没有 随机生成
        //String token_caipiao=commenUtil.getRandomString(12);

        logger.debug("生成的支付token----------------------》"+token_caipiao);


        String rawstring = "betn=" + betn
                + "&key=" + "01818EFD0816BAD5" + "&openid=" + openid_appid + "&token="
                + token_caipiao;
        String signature = commenUtil.getSHA1(rawstring);

        String url="http://weixin.ipaika.com/authMerchant/ticket?openid="+openid_appid+"&token="+token_caipiao
                +"&betn="+betn+"&verify="+signature;
        logger.debug("===================================彩票下注url====================================" + url);
        json_return.put("rspCode", "000");
        json_return.put("rspDesc","成功");
        json_return.put("url",url);
        String result = callback + "(" +json_return.toString() + ")";
        this.getResponse().setCharacterEncoding("UTF-8");
        this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
        logger.debug("H通过openid获取用户信息userinfo--response数据为：---" + result);
        try {
            this.getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.getResponse().sendRedirect(url);



    }

    /**
     * 彩票事件消息回调
     */
    public void lotteryTicket_return() throws ParseException {

        logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@2进入彩票事件消息回调接口@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        logger.debug("接受的彩票事件消息回调路径地址---------------》" + getRequest().getRequestURL());
//        String aa= getRequest().getParameter("appid");

        String signature=this.getRequest().getParameter("verify");//签名
        logger.debug("@@@@@@@@@@@@@@@@@@@@@签名串@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + signature);

        BufferedReader br;
        String postStr = null;
        try {

            br = new BufferedReader(new InputStreamReader(getRequest().getInputStream(),"utf-8"));
            String buffer = null;
            StringBuffer buff = new StringBuffer();
            while ((buffer = br.readLine()) != null) {
                buff.append(buffer + "\n");
            }
            br.close();
            postStr = buff.toString();
            logger.debug("接收-----------------------彩票回调-------------post发送数据:\n" + postStr);
            if (postStr.equals(new String(postStr.getBytes("iso-8859-1"), "iso-8859-1"))) {
                postStr = new String(postStr.getBytes("iso-8859-1"), "UTF-8");
                logger.debug("彩票回调------转码之后(iso-8859-1--->utf-8)——————————jsonStr:" + postStr);
            }

            JSONObject json = new JSONObject(postStr);
            String event_type=json.getString("event_type");//事件类型

            if("VoteSuccess".equals(event_type))
            {//彩票下注成功返回事件
                logger.debug("====================进入彩票下注数据处理=================");
                Long order_id=json.getLong("order_id");//订单号
                String open_id_appid=json.getString("open_id");
                String[] a= open_id_appid.split("--");
                String lottery_name=json.getString("lottery_name");//活动名称
                String pass_type=json.getString("pass_type");//玩法

                Double amt=0.0;
                try
                {
                    amt=json.getDouble("amt");//投注金额
                }catch (Exception e)
                {

                }
                Double est_prz=0.0;//预计奖金
                try
                {
                    est_prz=json.getDouble("est_prz");//预计奖金
                }catch (Exception e)
                {

                }
                String order_time=json.getString("order_time");//下单时间
                String open_id=json.getString("open_id");
                Double prz_amt=0.0;
                try
                {
                    prz_amt=json.getDouble("prz_amt");//实际奖金
                }catch (Exception e)
                {
                    //e.printStackTrace();
                }
                JSONArray games=json.getJSONArray("games");
                SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                Date date =  formatter.parse(order_time);
                Lottery_Ticket lt=new Lottery_Ticket();
                lt.setLOTTERY_SERIAL_NUM(String.valueOf(order_id));
                lt.setLOTTERY_CONTENT(games.toString());
                lt.setLOTTERY_STARTTIME(date);
                lt.setLOTTERY_ISLOTT(2);
                lt.setLOTTERY_ISWIN(0);
                lt.setLOTTERY_BET_PRICE(String.valueOf(amt));
                lt.setLOTTERY_BET_TIME(date);
                lt.setLOTTERY_ARETICKET(2);
                lt.setLOTTERY_WETCHAT_OPENID(a[0]);
                lt.setLOTTERY_MULTIPLE((amt.intValue()) / 2);
                lt.setLOTTERY_WETCHAT_APPID(a[1]);
                lt.setLOTTERY_ORDER(a[2]);
                lt.setLOTTERY_CLEARANCE_TYPE(pass_type);
                if( ltDaoimpl.insert(lt).equals("success"))
                {
                    getResponse().getWriter().write("SUCCESS");
                }


            }
            if("BuyTicketWait".equals(event_type))
            {//出票中
                logger.debug("=======================进入出票中======================");
                String open_id_appid=json.getString("open_id");
                String[] a= open_id_appid.split("--");
                Long order_id=json.getLong("order_id");//订单号
                List<Lottery_Ticket> lottery_ticketList=ltDaoimpl.listByCriteria(Lottery_Ticket.class,String.valueOf(order_id),
                        a[0],true);
                logger.debug("彩票记录list长度%%%%%%%%%%%%%%%%%%%%%%%%"+lottery_ticketList.size());
                if(lottery_ticketList!=null && lottery_ticketList.size()>0)
                {

                    Lottery_Ticket lot= lottery_ticketList.get(0);
                    lot.setLOTTERY_ARETICKET(3);//出票中
                    if( ltDaoimpl.insert(lot).equals("success"))
                    {
                        getResponse().getWriter().write("SUCCESS");
                    }
                }
            }
            if("BuyTicketSuccess".equals(event_type))
            {//已出票
                logger.debug("=======================进入已出票======================");
                Long order_id=json.getLong("order_id");//订单号
                String open_id_appid=json.getString("open_id");
                String[] a= open_id_appid.split("--");
                List<Lottery_Ticket> lottery_ticketList=ltDaoimpl.listByCriteria(Lottery_Ticket.class, String.valueOf(order_id),
                        a[0], true);
                logger.debug("彩票记录list长度%%%%%%%%%%%%%%%%%%%%%%%%"+lottery_ticketList.size());
                if(lottery_ticketList!=null && lottery_ticketList.size()>0)
                {
                    Lottery_Ticket lot= lottery_ticketList.get(0);
                    lot.setLOTTERY_ARETICKET(1);//出票中
                    if( ltDaoimpl.insert(lot).equals("success"))
                    {
                        getResponse().getWriter().write("SUCCESS");
                    }
                }
            }
            if("MatchResult".equals(event_type))
            {//中奖信息
                logger.debug("=======================进入已开奖======================");

                JSONArray data=json.getJSONArray("data");
                for(int i=0;i<data.length();i++)
                {
                    JSONObject jo = data.getJSONObject(i);
                    Long order_id=jo.getLong("order_id");//订单号
                    List<Lottery_Ticket> lottery_ticketList=ltDaoimpl.listByCriteria(Lottery_Ticket.class, String.valueOf(order_id), true);
                    logger.debug("彩票记录list长度%%%%%%%%%%%%%%%%%%%%%%%%"+lottery_ticketList.size());
                    Lottery_Ticket lticket=lottery_ticketList.get(0);
                    lticket.setLOTTERY_ISWIN(1);
                    lticket.setLOTTERY_PRICE(String.valueOf(jo.getDouble("prz_amt")));
                    ltDaoimpl.insert(lticket).equals("success");

                }
                getResponse().getWriter().write("SUCCESS");

            }
            if("TokenSuccess".equals(event_type))
            {
                getResponse().getWriter().write("SUCCESS");
            }
            if("SendRedPackSuccess".equals(event_type))
            {
                Long order_id=json.getLong("order_id");//订单号
                List<Lottery_Ticket> lottery_ticketList=ltDaoimpl.listByCriteria(Lottery_Ticket.class, String.valueOf(order_id), true);
                logger.debug("彩票记录list长度%%%%%%%%%%%%%%%%%%%%%%%%"+lottery_ticketList.size());
                Lottery_Ticket lticket=lottery_ticketList.get(0);
                String cunzaiMsg=lticket.getLOTTERY_ACCPET_TYPE();
                String msg=json.getString("msg");
                if(cunzaiMsg==null || "".equals(cunzaiMsg))
                {
                    lticket.setLOTTERY_ACCPET_TYPE(msg);
                }
                else
                {
                    lticket.setLOTTERY_ACCPET_TYPE("-"+cunzaiMsg+"-"+msg);
                }


                ltDaoimpl.insert(lticket);
                getResponse().getWriter().write("SUCCESS");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e)
        {

        }
    }

    //发送领取红包和领取券接口
    public void redcash() throws JSONException {
        logger.debug("进入彩票下注");
        String callback = this.getRequest().getParameter("callbackparam");
        String jsonStr1= this.getRequest().getParameter("jsonStr");
        logger.debug("*****************点击领取红包前台传的jsonStr***************" + jsonStr1);
        JSONObject requestJSON = new JSONObject(jsonStr1);
        JSONObject json = new JSONObject();
        String order_id=requestJSON.getString("order_id");
        logger.debug("order_id=================="+order_id+"&&&&&");
        if(order_id==null || order_id.equals(""))
        {
            json.put("rspCode","340001");
            json.put("rspDesc","订单id为空");

        }
        else {
            //String aa="body={\"bonus_type\":\"" + "RedPackage" + "\",\"event_type\":\"" + "BonusChoose" + "\",\"order_id\":" + "139637" + "}"+"&key=01818EFD0816BAD5";
            String aa="body={\"bonus_type\":\"" + "RedPackage" + "\",\"event_type\":\"" + "BonusChoose" + "\",\"order_id\":" + order_id.trim() + "}"+"&key=01818EFD0816BAD5";

            logger.debug("参与排序的字符串格式======================" + aa);
            String signature = commenUtil.getSHA1(aa);
            logger.debug("order_id=================="+order_id+"&&&&&");
            String responseStr = httpsPostMethod.doPostJson("http://ticket.ipaika.com/event/listener?verify=" + signature,
                    "{\"bonus_type\":\"" + "RedPackage" + "\",\"event_type\":\"" + "BonusChoose" + "\",\"order_id\":" + order_id.trim() + "}");
            logger.debug("================兑奖方式返回的内容：=================" + responseStr);
            json.put("rspCode", "000");
            json.put("rspDesc","成功");
        }
        String result = callback + "(" +json.toString() + ")";
        this.getResponse().setCharacterEncoding("UTF-8");
        this.getResponse().addHeader("Access-Control-Allow-Origin", "*");
        logger.debug("H通过openid获取用户信息userinfo--response数据为：---" + result);
        try {
            this.getResponse().getWriter().append(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public lotteryTicketDaoimpl getLtDaoimpl() {
        return ltDaoimpl;
    }

    public void setLtDaoimpl(lotteryTicketDaoimpl ltDaoimpl) {
        this.ltDaoimpl = ltDaoimpl;
    }
}
