package com.tongbupan.tpush.message.util;

import org.apache.log4j.Logger;

import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.param.TPushServerParam;

public class MessageTimestampUtil {
	private static final Logger logger = Logger
			.getLogger(MessageTimestampUtil.class);

	private static long generateMessageSN() {
		return System.nanoTime();
	}

	public static long getThresholdts() {
		long intervalSecond = TPushServerBeanFactory.getBean("tpushParam",
				TPushServerParam.class).getMessageExpireTime();
		long expireTime = intervalSecond * 1000 * 1000 * 1000;

		long currentTime = generateMessageSN();
		long thresholdts = currentTime - expireTime;

		logger.info(new StringBuilder(
				"MessageContainer clear begin. startTime[").append(currentTime)
				.append("],expireTime[").append(expireTime)
				.append("],thresholdts[").append(thresholdts).append("]")
				.toString());

		return thresholdts;
	}

	public static long getMessageTimestamp() {
		return System.currentTimeMillis();
	}
}
