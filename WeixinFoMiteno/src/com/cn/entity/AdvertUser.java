package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * ΢���û�֧�����͹�漰���������Ϣ
 * Created by cuixiaowei on 2016/9/14.
 */
@Entity
@Table(name = "AdvertUser")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@org.hibernate.annotations.AccessType("field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdvertUser {
    /**
     * ����id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "advertUser_id", length = 32)
    private String advertUser_id;
    /**
     * ���ͼƬ������ַ
     */
    @Column(name = "advertOrder_picurl", length = 1000)
    private String advertOrder_picurl;
    /**
     * �û��ǳ�
     */
    @Column(name = "advertUser_name", length = 100)
    private String advertUser_name;
    /**
     * �û�openid
     */
    @Column(name = "advertUser_openid", length = 500)
    private String advertUser_openid;
    /**
     * �û��Ա�
     */
    @Column(name = "advertUser_sex", length = 10)
    private String advertUser_sex;
    /**
     * ���׽��
     */
    @Column(name = "advertUser_cash")
    private double advertUser_cash;
    /**
     * �Ƿ�������1---�����2----��
     */
    @Column(name = "advertUser_is_click",length = 1)
    private int advertUser_is_click;
    /**
     * ���ʱ��
     */
    @Column(name = "clickedTime")
    @Type(type = "java.util.Date")
    private Date clickedTime;
    /**
     * ΢�Ŷ�����
     */
    @Column(name = "advertOrder_num", length = 100)
    private String advertOrder_num;
    /**
     * ΢��appid
     */
    @Column(name = "advertOrder_appid", length = 100)
    private String advertOrder_appid;
    /**
     * �������ʱ��
     */
    @Column(name = "sendTime")
    @Type(type = "java.util.Date")
    private Date sendTime;
    /**
     * ���ͼƬ������ַ
     */
    @Column(name = "advertOrder_url", length = 1000)
    private String advertOrder_url;
    /**
     * ���ͼƬ��΢�Ŷˣ���ַ
     */
    @Column(name = "advertOrder_imgWeixinPath", length = 1000)
    private String advertOrder_imgWeixinPath;

    public String getAdvertUser_id() {
        return advertUser_id;
    }

    public void setAdvertUser_id(String advertUser_id) {
        this.advertUser_id = advertUser_id;
    }

    public String getAdvertOrder_picurl() {
        return advertOrder_picurl;
    }

    public void setAdvertOrder_picurl(String advertOrder_picurl) {
        this.advertOrder_picurl = advertOrder_picurl;
    }

    public String getAdvertUser_name() {
        return advertUser_name;
    }

    public void setAdvertUser_name(String advertUser_name) {
        this.advertUser_name = advertUser_name;
    }

    public String getAdvertUser_openid() {
        return advertUser_openid;
    }

    public void setAdvertUser_openid(String advertUser_openid) {
        this.advertUser_openid = advertUser_openid;
    }

    public String getAdvertUser_sex() {
        return advertUser_sex;
    }

    public void setAdvertUser_sex(String advertUser_sex) {
        this.advertUser_sex = advertUser_sex;
    }

    public double getAdvertUser_cash() {
        return advertUser_cash;
    }

    public void setAdvertUser_cash(double advertUser_cash) {
        this.advertUser_cash = advertUser_cash;
    }

    public int getAdvertUser_is_click() {
        return advertUser_is_click;
    }

    public void setAdvertUser_is_click(int advertUser_is_click) {
        this.advertUser_is_click = advertUser_is_click;
    }

    public Date getClickedTime() {
        return clickedTime;
    }

    public void setClickedTime(Date clickedTime) {
        this.clickedTime = clickedTime;
    }

    public String getAdvertOrder_num() {
        return advertOrder_num;
    }

    public void setAdvertOrder_num(String advertOrder_num) {
        this.advertOrder_num = advertOrder_num;
    }

    public String getAdvertOrder_appid() {
        return advertOrder_appid;
    }

    public void setAdvertOrder_appid(String advertOrder_appid) {
        this.advertOrder_appid = advertOrder_appid;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getAdvertOrder_url() {
        return advertOrder_url;
    }

    public void setAdvertOrder_url(String advertOrder_url) {
        this.advertOrder_url = advertOrder_url;
    }

    public String getAdvertOrder_imgWeixinPath() {
        return advertOrder_imgWeixinPath;
    }

    public void setAdvertOrder_imgWeixinPath(String advertOrder_imgWeixinPath) {
        this.advertOrder_imgWeixinPath = advertOrder_imgWeixinPath;
    }
}
