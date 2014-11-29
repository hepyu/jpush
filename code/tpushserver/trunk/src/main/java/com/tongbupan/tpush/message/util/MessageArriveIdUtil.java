package com.tongbupan.tpush.message.util;

import org.apache.log4j.Logger;
import com.tongbupan.tpush.message.MessageContainer;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.exception.TPushServerException;
import com.tongbupan.tpush.util.CounterUtil;

public class MessageArriveIdUtil {

	private static final Logger logger = Logger
			.getLogger(MessageArriveIdUtil.class);

	private static long generateMessageSN() {
		return System.nanoTime();
	}

	private static long putMessageContainer(long timestamp, String uuid)
			throws TPushServerException {

		// uuiq implements
		synchronized (MessageArriveIdUtil.class) {
			String obj = MessageContainer.get(timestamp);
			if (obj != null) {
				while (true) {
					timestamp++;
					obj = MessageContainer.get(timestamp);
					if (obj == null) {
						MessageContainer.put(timestamp, uuid);
						break;
					}
				}
			} else {
				MessageContainer.put(timestamp, uuid);
			}
		}
		return timestamp;
	}

	public static long getArriveId(TPushConnection resource) {
		try {
			CounterUtil.getBroadcastresourcecounter().incrementAndGet();

			long timestamp = generateMessageSN();
			return putMessageContainer(timestamp, resource.getConnectionId());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return -1;
	}

}