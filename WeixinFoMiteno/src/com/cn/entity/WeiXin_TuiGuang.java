package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by cuixiaowei on 2016/5/26.
 */
@Entity
@Table(name = "WeiXin_TuiGuang")
@GenericGenerator(name = "system-uuid", strategy = "uuid" )
@org.hibernate.annotations.AccessType("field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WeiXin_TuiGuang {
    /**
     * 微信——推广表 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "weixin_tuiguang_id", length = 32)
    private String weixin_tuiguang_id;

    /**
     *微信号（不是微信昵称）
     */
    @Column(name = "wexin_num", length = 100)
    private String wexin_num;
    /**
     *推广团队代号
     */
    @Column(name = "tuiguang_num", length = 100)
    private String tuiguang_num;
    /**
     *推广团队二维码
     */
    @Column(name = "tuiguang_code", length = 1000)
    private String tuiguang_code;
    /**
     *推广团队关注数
     */
    @Column(name = "tuiguang_count")
    private String tuiguang_count;


    public String getWeixin_tuiguang_id() {
        return weixin_tuiguang_id;
    }

    public void setWeixin_tuiguang_id(String weixin_tuiguang_id) {
        this.weixin_tuiguang_id = weixin_tuiguang_id;
    }

    public String getWexin_num() {
        return wexin_num;
    }

    public void setWexin_num(String wexin_num) {
        this.wexin_num = wexin_num;
    }

    public String getTuiguang_num() {
        return tuiguang_num;
    }

    public void setTuiguang_num(String tuiguang_num) {
        this.tuiguang_num = tuiguang_num;
    }

    public String getTuiguang_code() {
        return tuiguang_code;
    }

    public void setTuiguang_code(String tuiguang_code) {
        this.tuiguang_code = tuiguang_code;
    }

    public String getTuiguang_count() {
        return tuiguang_count;
    }

    public void setTuiguang_count(String tuiguang_count) {
        this.tuiguang_count = tuiguang_count;
    }
}
