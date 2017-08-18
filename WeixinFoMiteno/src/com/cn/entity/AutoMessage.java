package com.cn.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * ΢���Զ�����Ϣ�ظ�
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
     * ��Ϣid
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "messageId", length = 32)
    private String messageId;
    /**
     * ��Ϣ����
     */
    @Column(name = "messageContent", length = 1000)
    private String messageContent;
    /**
     * �ظ���Ϣ���� 1---����ע�Զ��ظ� 2----��Ϣ�Զ��ظ�  3--------�ؼ����Զ��ظ�
     */
    @Column(name = "messageTypeNum", length = 1)
    private String messageTypeNum;

    /**
     * �Զ�����Ϣ������
     */
    @Column(name = "messageRule", length = 200)
    private String messageRule;
    /**
     * �Զ�����Ϣ�ؼ���
     */
    @Column(name = "messageName", length = 200)
    private String messageName;
    /**
     * �ز�΢�Ŷ�id
     */
    @Column(name = "mediaId", length = 1000)
    private String mediaId;
    /**
     * ��Ϣ����1----text���ı��ظ���2----image��ͼƬ�ظ���3---news(ͼ��)
     */
    @Column(name = "replyType", length = 1)
    private String replyType;
    /**
     * �زı��ش洢��url
     */
    @Column(name = "mediaurl", length = 1000)
    private String mediaurl;
    /**
     * ��Ϣ�ظ�˳��
     */
    @Column(name = "replyOrder", length = 1)
    private String replyOrder;
    /**
     * ���ں�appid
     */
    @Column(name = "appid", length = 200)
    private String appid;
    /**
     * ������
     */
    @Column(name = "messageCreate", length = 50)
    private String  messageCreate;
    /**
     * ����ʱ��
     */
    @Column(name = "messageCreated")
    @Type(type = "java.util.Date")
    private Date messageCreated;

    /**
     * �Ƿ����� 1�����ã�2������
     */
    @Column(name = "isStart", length = 1)
    private String  isStart;

    /**
     * ͼ������
     */
    @Column(name = "outside_url", length = 2000)
    private String  outside_url;

    /**
     * ͼ������˵��
     */
    @Column(name = "description", length = 3000)
    private String  description;

    /**
     * ͼ�ı���
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
