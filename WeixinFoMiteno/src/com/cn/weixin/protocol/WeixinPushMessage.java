package com.cn.weixin.protocol;

public class WeixinPushMessage {
	/**
	 * ������΢�ź�
	 */
	private String ToUserName="";
	/**
	 * ���ͷ��ʺţ�һ��OpenID��
	 * ��ȯ���ʺţ�һ��OpenID��
	 */
	private String FromUserName="";
	/**
	 * ��Ϣ����ʱ�䣨���ͣ�
	 */
	private String CreateTime="";
	/**
	 * ��Ϣ����
	 * event �¼�
	 * text �ı���Ϣ
	 * image ͼƬ��Ϣ
	 * voice ������Ϣ
	 * video ��Ƶ��Ϣ
	 * location ����λ����Ϣ
	 * link  ������Ϣ
	 * 
	 */
	private String MsgType="";
	
	private String Content="";
	
	private String EventKey="";
	
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	/**
	 * �¼�����
	 *  subscribe(����)
	 *  unsubscribe(ȡ������)
	 *  card_pass_check(��ȯͨ�����)
	 *  card_not_pass_check����ȯδͨ����ˣ�
	 *  user_get_card(�û���ȡ��ȯ)
	 *  user_del_card(�û�ɾ����ȯ)
	 *  user_view_card(�û������Ա��)
	 *  user_consume_card(�����¼�)
	 */
	private String Event="";
	/**
	 * �Ƿ�Ϊת����1 �����ǣ�0 �����
	 */
	private String IsGiveByFriend="";
	/**
	 * ���ͷ��˺ţ�һ��OpenID����"IsGiveByFriend��Ϊ1 ʱ��д�ò�����
	 */
	private String FriendUserName="";
	/**
	 * ��ȡ����ֵ��������ȡ��������ͳ�ơ��������ɶ�ά��ӿڼ����JS API �ӿ����Զ�����ֶε�����ֵ��
	 */
	private String OuterId="";
	/**
	 * ��ȯID
	 */
	private String OldUserCardCode="";
	/**
	 * ��ȯID
	 */
	private String CardId="";
	/**
	 * code ���кš��Զ���code �����Զ���code�Ŀ�ȯ����ȡ��֧���¼����͡�
	 */
	private String UserCardCode="";
	/**
	 * �̻��Լ��ڲ�ID�����ֶ��е�sid
	 */
	private String UniqId="";
	
	/**
	 * ΢�ŵ��ŵ�ID��΢�����ŵ�Ψһ��ʾID
	 */
	private String PoiId="";
	/**
	 * ��˽�����ɹ�succ��ʧ��fail
	 */
	private String Result="";
	/**
	 * �ɹ���֪ͨ��Ϣ�������ʧ�ܵĲ�������
	 */
	private String Msg="";
	/**
	 * ���̻�ID��
	 */
	private String MerchantId="";
	/**
	 * �Ƿ�ͨ����Ϊ1ʱ���ͨ����(���̻����)
	 */
	private String IsPass="";
	/**
	 * ���ص�ԭ��(���̻����)
	 */
	private String Reason="";
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getUniqId() {
		return UniqId;
	}
	public void setUniqId(String uniqId) {
		UniqId = uniqId;
	}
	public String getPoiId() {
		return PoiId;
	}
	public void setPoiId(String poiId) {
		PoiId = poiId;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getIsGiveByFriend() {
		return IsGiveByFriend;
	}
	public void setIsGiveByFriend(String isGiveByFriend) {
		IsGiveByFriend = isGiveByFriend;
	}
	public String getFriendUserName() {
		return FriendUserName;
	}
	public void setFriendUserName(String friendUserName) {
		FriendUserName = friendUserName;
	}
	public String getOuterId() {
		return OuterId;
	}
	public void setOuterId(String outerId) {
		OuterId = outerId;
	}
	public String getOldUserCardCode() {
		return OldUserCardCode;
	}
	public void setOldUserCardCode(String oldUserCardCode) {
		OldUserCardCode = oldUserCardCode;
	}
	public String getCardId() {
		return CardId;
	}
	public void setCardId(String cardId) {
		CardId = cardId;
	}
	public String getUserCardCode() {
		return UserCardCode;
	}
	public void setUserCardCode(String userCardCode) {
		UserCardCode = userCardCode;
	}
	public String getMerchantId() {
		return MerchantId;
	}
	public void setMerchantId(String merchantId) {
		MerchantId = merchantId;
	}
	public String getIsPass() {
		return IsPass;
	}
	public void setIsPass(String isPass) {
		IsPass = isPass;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	
}
