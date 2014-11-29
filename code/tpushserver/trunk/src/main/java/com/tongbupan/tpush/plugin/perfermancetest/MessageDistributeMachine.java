package com.tongbupan.tpush.plugin.perfermancetest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.communication.config.CommunicationBeanConfig;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.communication.topic.center.TopicCenter;
import com.tongbupan.tpush.message.definition.AbstractBaseMessage;
import com.tongbupan.tpush.message.definition.NotifyMessage;
import com.tongbupan.tpush.message.definition.ProtocolMessage;
import com.tongbupan.tpush.message.util.MessageArriveIdUtil;
import com.tongbupan.tpush.message.util.MessageTimestampUtil;
import com.tongbupan.tpush.util.CounterUtil;

public class MessageDistributeMachine extends Thread {

	private static final Logger logger = Logger
			.getLogger(MessageDistributeMachine.class);

	private static MessageDistributeMachine initializer;

	private volatile static boolean isStop = false;

	private String initMessageBody() {
		StringBuilder sb = new StringBuilder("{\"test\":\"");
		for (int i = 0; i < messageSize; i++) {
			sb.append("a");
		}
		sb.append("\"}");
		return sb.toString();
	}

	private String messageBodyStr;

	private int threadCount;

	private int messageSize;

	private int runtime;

	private int connCount;

	public MessageDistributeMachine() {
		PerfermanceTestParamBean qpsTestPlugParam = null;

		try {
			qpsTestPlugParam = TPushServerBeanFactory.getBean(
					"PerfermanceTestParamBean", PerfermanceTestParamBean.class);
		} catch (NoSuchBeanDefinitionException e) {
			logger.error("MessageDistributeMachine is not open.");
			return;
		}

		if (qpsTestPlugParam != null) {
			boolean isopen = qpsTestPlugParam.isOpenThisPlug();

			if (isopen) {
				threadCount = qpsTestPlugParam.getThreadCount();
				messageSize = qpsTestPlugParam.getMessageSize();
				runtime = qpsTestPlugParam.getRuntime();
				connCount = qpsTestPlugParam.getConnCount();

				logger.info("test1.performance.threadCount:" + threadCount);
				logger.info("test1.performance.messageSize:" + messageSize);
				logger.info("test1.performance.runtime:" + runtime);
				logger.info("test1.performance.connCount:" + connCount);

				// 1.init message
				messageBodyStr = initMessageBody();

				// 2.start thread
				logger.info("wait the conn count arrive to " + connCount);
				this.start();
			}
		}
	}

	public static void init() {
		if (initializer != null) {
			return;
		} else {
			synchronized (MessageDistributeMachine.class) {
				if (initializer != null) {
					return;
				} else {
					initializer = new MessageDistributeMachine();
				}
			}
		}
	}

	@Override
	public void run() {

		// 1.wait ws count added to somevalue that configged.
		while (true) {
			int wsAliveCount = CounterUtil.getAlivewsconncounter().intValue();
			if (wsAliveCount == connCount) {
				break;
			}

			try {
				logger.info("ws alive count is not enough, now is:"
						+ wsAliveCount);
				Thread.sleep(60 * 1000);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		// 2.start threads to push.
		Thread t = null;
		for (int i = 0; i < threadCount; i++) {
			t = new Distrbuter();
			t.setName("test" + i);
			t.start();
		}

		// 3.performance-test-timer thread to decide when to close the
		// Distrbuter threads.
		Thread timer = new TimerThread(runtime);
		timer.start();
	}

	class Distrbuter extends Thread {

		private long pushMsgCount;

		@Override
		public void run() {

			TopicCenter topicCenter = CommunicationBeanConfig
					.getTopicCenterBean();
			PerfermanceTestParamBean testParamBean = TPushServerBeanFactory
					.getBean("PerfermanceTestParamBean",
							PerfermanceTestParamBean.class);

			String topic = ProtocolMessage.class.getSimpleName();
			int conncount = testParamBean
					.getTpush_server_websocket_conn_count();
			List<String> nodeFlagList = testParamBean
					.getCurrent_test_client_flag_list();

			String msg = null;

			long startTime = System.currentTimeMillis();
			while (true) {
				try {
					if (isStop) {
						break;
					}

					for (String nodeFlag : nodeFlagList) {
						for (int i = 0; i < conncount; i++) {
							Set<TPushConnection> set = topicCenter
									.getConnections(new StringBuilder(nodeFlag)
											.append(i).toString(), topic);
							if (set == null || set.isEmpty()) {
								logger.info(new StringBuilder(
										"user's connections are not exist:")
										.append(nodeFlag).append(i).toString());
							} else if (set.size() > 1) {
								logger.info(new StringBuilder(
										"user's connections count >1:")
										.append(nodeFlag).append(i).toString());
							} else {
								if (!isStop) {
									TPushConnection connection = set.iterator()
											.next();
									msg = getMessage(connection);
									connection.sendMessage(msg);
								} else {
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			long endTime = System.currentTimeMillis();

			logger.info(new StringBuilder("TestThread[").append(this.getName())
					.append("]").append("finished. runtime:")
					.append((endTime - startTime) / 1000)
					.append(", pushMsgCount:").append(pushMsgCount));

		}

		private String getMessage(TPushConnection connection) {
			long timestamp = MessageTimestampUtil.getMessageTimestamp();

			NotifyMessage msg = new NotifyMessage();
			msg.setTimestamp(timestamp + "");
			msg.setMsgId(UUID.randomUUID().toString());

			JSONObject jsonobj = JSONObject.fromObject(msg);
			jsonobj.put("testData", messageBodyStr);
			jsonobj.put(AbstractBaseMessage.FIELD_TYPE,
					NotifyMessage.class.getSimpleName());

			jsonobj.put(AbstractBaseMessage.FIELD_CUR_CONN_ID,
					connection.getConnectionId());
			jsonobj.put(AbstractBaseMessage.FIELD_ASK_ACK, true);

			long ts = MessageArriveIdUtil.getArriveId(connection);
			jsonobj.put(AbstractBaseMessage.FIELD_ARRIVE_ID, ts + "");

			return jsonobj.toString();
		}
	}

	class TimerThread extends Thread {

		private int runtime;

		public TimerThread(int runtime) {
			this.runtime = runtime;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(runtime * 60 * 1000);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			isStop = true;
		}
	}

}
