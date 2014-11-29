package com.tongbupan.tpush.tenum;

public enum MessageTypeEnum {

	// 1.base message type
	ACKMessage, ConnIdMessage, WebSocketStatMessage,

	// 2.detail message type
	NoticeMessage, NotifyMessage, FSMessage
}
