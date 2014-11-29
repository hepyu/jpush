package com.tongbupan.test.websocket;

import java.util.concurrent.atomic.AtomicLong;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.websocket.WebSocket;
import com.tongbupan.tpush.TPushInitializer;
import com.tongbupan.tpush.message.definition.ACKMessage;
import com.tongbupan.tpush.message.definition.AbstractBaseMessage;
import com.tongbupan.tpush.message.definition.ConnIdMessage;
import com.tongbupan.tpush.tenum.MessageTypeEnum;

public class ClientWebSocket implements WebSocket.OnTextMessage {

	private static final Logger logger = Logger
			.getLogger(ClientWebSocket.class);

	private static ObjectMapper objmapper = new ObjectMapper();

	private Connection connection;
	private String userId;

	public ClientWebSocket() {
	}

	// store the alive total count of ws conn
	private static final AtomicLong WS_LIVE_COUNT = new AtomicLong(0);

	// the count of receive message
	private static final AtomicLong RECEIVE_MESSAGE_COUNT = new AtomicLong(0);

	public Connection getConnection() {
		return connection;
	}

	public void onClose(int closeCode, String message) {
		WS_LIVE_COUNT.decrementAndGet();
	}

	public void onOpen(Connection connection) {
		this.connection = connection;
		WS_LIVE_COUNT.addAndGet(1);
	}

	public void onMessage(String text) {
		logger.info(new StringBuilder("receive message:").append(text)
				.append(",").append(System.currentTimeMillis()));

		if (TPushInitializer.getBeatString().equals(text)) {
			return;
		}
		try {
			JSONObject jsonobject = JSONObject.fromObject(text);
			if (jsonobject.getString("@class").equals(
					ConnIdMessage.class.getSimpleName())) {
				if (connection != null) {
					ACKMessage ack = new ACKMessage();
					ack.setArriveId(jsonobject.getString("arriveId"));
					ack.setCurConnId(jsonobject
							.getString(AbstractBaseMessage.FIELD_CUR_CONN_ID));
					ack.setTimestamp(new StringBuilder(jsonobject
							.getString(AbstractBaseMessage.FIELD_TIMESTAMP))
							.append(",").append(System.currentTimeMillis())
							.toString());

					ack.setType(MessageTypeEnum.valueOf(jsonobject
							.getString("@class")));

					try {
						connection.sendMessage(objmapper
								.writeValueAsString(ack));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static AtomicLong getWsLiveCount() {
		return WS_LIVE_COUNT;
	}

	public static long getReceiveMessageCount() {
		return RECEIVE_MESSAGE_COUNT.longValue();
	}
}
