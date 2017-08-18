package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信自定义消息回复
 * Created by cuixiaowei on 2016/8/4.
 */
@Entity
@Table(name = "AutoMessage")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@org.hibernate.annotations.AccessType("field")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AutoMessage {
    /**
     * 消息id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "messageId", length = 32)
    private String messageId;
    /**
     * 消息内容
     */
    @Column(name = "messageContent", length = 1000)
    private String messageContent;
    /**
     * 回复消息类型 1---被关注自动回复 2----消息自动回复  3--------关键字自动回复
     */
    @Column(name = "messageTypeNum", length = 1)
    private String messageTypeNum;

    /**
     * 自定义消息规则名
     */
    @Column(name = "messageRule", length = 200)
    private String messageRule;
    /**
     * 自定义消息关键字
     */
    @Column(name = "messageName", length = 200)
    private String messageName;
    /**
     * 素材微信端id
     */
    @Column(name = "mediaId", length = 1000)
    private String mediaId;
    /**
     * 消息类型1----text（文本回复）2----image（图片回复）3---news(图文)
     */
    @Column(name = "replyType", length = 1)
    private String replyType;
    /**
     * 素材本地存储的url
     */
    @Column(name = "mediaurl", length = 1000)
    private String mediaurl;
    /**
     * 消息回复顺序
     */
    @Column(name = "replyOrder", length = 1)
    private String replyOrder;
    /**
     * 公众号appid
     */
    @Column(name = "appid", length = 200)
    private String appid;
    /**
     * 创建人
     */
    @Column(name = "messageCreate", length = 50)
    private String  messageCreate;
    /**
     * 创建时间
     */
    @Column(name = "messageCreated")
    @Type(type = "java.util.Date")
    private Date messageCreated;

    /**
     * 是否启用 1：启用；2：禁用
     */
    @Column(name = "isStart", length = 1)
    private String  isStart;

    /**
     * 图文外链
     */
    @Column(name = "outside_url", length = 2000)
    private String  outside_url;

    /**
     * 图文文字说明
     */
    @Column(name = "description", length = 3000)
    private String  description;

    /**
     * 图文标题
     */
    @Column(name = "title", length = 3000)
    private String  title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutside_url() {
        return outside_url;
    }

    public void setOutside_url(String outside_url) {
        this.outside_url = outside_url;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageTypeNum() {
        return messageTypeNum;
    }

    public void setMessageTypeNum(String messageTypeNum) {
        this.messageTypeNum = messageTypeNum;
    }

    public String getMessageRule() {
        return messageRule;
    }

    public void setMessageRule(String messageRule) {
        this.messageRule = messageRule;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMessageCreate() {
        return messageCreate;
    }

    public void setMessageCreate(String messageCreate) {
        this.messageCreate = messageCreate;
    }

    public Date getMessageCreated() {
        return messageCreated;
    }

    public void setMessageCreated(Date messageCreated) {
        this.messageCreated = messageCreated;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getReplyOrder() {
        return replyOrder;
    }

    public void setReplyOrder(String replyOrder) {
        this.replyOrder = replyOrder;
    }

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }
}
