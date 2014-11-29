package com.tongbupan.tpush.message.util;

import org.apache.log4j.Logger;

import com.tongbupan.tpush.communication.connection.TPushConnection;

public class MessageLogUtil {

	public static void info(String msg, TPushConnection connection,
			Logger logger) {
		try {
			String userAgent = connection.getUserAgent();
			if (userAgent != null) {
				userAgent = userAgent.replaceAll(" ", ",");
			}
			logger.info(new StringBuffer("\"User-Agent\":\"").append(userAgent)
					.append("\",").append(msg.replaceAll("\n\r", ""))
					.append(",connId:").append(connection.getConnectionId()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
