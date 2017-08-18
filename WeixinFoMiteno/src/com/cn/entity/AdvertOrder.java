package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 支付投放广告和微信订单关联
 * Created by cuixiaowei on 2016/9/14.
 */
@Entity
@Table(name = "AdvertOrder")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@org.hibernate.annotations.AccessType("field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class AdvertOrder {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "advertOrder_id", length = 32)
    private String advertOrder_id;
    /**
     * 广告图片地址
     */
    @Column(name = "advertOrder_picurl", length = 1000)
    private String advertOrder_picurl;
    /**
     * 广告图片外链地址
     */
    @Column(name = "advertOrder_url", length = 1000)
    private String advertOrder_url;
    /**
     * 微信订单号
     */
    @Column(name = "advertOrder_num", length = 100)
    private String advertOrder_num;
    /**
     * 商户id
     */
    @Column(name = "advertOrder_shopid", length = 32)
    private String advertOrder_shopid;
    /**
     * 商户名称
     */
    @Column(name = "advertOrder_shopname", length = 200)
    private String advertOrder_shopname;
    /**
     * 商品介绍
     */
    @Column(name = "advertOrder_goodsIntroduction", length = 1000)
    private String advertOrder_goodsIntroduction;
    /**
     * 广告图片（微信端）地址
     */
    @Column(name = "advertOrder_imgWeixinPath", length = 1000)
    private String advertOrder_imgWeixinPath;

    public String getAdvertOrder_id() {
        return advertOrder_id;
    }

    public void setAdvertOrder_id(String advertOrder_id) {
        this.advertOrder_id = advertOrder_id;
    }

    public String getAdvertOrder_picurl() {
        return advertOrder_picurl;
    }

    public void setAdvertOrder_picurl(String advertOrder_picurl) {
        this.advertOrder_picurl = advertOrder_picurl;
    }

    public String getAdvertOrder_url() {
        return advertOrder_url;
    }

    public void setAdvertOrder_url(String advertOrder_url) {
        this.advertOrder_url = advertOrder_url;
    }

    public String getAdvertOrder_num() {
        return advertOrder_num;
    }

    public void setAdvertOrder_num(String advertOrder_num) {
        this.advertOrder_num = advertOrder_num;
    }

    public String getAdvertOrder_shopid() {
        return advertOrder_shopid;
    }

    public void setAdvertOrder_shopid(String advertOrder_shopid) {
        this.advertOrder_shopid = advertOrder_shopid;
    }

    public String getAdvertOrder_shopname() {
        return advertOrder_shopname;
    }

    public void setAdvertOrder_shopname(String advertOrder_shopname) {
        this.advertOrder_shopname = advertOrder_shopname;
    }

    public String getAdvertOrder_goodsIntroduction() {
        return advertOrder_goodsIntroduction;
    }

    public void setAdvertOrder_goodsIntroduction(String advertOrder_goodsIntroduction) {
        this.advertOrder_goodsIntroduction = advertOrder_goodsIntroduction;
    }

    public String getAdvertOrder_imgWeixinPath() {
        return advertOrder_imgWeixinPath;
    }

    public void setAdvertOrder_imgWeixinPath(String advertOrder_imgWeixinPath) {
        this.advertOrder_imgWeixinPath = advertOrder_imgWeixinPath;
    }
}
