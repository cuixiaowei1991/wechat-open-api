package com.cn.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by cuixiaowei on 2016/5/31.
 * 彩票信息表
 */
@Entity
@Table(name = "Lottery_Ticket")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@AccessType("field")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lottery_Ticket
{
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "LOTTERY_ID", length = 32)
    private String LOTTERY_ID;     //彩票id
    /**
     *彩种
     */
    @Column(name = "LOTTERY_TYPE", length = 32)
    private String LOTTERY_TYPE;
    /**
     *场次时间
     */
    @Column(name = "LOTTERY_EVENTTIME", length = 100)
    private String LOTTERY_EVENTTIME;
    /**
     *发起时间
     */

    @Column(name = "LOTTERY_STARTTIME")
    @Type(type = "java.util.Date")
    private Date LOTTERY_STARTTIME;
    /**
     *投注内容
     */
    @Column(name = "LOTTERY_CONTENT", length = 2000)
    private String LOTTERY_CONTENT;
    /**
     *注数
     */
    @Column(name = "LOTTERY_MULTIPLE", length = 10)
    private int LOTTERY_MULTIPLE;
    /**
     *是否开奖 1是开奖 2是未开奖，默认2
     */
    @Column(name = "LOTTERY_ISLOTT", length = 1)
    private int LOTTERY_ISLOTT;
    /**
     *是否中奖 1是中奖 2是未中奖 0是等待开奖结果，默认0
     */
    @Column(name = "LOTTERY_ISWIN", length = 1)
    private int LOTTERY_ISWIN;
    /**
     *中奖金额
     */
    @Column(name = "LOTTERY_PRICE", length = 10)
    private String LOTTERY_PRICE;
    /**
     *投注金额
     */
    @Column(name = "LOTTERY_BET_PRICE", length = 10)
    private String LOTTERY_BET_PRICE;

    /**
     * 投注时间
     */
    @Column(name = "LOTTERY_BET_TIME")
    @Type(type = "java.util.Date")
    private Date LOTTERY_BET_TIME;
    /**
     *是否出票 1是出票 2是未出票，默认2
     */
    @Column(name = "LOTTERY_ARETICKET", length = 1)
    private int LOTTERY_ARETICKET;
    /**
     *微信用户标识
     */
    @Column(name = "LOTTERY_WETCHAT_OPENID", length = 1000)
    private String LOTTERY_WETCHAT_OPENID;
    /**
     *微信公众号标示
     */
    @Column(name = "LOTTERY_WETCHAT_APPID", length = 1000)
    private String LOTTERY_WETCHAT_APPID;
    /**
     *送彩商铺（商户id）
     */
    @Column(name = "LOTTERY_SHOP", length = 32)
    private String LOTTERY_SHOP;

    /**
     *送彩商铺（商户）名称
     */
    @Column(name = "LOTTERY_SHOPNAME", length = 500)
    private String LOTTERY_SHOPNAME;
    /**
     *领彩预约电话（商户电话）
     */
    @Column(name = "LOTTERY_PHONE", length = 100)
    private String LOTTERY_PHONE;
    /**
     *流水号，彩票号 ，订单号
     */
    @Column(name = "LOTTERY_SERIAL_NUM", length = 500)
    private String LOTTERY_SERIAL_NUM;
    /**
     *承兑方式
     */
    @Column(name = "LOTTERY_ACCPET_TYPE", length = 2000)
    private String LOTTERY_ACCPET_TYPE;
    /**
     *过关方式
     */
    @Column(name = "LOTTERY_CLEARANCE_TYPE", length = 100)
    private String LOTTERY_CLEARANCE_TYPE;
    /**
     *订单id（我们自己的）
     */
    @Column(name = "LOTTERY_ORDER", length = 32)
    private String LOTTERY_ORDER;

    public String getLOTTERY_ORDER() {
        return LOTTERY_ORDER;
    }

    public void setLOTTERY_ORDER(String LOTTERY_ORDER) {
        this.LOTTERY_ORDER = LOTTERY_ORDER;
    }

    public String getLOTTERY_ID() {
        return LOTTERY_ID;
    }

    public void setLOTTERY_ID(String LOTTERY_ID) {
        this.LOTTERY_ID = LOTTERY_ID;
    }

    public String getLOTTERY_TYPE() {
        return LOTTERY_TYPE;
    }

    public void setLOTTERY_TYPE(String LOTTERY_TYPE) {
        this.LOTTERY_TYPE = LOTTERY_TYPE;
    }

    public String getLOTTERY_EVENTTIME() {
        return LOTTERY_EVENTTIME;
    }

    public void setLOTTERY_EVENTTIME(String LOTTERY_EVENTTIME) {
        this.LOTTERY_EVENTTIME = LOTTERY_EVENTTIME;
    }

    public Date getLOTTERY_STARTTIME() {
        return LOTTERY_STARTTIME;
    }

    public void setLOTTERY_STARTTIME(Date LOTTERY_STARTTIME) {
        this.LOTTERY_STARTTIME = LOTTERY_STARTTIME;
    }

    public String getLOTTERY_CONTENT() {
        return LOTTERY_CONTENT;
    }

    public void setLOTTERY_CONTENT(String LOTTERY_CONTENT) {
        this.LOTTERY_CONTENT = LOTTERY_CONTENT;
    }

    public int getLOTTERY_MULTIPLE() {
        return LOTTERY_MULTIPLE;
    }

    public void setLOTTERY_MULTIPLE(int LOTTERY_MULTIPLE) {
        this.LOTTERY_MULTIPLE = LOTTERY_MULTIPLE;
    }

    public int getLOTTERY_ISLOTT() {
        return LOTTERY_ISLOTT;
    }

    public void setLOTTERY_ISLOTT(int LOTTERY_ISLOTT) {
        this.LOTTERY_ISLOTT = LOTTERY_ISLOTT;
    }

    public int getLOTTERY_ISWIN() {
        return LOTTERY_ISWIN;
    }

    public void setLOTTERY_ISWIN(int LOTTERY_ISWIN) {
        this.LOTTERY_ISWIN = LOTTERY_ISWIN;
    }

    public String getLOTTERY_PRICE() {
        return LOTTERY_PRICE;
    }

    public void setLOTTERY_PRICE(String LOTTERY_PRICE) {
        this.LOTTERY_PRICE = LOTTERY_PRICE;
    }

    public String getLOTTERY_BET_PRICE() {
        return LOTTERY_BET_PRICE;
    }

    public void setLOTTERY_BET_PRICE(String LOTTERY_BET_PRICE) {
        this.LOTTERY_BET_PRICE = LOTTERY_BET_PRICE;
    }

    public Date getLOTTERY_BET_TIME() {
        return LOTTERY_BET_TIME;
    }

    public void setLOTTERY_BET_TIME(Date LOTTERY_BET_TIME) {
        this.LOTTERY_BET_TIME = LOTTERY_BET_TIME;
    }

    public int getLOTTERY_ARETICKET() {
        return LOTTERY_ARETICKET;
    }

    public void setLOTTERY_ARETICKET(int LOTTERY_ARETICKET) {
        this.LOTTERY_ARETICKET = LOTTERY_ARETICKET;
    }

    public String getLOTTERY_WETCHAT_OPENID() {
        return LOTTERY_WETCHAT_OPENID;
    }

    public void setLOTTERY_WETCHAT_OPENID(String LOTTERY_WETCHAT_OPENID) {
        this.LOTTERY_WETCHAT_OPENID = LOTTERY_WETCHAT_OPENID;
    }

    public String getLOTTERY_WETCHAT_APPID() {
        return LOTTERY_WETCHAT_APPID;
    }

    public void setLOTTERY_WETCHAT_APPID(String LOTTERY_WETCHAT_APPID) {
        this.LOTTERY_WETCHAT_APPID = LOTTERY_WETCHAT_APPID;
    }

    public String getLOTTERY_SHOP() {
        return LOTTERY_SHOP;
    }

    public void setLOTTERY_SHOP(String LOTTERY_SHOP) {
        this.LOTTERY_SHOP = LOTTERY_SHOP;
    }

    public String getLOTTERY_SHOPNAME() {
        return LOTTERY_SHOPNAME;
    }

    public void setLOTTERY_SHOPNAME(String LOTTERY_SHOPNAME) {
        this.LOTTERY_SHOPNAME = LOTTERY_SHOPNAME;
    }

    public String getLOTTERY_PHONE() {
        return LOTTERY_PHONE;
    }

    public void setLOTTERY_PHONE(String LOTTERY_PHONE) {
        this.LOTTERY_PHONE = LOTTERY_PHONE;
    }

    public String getLOTTERY_SERIAL_NUM() {
        return LOTTERY_SERIAL_NUM;
    }

    public void setLOTTERY_SERIAL_NUM(String LOTTERY_SERIAL_NUM) {
        this.LOTTERY_SERIAL_NUM = LOTTERY_SERIAL_NUM;
    }

    public String getLOTTERY_ACCPET_TYPE() {
        return LOTTERY_ACCPET_TYPE;
    }

    public void setLOTTERY_ACCPET_TYPE(String LOTTERY_ACCPET_TYPE) {
        this.LOTTERY_ACCPET_TYPE = LOTTERY_ACCPET_TYPE;
    }

    public String getLOTTERY_CLEARANCE_TYPE() {
        return LOTTERY_CLEARANCE_TYPE;
    }

    public void setLOTTERY_CLEARANCE_TYPE(String LOTTERY_CLEARANCE_TYPE) {
        this.LOTTERY_CLEARANCE_TYPE = LOTTERY_CLEARANCE_TYPE;
    }
}
