package com.tongbupan.tpush.message;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.log4j.Logger;
import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.exception.TPushServerException;
import com.tongbupan.tpush.message.util.MessageTimestampUtil;
import com.tongbupan.tpush.param.TPushServerParam;
import com.tongbupan.tpush.util.StringUtil;

public class MessageContainer {

	private static final Logger logger = Logger
			.getLogger(MessageContainer.class);

	private static ConcurrentSkipListMap<Long, String> messageStatusMap;

	private static MessageContainer container;

	private static MessageContainerClearThread clearThread;

	private MessageContainer() {
		messageStatusMap = new ConcurrentSkipListMap<Long, String>();
		clearThread = new MessageContainerClearThread();
		clearThread.start();
		logger.info("clearThread has started.");
	}

	public static void init() {
		if (container == null) {
			synchronized (MessageContainer.class) {
				if (container == null) {
					container = new MessageContainer();
				}
			}
		}
	}

	public static String put(Long timestamp, String value)
			throws TPushServerException {
		if (StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(value)) {
			throw new TPushServerException("error key or value, exist empty.");
		}
		return messageStatusMap.put(timestamp, value);
	}

	public static String get(Long timestamp) {
		return messageStatusMap.get(timestamp);
	}

	public static String remove(Long timestamp) {
		return messageStatusMap.remove(timestamp);
	}

	static class MessageContainerClearThread extends Thread {

		private static final Logger logger = Logger
				.getLogger(MessageContainerClearThread.class);

		private static MessageContainerClearThread thread;

		private MessageContainerClearThread() {
		}

		public static MessageContainerClearThread getInstance() {
			if (thread == null) {
				synchronized (MessageContainerClearThread.class) {
					if (thread == null) {
						thread = new MessageContainerClearThread();
					}
				}
			}
			return thread;
		}

		@Override
		public void run() {
			long intervalSecond = TPushServerBeanFactory.getBean("tpushParam",
					TPushServerParam.class).getMessageExpireTime();
			while (true) {
				try {
					long thresholdts = MessageTimestampUtil.getThresholdts();

					ConcurrentNavigableMap<Long, String> subMap = MessageContainer.messageStatusMap
							.subMap(0l, thresholdts);

					long count = 0;
					for (Long key : subMap.keySet()) {
						count++;
						logger.info(new StringBuilder(
								"clear message flag,msgId:").append(key)
								.toString());
						subMap.remove(key);
					}

					logger.info(new StringBuilder(
							"MessageContainer clear finished.map.deleted.count[")
							.append(count).append("]"));

					Thread.sleep(intervalSecond * 1000);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}
