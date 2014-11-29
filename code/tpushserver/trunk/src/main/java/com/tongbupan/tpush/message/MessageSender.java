package com.tongbupan.tpush.message;

import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.message.menum.PacketType;

public interface MessageSender {

	public void sendConnIdMessage(TPushConnection connection);

	public void sendPacketMessage(TPushConnection connection, String notices,
			PacketType packetType);

	public void sendBusinessMessage(String userId, String topicId,
			String message);
}
