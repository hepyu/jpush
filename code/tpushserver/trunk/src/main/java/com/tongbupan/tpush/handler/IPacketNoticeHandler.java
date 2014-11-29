package com.tongbupan.tpush.handler;

import com.tongbupan.tpush.communication.connection.TPushConnection;

public interface IPacketNoticeHandler extends ITPushHandler {

	public void handle(TPushConnection connectResource, long prets,
			String userId, String curConnId);
}
