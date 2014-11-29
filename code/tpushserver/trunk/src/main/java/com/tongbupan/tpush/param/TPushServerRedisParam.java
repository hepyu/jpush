package com.tongbupan.tpush.param;

public class TPushServerRedisParam {

	private int pushMaxActive;

	private int pushMaxWait;

	private int pushMaxIdle;

	private int notifyMaxActive;

	private int notifyMaxWait;

	private int notifyMaxIdle;

	private String host;

	private int port;

	private boolean openPush;

	private boolean openNotify;

	public int getPushMaxActive() {
		return pushMaxActive;
	}

	public void setPushMaxActive(int pushMaxActive) {
		this.pushMaxActive = pushMaxActive;
	}

	public int getPushMaxWait() {
		return pushMaxWait;
	}

	public void setPushMaxWait(int pushMaxWait) {
		this.pushMaxWait = pushMaxWait;
	}

	public int getPushMaxIdle() {
		return pushMaxIdle;
	}

	public void setPushMaxIdle(int pushMaxIdle) {
		this.pushMaxIdle = pushMaxIdle;
	}

	public int getNotifyMaxActive() {
		return notifyMaxActive;
	}

	public void setNotifyMaxActive(int notifyMaxActive) {
		this.notifyMaxActive = notifyMaxActive;
	}

	public int getNotifyMaxWait() {
		return notifyMaxWait;
	}

	public void setNotifyMaxWait(int notifyMaxWait) {
		this.notifyMaxWait = notifyMaxWait;
	}

	public int getNotifyMaxIdle() {
		return notifyMaxIdle;
	}

	public void setNotifyMaxIdle(int notifyMaxIdle) {
		this.notifyMaxIdle = notifyMaxIdle;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isOpenPush() {
		return openPush;
	}

	public void setOpenPush(boolean openPush) {
		this.openPush = openPush;
	}

	public boolean isOpenNotify() {
		return openNotify;
	}

	public void setOpenNotify(boolean openNotify) {
		this.openNotify = openNotify;
	}

}
