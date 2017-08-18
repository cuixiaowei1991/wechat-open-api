package com.cn.entity;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单表
 * Created by WangWenFang on 2016/5/3.
 */
@Entity
@Table(name = "ORDERINFO")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@AccessType("field")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderInfo {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;     //订单id

    @Column(name = "ORDER_WECHAT_OPENID", length = 1000)
    private String orderWechatOpenid;  //微信用户标识

    @Column(name = "ORDER_WECHAT_APPID", length = 1000)
    private String orderWechatAppid;   //微信公众号标识

    @Column(name = "ORDER_WECHAT_NAME", length = 1000)
    private String orderWechatName;    //微信名称

    @Column(name = "ORDER_PHONE", length = 100)
    private String orderPhone;    //用户手机号，支付手机号

    @Column(name = "ORDER_TICKET", length = 32)
    private String orderTicket;   //现金券id

    @Column(name = "ORDER_PRICE", length = 10)
    private int orderPrice;       //支付金额

    @Column(name = "ORDER_PAYRESULT", length = 1)
    private int orderPayresult;   //支付结果（支付成功1，失败2，未支付3）

    @Column(name = "ORDER_PAYREMARK", length = 3000)
    private String orderPayremark;  //支付备注

    @Column(name = "ORDER_ISRECEIVE", length = 1)
    private int orderIsreceive;     //是否领取彩票 是1 否2

    @Column(name = "ORDER_ISACCEPT", length = 1)
    private int orderIsaccept;  //是否承兑  是1 否2

    @Column(name = "ORDER_PAYDATE", length = 10)
    private Date orderPaydate;      //支付时间

    @Column(name = "ORDER_QUANTITY", length = 10)
    private int orderQuantity;      //订单数量

    @Column(name = "ORDER_SERIAL_NUM", length = 500)
    private String orderSerialNum;  //流水号

    @Column(name = "ORDER_UNIONID", length = 1000)
    private String orderUnionid;     //用户全网唯一的id

    @Column(name = "ORDER_SHOPID", length = 32)
    private String orderShopid;     //商户id

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderWechatOpenid() {
        return orderWechatOpenid;
    }

    public void setOrderWechatOpenid(String orderWechatOpenid) {
        this.orderWechatOpenid = orderWechatOpenid;
    }

    public String getOrderWechatAppid() {
        return orderWechatAppid;
    }

    public void setOrderWechatAppid(String orderWechatAppid) {
        this.orderWechatAppid = orderWechatAppid;
    }

    public String getOrderWechatName() {
        return orderWechatName;
    }

    public void setOrderWechatName(String orderWechatName) {
        this.orderWechatName = orderWechatName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderTicket() {
        return orderTicket;
    }

    public void setOrderTicket(String orderTicket) {
        this.orderTicket = orderTicket;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderPayresult() {
        return orderPayresult;
    }

    public void setOrderPayresult(int orderPayresult) {
        this.orderPayresult = orderPayresult;
    }

    public String getOrderPayremark() {
        return orderPayremark;
    }

    public void setOrderPayremark(String orderPayremark) {
        this.orderPayremark = orderPayremark;
    }

    public int getOrderIsreceive() {
        return orderIsreceive;
    }

    public void setOrderIsreceive(int orderIsreceive) {
        this.orderIsreceive = orderIsreceive;
    }

    public int getOrderIsaccept() {
        return orderIsaccept;
    }

    public void setOrderIsaccept(int orderIsaccept) {
        this.orderIsaccept = orderIsaccept;
    }

    public Date getOrderPaydate() {
        return orderPaydate;
    }

    public void setOrderPaydate(Date orderPaydate) {
        this.orderPaydate = orderPaydate;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderSerialNum() {
        return orderSerialNum;
    }

    public void setOrderSerialNum(String orderSerialNum) {
        this.orderSerialNum = orderSerialNum;
    }

    public String getOrderUnionid() {
        return orderUnionid;
    }

    public void setOrderUnionid(String orderUnionid) {
        this.orderUnionid = orderUnionid;
    }

    public String getOrderShopid() {
        return orderShopid;
    }

    public void setOrderShopid(String orderShopid) {
        this.orderShopid = orderShopid;
    }
}
