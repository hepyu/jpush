package com.tongbupan.tpush.plugin.perfermancetest;

import java.util.List;

public class PerfermanceTestParamBean {

	private int threadCount;

	private int messageSize;

	private int runtime;

	private int connCount;

	private boolean openThisPlug;

	private int interval;

	private int tpush_server_websocket_conn_count;

	private List<String> current_test_client_flag_list;

	public int getTpush_server_websocket_conn_count() {
		return tpush_server_websocket_conn_count;
	}

	public void setTpush_server_websocket_conn_count(
			int tpush_server_websocket_conn_count) {
		this.tpush_server_websocket_conn_count = tpush_server_websocket_conn_count;
	}

	public List<String> getCurrent_test_client_flag_list() {
		return current_test_client_flag_list;
	}

	public void setCurrent_test_client_flag_list(
			List<String> current_test_client_flag_list) {
		this.current_test_client_flag_list = current_test_client_flag_list;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public int getConnCount() {
		return connCount;
	}

	public void setConnCount(int connCount) {
		this.connCount = connCount;
	}

	public boolean isOpenThisPlug() {
		return openThisPlug;
	}

	public void setOpenThisPlug(boolean openThisPlug) {
		this.openThisPlug = openThisPlug;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
