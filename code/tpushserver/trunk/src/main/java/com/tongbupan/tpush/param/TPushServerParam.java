package com.tongbupan.tpush.param;

public class TPushServerParam {

	private int topicExpireTime;

	private int historyMessagePushCount;

	/**
	 * unit by second
	 */
	private int heartbeatInterval;

	private int pusherWorkerNum;

	/**
	 * messageExpireTime unit by second
	 */
	private int messageExpireTime;

	/**
	 * init size of TopicResourceRelationContainerSize
	 */
	private int initTopicResourceRelationContainerSize;

	/**
	 * init size of ConnStatusContainerSize
	 */
	private int initConnStatusContainerSize;

	private int sendMsgRetryCount;

	// by millions
	private long sendMsgRetrySleepTime;

	// start plug config

	/**
	 * used for performance test
	 */
	private boolean openQPSTestPlug;

	// end plug config

	public int getTopicExpireTime() {
		return topicExpireTime;
	}

	public void setTopicExpireTime(int topicExpireTime) {
		this.topicExpireTime = topicExpireTime;
	}

	public int getHistoryMessagePushCount() {
		return historyMessagePushCount;
	}

	public void setHistoryMessagePushCount(int historyMessagePushCount) {
		this.historyMessagePushCount = historyMessagePushCount;
	}

	public int getHeartbeatInterval() {
		return heartbeatInterval;
	}

	public void setHeartbeatInterval(int heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	public int getPusherWorkerNum() {
		return pusherWorkerNum;
	}

	public void setPusherWorkerNum(int pusherWorkerNum) {
		this.pusherWorkerNum = pusherWorkerNum;
	}

	public int getInitTopicResourceRelationContainerSize() {
		return initTopicResourceRelationContainerSize;
	}

	public void setInitTopicResourceRelationContainerSize(
			int initTopicResourceRelationContainerSize) {
		this.initTopicResourceRelationContainerSize = initTopicResourceRelationContainerSize;
	}

	public int getInitConnStatusContainerSize() {
		return initConnStatusContainerSize;
	}

	public void setInitConnStatusContainerSize(int initConnStatusContainerSize) {
		this.initConnStatusContainerSize = initConnStatusContainerSize;
	}

	public int getMessageExpireTime() {
		return messageExpireTime;
	}

	public void setMessageExpireTime(int messageExpireTime) {
		this.messageExpireTime = messageExpireTime;
	}

	public boolean isOpenQPSTestPlug() {
		return openQPSTestPlug;
	}

	public void setOpenQPSTestPlug(boolean openQPSTestPlug) {
		this.openQPSTestPlug = openQPSTestPlug;
	}

	public int getSendMsgRetryCount() {
		return sendMsgRetryCount;
	}

	public void setSendMsgRetryCount(int sendMsgRetryCount) {
		this.sendMsgRetryCount = sendMsgRetryCount;
	}

	public long getSendMsgRetrySleepTime() {
		return sendMsgRetrySleepTime;
	}

	public void setSendMsgRetrySleepTime(long sendMsgRetrySleepTime) {
		this.sendMsgRetrySleepTime = sendMsgRetrySleepTime;
	}

}
