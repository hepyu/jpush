/**
 * 
 */
package com.tongbupan.tpush.message;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;
import com.tongbupan.tpush.communication.config.CommunicationBeanConfig;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.message.definition.AbstractBaseMessage;
import com.tongbupan.tpush.message.definition.ConnIdMessage;
import com.tongbupan.tpush.message.definition.PacketMessage;
import com.tongbupan.tpush.message.menum.PacketType;
import com.tongbupan.tpush.message.util.MessageArriveIdUtil;
import com.tongbupan.tpush.message.util.MessageTimestampUtil;
import com.tongbupan.tpush.util.JSONUtil;
import com.tongbupan.tpush.util.StringUtil;

/**
 * @author hpy
 * 
 */
public class DefaultMessageSender implements MessageSender {

	private static final Logger logger = Logger
			.getLogger(DefaultMessageSender.class);

	@Override
	public void sendConnIdMessage(TPushConnection connection) {
		try {
			ConnIdMessage msg = new ConnIdMessage();
			msg.setAskACK(true);
			msg.setCurConnId(connection.getConnectionId());
			msg.setTimestamp(MessageTimestampUtil.getMessageTimestamp() + "");
			msg.setMsgId(UUID.randomUUID().toString());

			long sn = MessageArriveIdUtil.getArriveId(connection);
			msg.setArriveId(sn + "");

			String str = JSONUtil.getObjectMapperInstance().writeValueAsString(
					msg);

			connection.sendMessage(str);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void sendPacketMessage(TPushConnection connection, String notices,
			PacketType packetType) {
		if (StringUtil.isEmpty(notices) || notices.trim().equals("[]")) {
			return;
		}

		try {
			PacketMessage packetMessage = new PacketMessage();

			packetMessage.setCurConnId(connection.getConnectionId());
			packetMessage.setPacketType(packetType);
			packetMessage.setMsgId(UUID.randomUUID().toString());

			long ts = MessageArriveIdUtil.getArriveId(connection);
			if (ts > 0) {
				packetMessage.setArriveId(ts + "");

				packetMessage.setTimestamp(MessageTimestampUtil
						.getMessageTimestamp() + "");

				JSONObject jsonobj = JSONObject.fromObject(packetMessage);
				jsonobj.put(PacketMessage.FIELD_DATA, notices);
				jsonobj.put(PacketMessage.FIELD_CLASS,
						PacketMessage.class.getSimpleName());

				connection.sendMessage(jsonobj.toString());
			} else {
				// TODO
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void sendBusinessMessage(String userId, String topicId,
			String message) {
		Set<TPushConnection> connectionList = CommunicationBeanConfig
				.getTopicCenterBean().getConnections(userId, topicId);
		if (connectionList == null || connectionList.isEmpty()) {
			// only logic route.
		} else {
			for (TPushConnection connection : connectionList) {
				send(connection, message);
			}
		}
	}

	public void sendBusinessMessage(TPushConnection connection, String message) {
		send(connection, message);
	}

	private void send(TPushConnection connection, String message) {
		// 1.build formed-message
		JSONObject jsonobj = JSONObject.fromObject(message);

		jsonobj.put(AbstractBaseMessage.FIELD_CUR_CONN_ID,
				connection.getConnectionId());
		jsonobj.put(AbstractBaseMessage.FIELD_ASK_ACK, true);

		long ts = MessageArriveIdUtil.getArriveId(connection);
		if (ts > 0) {
			jsonobj.put(AbstractBaseMessage.FIELD_ARRIVE_ID, ts + "");
			// TODO to be modifyed
			jsonobj.put(AbstractBaseMessage.FIELD_VERSION, "0.1.1");

			// 3.send
			connection.sendMessage(message);
		} else {
			// TODO
		}
	}

}
