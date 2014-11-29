package com.tongbupan.tpush.message.definition;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class WebSocketStatMessage {

	// �ͻ����յ�����������
	private int receiveBeatCount;

	// �ͻ����յ�����Ϣ����
	private int receiveMsgCount;

	// �ͻ��˷���ack��Ϣ����
	private int sendACKCount;

	// ws conn����ʱ�䳤��
	private int connTime;

	// ��Ϊʲôԭ���л���websocket
	private int errorCode;

	public int getReceiveBeatCount() {
		return receiveBeatCount;
	}

	public void setReceiveBeatCount(int receiveBeatCount) {
		this.receiveBeatCount = receiveBeatCount;
	}

	public int getReceiveMsgCount() {
		return receiveMsgCount;
	}

	public void setReceiveMsgCount(int receiveMsgCount) {
		this.receiveMsgCount = receiveMsgCount;
	}

	public int getSendACKCount() {
		return sendACKCount;
	}

	public void setSendACKCount(int sendACKCount) {
		this.sendACKCount = sendACKCount;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getConnTime() {
		return connTime;
	}

	public void setConnTime(int connTime) {
		this.connTime = connTime;
	}

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		WebSocketStatMessage msg = new WebSocketStatMessage();
		try {
			System.out.println(objectMapper.writeValueAsString(msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
