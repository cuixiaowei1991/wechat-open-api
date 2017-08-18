package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by cuixiaowei on 2016/4/26.
 *
 * 微信授权表
 */
@Entity
@Table(name = "WeiXinAuthorization")
@GenericGenerator(name = "system-uuid", strategy = "uuid" )
@org.hibernate.annotations.AccessType("field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WeiXinAuthorization {
    /**
     * 微信授权表 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "weixin_auth_id", length = 32)
    private String weixin_auth_id;
    /**
     *授权公众号id
     */
    @Column(name = "weixin_Authorizer_Appid", length = 100)
    private String weixin_Authorizer_Appid;
    /**
     *授权公众号授权码（有效期一小时）
     */
    @Column(name = "weixin_Authorizer_code", length = 800)
    private String weixin_Authorizer_code;
    /**
     *微信服务端授权码创建日期（毫秒）存入数据库后转为正常日期
     */
    @Column(name = "CreateTime", length = 200)
    private String CreateTime;
    /**
     *微信服务端授权码失效日期（毫秒）存入数据库后转为正常日期
     */
    @Column(name = "AuthorizationCodeExpiredTime", length = 200)
    private String AuthorizationCodeExpiredTime;
    /**
     *授权状态 1：授权成功 2：取消授权 3：授权更新
     */
    @Column(name = "Authorization_zhuangtai", length = 200)
    private String Authorization_zhuangtai;


    /**
     * 1.公众号昵称---2.账号别名-----3.头像图片（url）---4.二维码地址----5.商户号-----6.公众号秘钥---7.商户id（商助平台）
     * @return
     */
    /**
     * 公众号昵称
     */
    @Column(name = "nickname", length = 400)
    private String  nickname;
    /**
     * 账号别名
     */
    @Column(name = "alias", length = 400)
    private String  alias;
    /**
     * 头像图片（url）
     */
    @Column(name = "image_url", length = 1000)
    private String  image_url;
    /**
     * 二维码地址（url）
     */
    @Column(name = "code_url", length = 1000)
    private String  code_url;
    /**
     * 商户号
     */
    @Column(name = "much_id", length = 200)
    private String  much_id;
    /**
     * 公众号秘钥
     */
    @Column(name = "num_secret", length = 200)
    private String  num_secret;
    /**
     * 商户id（商助平台）
     */
    @Column(name = "shop_id", length = 32)
    private String  shop_id;
    /**
     * 用于authorizer_access_token过期时获取新的token（妥善保管，切勿丢失，一旦丢失，需要第三方平台重新授权）
     */
    @Column(name = "authorizer_refresh_token", length = 2000)
    private String  authorizer_refresh_token;

    /**
     * 创建时间
     */
    @Column(name = "weixinInfo_CREATED")
    @Type(type = "java.util.Date")
    private Date weixinInfoCreated;


    /**
     * 商户微信号（不是商户昵称）
     */
    @Column(name = "weixin_num", length = 100)
    private String  weixin_num;

    /**
     * 微信支付成功之后的回调地址（POS出票）
     */
    @Column(name = "return_url", length = 400)
    private String  return_url;

    /**
     * 预备字段1
     */
    @Column(name = "prepare1", length = 400)
    private String  prepare1 ;
    /**
     * 预备字段2
     */
    @Column(name = "prepare2", length = 400)
    private String  prepare2 ;


    public WeiXinAuthorization()
    {
        this.weixinInfoCreated=new Date();
    }

    public String getAuthorization_zhuangtai() {
        return Authorization_zhuangtai;
    }

    public void setAuthorization_zhuangtai(String authorization_zhuangtai) {
        Authorization_zhuangtai = authorization_zhuangtai;
    }

    public String getWeixin_auth_id() {
        return weixin_auth_id;
    }

    public void setWeixin_auth_id(String weixin_auth_id) {
        this.weixin_auth_id = weixin_auth_id;
    }

    public String getWeixin_Authorizer_Appid() {
        return weixin_Authorizer_Appid;
    }

    public void setWeixin_Authorizer_Appid(String weixin_Authorizer_Appid) {
        this.weixin_Authorizer_Appid = weixin_Authorizer_Appid;
    }

    public String getWeixin_Authorizer_code() {
        return weixin_Authorizer_code;
    }

    public void setWeixin_Authorizer_code(String weixin_Authorizer_code) {
        this.weixin_Authorizer_code = weixin_Authorizer_code;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getAuthorizationCodeExpiredTime() {
        return AuthorizationCodeExpiredTime;
    }

    public void setAuthorizationCodeExpiredTime(String authorizationCodeExpiredTime) {
        AuthorizationCodeExpiredTime = authorizationCodeExpiredTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMuch_id() {
        return much_id;
    }

    public void setMuch_id(String much_id) {
        this.much_id = much_id;
    }

    public String getNum_secret() {
        return num_secret;
    }

    public void setNum_secret(String num_secret) {
        this.num_secret = num_secret;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getAuthorizer_refresh_token() {
        return authorizer_refresh_token;
    }

    public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
        this.authorizer_refresh_token = authorizer_refresh_token;
    }

    public Date getWeixinInfoCreated() {
        return weixinInfoCreated;
    }

    public void setWeixinInfoCreated(Date weixinInfoCreated) {
        this.weixinInfoCreated = weixinInfoCreated;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getWeixin_num() {
        return weixin_num;
    }

    public void setWeixin_num(String weixin_num) {
        this.weixin_num = weixin_num;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getPrepare1() {
        return prepare1;
    }

    public void setPrepare1(String prepare1) {
        this.prepare1 = prepare1;
    }

    public String getPrepare2() {
        return prepare2;
    }

    public void setPrepare2(String prepare2) {
        this.prepare2 = prepare2;
    }
}
