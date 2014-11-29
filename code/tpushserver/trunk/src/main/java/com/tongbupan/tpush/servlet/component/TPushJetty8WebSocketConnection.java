/**
 * 
 */
package com.tongbupan.tpush.servlet.component;

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;
import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.communication.config.CommunicationBeanConfig;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.communication.connection.impl.jetty8.Jetty8WebSocketConnection;
import com.tongbupan.tpush.communication.topic.center.TopicCenter;
import com.tongbupan.tpush.handler.IPacketNoticeHandler;
import com.tongbupan.tpush.message.MessageContainer;
import com.tongbupan.tpush.message.MessageSender;
import com.tongbupan.tpush.message.definition.ACKMessage;
import com.tongbupan.tpush.message.definition.AbstractBaseMessage;
import com.tongbupan.tpush.message.util.MessageTimestampUtil;
import com.tongbupan.tpush.tenum.MessageTypeEnum;
import com.tongbupan.tpush.util.CounterUtil;
import com.tongbupan.tpush.util.StringUtil;

/**
 * @author hpy
 * 
 */
public class TPushJetty8WebSocketConnection extends Jetty8WebSocketConnection {

	private static final Logger logger = Logger
			.getLogger(TPushJetty8WebSocketConnection.class);

	@Override
	public void processAfterOpen() {

		logger.info(new StringBuilder(
				"connection has established right now. connectionId:")
				.append(this.getConnectionId()).append(";userId:")
				.append(this.getUserId()).append(";creator:container"));

		CounterUtil.getAlivewsconncounter().incrementAndGet();

		// send protocol message: connidmessage
		MessageSender messageSender = TPushServerBeanFactory.getBean(
				"MessageSender", MessageSender.class);
		messageSender.sendConnIdMessage(this);
	}

	@Override
	public void onClose(int closeCode, String message) {
		CounterUtil.getAlivewsconncounter().decrementAndGet();

		logger.info(new StringBuilder(
				"connection has closed right now. connectionId:")
				.append(this.getConnectionId()).append(";userId:")
				.append(this.getUserId()).append(";closer:container")
				.append(";closeCode:").append(closeCode).append(";message:")
				.append(message));
		CommunicationBeanConfig.getTopicCenterBean().remove(this);
	}

	@Override
	public void onMessage(String data) {
		logger.info("communication.resource.uuid:" + this.getConnectionId());
		try {
			// process msg from client, such as ackmsg, etc...
			if (data != null && data.trim().length() > 0) {

				JSONObject jsonobj = JSONObject.fromObject(data);
				String msgType = jsonobj.getString("@class");

				calMessageArrive(jsonobj, data);

				// process ack message
				if (ACKMessage.class.getSimpleName().equalsIgnoreCase(msgType)) {
					logger.info(new StringBuilder("currentTimestamp:")
							.append(MessageTimestampUtil.getMessageTimestamp())
							.append(",").append(data.replaceAll("\n\r", ""))
							.toString());
					String responseType = jsonobj.getString("type");

					// 1.register topics
					if (MessageTypeEnum.ConnIdMessage.toString().equals(
							responseType)) {
						TopicCenter topicCenter = CommunicationBeanConfig
								.getTopicCenterBean();
						topicCenter.registerTopics(this);
					}

					// 2. closePreConn and sendHistory
					closePreConnAndSendHistory(jsonobj, this);
				} else {
					logger.error(data);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void calMessageArrive(JSONObject jsonobj, String message) {
		try {
			// process the arrive message
			String sn = jsonobj.getString(AbstractBaseMessage.FIELD_ARRIVE_ID);

			String str = MessageContainer.remove(Long.valueOf(sn));
			if (StringUtil.isEmpty(str)) {
				logger.error("MessageStatModel in not exist in map.arriveId:"
						+ sn);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void closePreConnAndSendHistory(JSONObject jsonobj,
			TPushConnection resource) {
		String preConnId = (String) jsonobj.get("preConnId");
		String curConnId = (String) jsonobj.get("curConnId");
		String userId = (String) jsonobj.get("userId");
		String pretstr = (String) jsonobj.get("lastTimestamp");
		// String curtstr = jsonobj.getString("timestamp");

		// 1.close pre conn
		if (preConnId != null && !preConnId.trim().equals("")) {
			// 1.close pre conn
			CommunicationBeanConfig.getTopicCenterBean().remove(preConnId,
					userId);
		}

		// 2.send history notices
		if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(pretstr)) {
			long prets = Long.parseLong(pretstr);
			IPacketNoticeHandler historyNoticeHandler = TPushServerBeanFactory
					.getBean("IPacketNoticeHandler", IPacketNoticeHandler.class);
			historyNoticeHandler.handle(resource, prets, userId, curConnId);
		}
	}

}
