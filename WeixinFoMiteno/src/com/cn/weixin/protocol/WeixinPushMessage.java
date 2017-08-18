package com.cn.weixin.protocol;

public class WeixinPushMessage {
	/**
	 * 开发者微信号
	 */
	private String ToUserName="";
	/**
	 * 发送方帐号（一个OpenID）
	 * 领券方帐号（一个OpenID）
	 */
	private String FromUserName="";
	/**
	 * 消息创建时间（整型）
	 */
	private String CreateTime="";
	/**
	 * 消息类型
	 * event 事件
	 * text 文本消息
	 * image 图片消息
	 * voice 语音消息
	 * video 视频消息
	 * location 地理位置消息
	 * link  链接消息
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
	 * 事件类型
	 *  subscribe(订阅)
	 *  unsubscribe(取消订阅)
	 *  card_pass_check(卡券通过审核)
	 *  card_not_pass_check（卡券未通过审核）
	 *  user_get_card(用户领取卡券)
	 *  user_del_card(用户删除卡券)
	 *  user_view_card(用户进入会员卡)
	 *  user_consume_card(核销事件)
	 */
	private String Event="";
	/**
	 * 是否为转赠，1 代表是，0 代表否。
	 */
	private String IsGiveByFriend="";
	/**
	 * 赠送方账号（一个OpenID），"IsGiveByFriend”为1 时填写该参数。
	 */
	private String FriendUserName="";
	/**
	 * 领取场景值，用于领取渠道数据统计。可在生成二维码接口及添加JS API 接口中自定义该字段的整型值。
	 */
	private String OuterId="";
	/**
	 * 卡券ID
	 */
	private String OldUserCardCode="";
	/**
	 * 卡券ID
	 */
	private String CardId="";
	/**
	 * code 序列号。自定义code 及非自定义code的卡券被领取后都支持事件推送。
	 */
	private String UserCardCode="";
	/**
	 * 商户自己内部ID，即字段中的sid
	 */
	private String UniqId="";
	
	/**
	 * 微信的门店ID，微信内门店唯一标示ID
	 */
	private String PoiId="";
	/**
	 * 审核结果，成功succ或失败fail
	 */
	private String Result="";
	/**
	 * 成功的通知信息，或审核失败的驳回理由
	 */
	private String Msg="";
	/**
	 * 子商户ID。
	 */
	private String MerchantId="";
	/**
	 * 是否通过，为1时审核通过。(子商户审核)
	 */
	private String IsPass="";
	/**
	 * 驳回的原因(子商户审核)
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
