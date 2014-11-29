package com.tongbupan.tpush.communication.connection.impl.jetty8;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.apache.log4j.Logger;
import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.communication.cenum.Transport;
import com.tongbupan.tpush.communication.config.CommunicationBeanConfig;
import com.tongbupan.tpush.communication.connection.TPushWebSocketConnection;
import com.tongbupan.tpush.message.util.MessageLogUtil;
import com.tongbupan.tpush.param.TPushServerParam;

public abstract class Jetty8WebSocketConnection extends
		TPushWebSocketConnection implements WebSocket, WebSocket.OnTextMessage {

	private static final Logger logger = Logger
			.getLogger(Jetty8WebSocketConnection.class);

	protected Connection connection;

	protected String userId;

	protected String userAgent;

	protected String[] topicList;

	@Override
	public void configRequest(HttpServletRequest req) {
		userId = (String) req.getAttribute("userId");
		userAgent = req.getHeader("User-Agent");
		topicList = req.getParameterValues(TOPIC_KEY);

	}

	@Override
	public void onOpen(Connection connection) {
		this.connection = connection;
		processAfterOpen();
	}

	@Override
	public Transport transport() {
		return Transport.WEBSOCKET;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String[] getTopics() {
		return topicList;
	}

	@Override
	public String getUserAgent() {
		return this.userAgent;
	}

	@Override
	public void sendMessage(String message) {
		if (connection != null && connection.isOpen()) {
			boolean sendSuccess = false;
			TPushServerParam param = TPushServerBeanFactory.getBean(
					"tpushParam", TPushServerParam.class);
			int upLimit = param.getSendMsgRetryCount();
			int count = 0;
			while (count < upLimit) {
				try {
					this.connection.sendMessage(message);
					long ts1 = System.currentTimeMillis();
					MessageLogUtil.info(message, this, logger);
					long ts2 = System.currentTimeMillis();
					long in = ts2 - ts1;
					if (in > 10) {
						logger.error(new StringBuilder("msg.send.time>10.")
								.append(in));
					} else if (in > 5) {
						logger.error(new StringBuilder("msg.send.time>5.")
								.append(in));
					}
					sendSuccess = true;
					break;
				} catch (IOException e) {
					try {
						Thread.sleep(param.getSendMsgRetrySleepTime() * 1000);
					} catch (InterruptedException e1) {
						logger.error(e1.getMessage(), e);
					}
					logger.error(e.getMessage(), e);
				}
				++count;
			}
			if (!sendSuccess) {
				logger.error(new StringBuilder().append("message send fail.")
						.append("uplimit:").append(upLimit)
						.append(";User-Agent:").append(userAgent)
						.append(";message:").append(message));
				this.close();
			}
		}
	}

	@Override
	public void close() {
		// 1.remove relationship
		CommunicationBeanConfig.getTopicCenterBean().remove(this);
		// 2.close real websocket
		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public boolean isOpen() {
		if (connection != null) {
			return connection.isOpen();
		}
		return false;
	}

	@Override
	public void setTopics(String[] topics) {
		topicList = topics;
	}

}
