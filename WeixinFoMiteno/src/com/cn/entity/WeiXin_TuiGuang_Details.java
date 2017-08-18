package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by cuixiaowei on 2016/5/26.
 */
@Entity
@Table(name = "WeiXin_TuiGuang_Details")
@GenericGenerator(name = "system-uuid", strategy = "uuid" )
@org.hibernate.annotations.AccessType("field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WeiXin_TuiGuang_Details {
    /**
     * ΢�š����ƹ�� ����
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "details_id", length = 32)
    private String details_id;
    /**
     *�ƹ��ŶӴ���
     */
    @Column(name = "details_tuiguang_num", length = 100)
    private String details_tuiguang_num;
    /**
     *΢�ź�
     */
    @Column(name = "weixin_num", length = 100)
    private String weixin_num;
    /**
     *��ע��openid
     */
    @Column(name = "details_openid", length = 100)
    private String details_openid;

    /**
     * ��עʱ��
     */
    @Column(name = "details_CREATED")
    @Type(type = "java.util.Date")
    private Date details_CREATED;
    /**
     *�̻�appid
     */
    @Column(name = "appid", length = 500)
    private String appid;

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }

    public String getDetails_tuiguang_num() {
        return details_tuiguang_num;
    }

    public void setDetails_tuiguang_num(String details_tuiguang_num) {
        this.details_tuiguang_num = details_tuiguang_num;
    }

    public String getDetails_openid() {
        return details_openid;
    }

    public void setDetails_openid(String details_openid) {
        this.details_openid = details_openid;
    }

    public Date getDetails_CREATED() {
        return details_CREATED;
    }

    public void setDetails_CREATED(Date details_CREATED) {
        this.details_CREATED = details_CREATED;
    }

    public String getWeixin_num() {
        return weixin_num;
    }

    public void setWeixin_num(String weixin_num) {
        this.weixin_num = weixin_num;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
