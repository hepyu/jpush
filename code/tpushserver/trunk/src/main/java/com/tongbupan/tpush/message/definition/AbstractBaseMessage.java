package com.tongbupan.tpush.message.definition;

public abstract class AbstractBaseMessage {

	public static final String FIELD_TIMESTAMP = "timestamp";

	public static final String FIELD_TYPE = "@class";

	public static final String FIELD_MSG_ID = "msgId";

	public static final String FIELD_CUR_CONN_ID = "curConnId";

	public static final String FIELD_VERSION = "version";

	public static final String FIELD_ASK_ACK = "askACK";

	public static final String FIELD_ARRIVE_ID = "arriveId";

	// connId, uniq id for every ws conn.
	private String curConnId;

	private String preConnId;

	// flag the num of every ack
	private String timestamp;

	// 当前协议版本号
	// TODO to be modifyed
	private String version = "0.1.1";

	private String msgId;

	private String arriveId;

	private boolean askACK = true;

	public boolean isAskACK() {
		return askACK;
	}

	public void setAskACK(boolean askACK) {
		this.askACK = askACK;
	}

	public String getCurConnId() {
		return curConnId;
	}

	public void setCurConnId(String curConnId) {
		this.curConnId = curConnId;
	}

	public String getPreConnId() {
		return preConnId;
	}

	public void setPreConnId(String preConnId) {
		this.preConnId = preConnId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getArriveId() {
		return arriveId;
	}

	public void setArriveId(String arriveId) {
		this.arriveId = arriveId;
	}

}
