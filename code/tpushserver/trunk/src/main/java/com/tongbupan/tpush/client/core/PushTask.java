package com.tongbupan.tpush.client.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import com.emacle.foundation.param.RedisParam;
import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.Notice;
import com.tongbupan.tpush.client.TPushClientBeanFactory;
import com.tongbupan.tpush.client.param.TPushClientParam;
import com.tongbupan.tpush.message.definition.AbstractBaseMessage;
import com.tongbupan.tpush.message.persistence.MessagePersistenceBeanFactory;
import com.tongbupan.tpush.message.persistence.entity.MessageEntity;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceHandlerExceptioin;
import com.tongbupan.tpush.message.persistence.handler.IMessagePersistenceHandler;
import com.tongbupan.tpush.partitioner.TopicPartitioner;
import com.tongbupan.tpush.util.StringUtil;

public class PushTask implements Runnable {

	private static final Logger logger = Logger.getLogger(PushTask.class);

	private RedisParam redisParam;

	private TPushClientParam tpushClientParam;

	private List<Notice> noticeList;

	private boolean needPersistence;

	private TopicPartitioner topicPartitioner;

	public PushTask(List<Notice> noticeList, boolean needPersistence) {
		this.redisParam = TPushClientBeanFactory.getInstance().getBean(
				"clientTPushRedisParam", RedisParam.class);

		this.tpushClientParam = TPushClientBeanFactory.getInstance().getBean(
				TPushClientParam.class.getSimpleName(), TPushClientParam.class);

		this.topicPartitioner = TPushClientBeanFactory.getInstance().getBean(
				TopicPartitioner.class.getSimpleName(), TopicPartitioner.class);

		this.noticeList = noticeList;
		this.needPersistence = needPersistence;
	}

	@Override
	public void run() {
		publish();
	}

	// TODO to youhua in the future,two threads for async
	private void publish() {

		// (1).process and send notice
		Jedis jedis = null;
		try {
			jedis = JedisFactory.getInstance(redisParam);

			Notice notice = null;
			Iterator<Notice> iterator = noticeList.iterator();
			while (iterator.hasNext()) {
				notice = iterator.next();
				String topic = notice.getTopic();
				String msg = notice.toString();
				push2queue(jedis, topic, msg);
				publish2channel(jedis, topic, msg);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisFactory.returnResource(redisParam.getHost(),
					redisParam.getPort(), jedis);
		}

		// (2).validate notice whether valid
		validateAndStoreNotice(noticeList, needPersistence);
	}

	private void push2queue(Jedis jedis, String topic, String msg) {
		jedis.lpush(topic, msg);
		jedis.expire(topic, tpushClientParam.getTopicExpireTime());
	}

	private void publish2channel(Jedis jedis, String topic, String msg) {
		String channel = topicPartitioner.getChannel(topic);
		jedis.publish(channel, msg);
	}

	private void validateAndStoreNotice(List<Notice> noticeList,
			boolean needPersistence) {

		if (!needPersistence) {
			return;
		}

		Notice notice = null;
		Iterator<Notice> iterator = noticeList.iterator();

		List<MessageEntity> messageEntityList = new ArrayList<MessageEntity>();
		MessageEntity messageEntity = null;

		while (iterator.hasNext()) {

			notice = iterator.next();

			// -----------------valid notice --------------------

			// (1).judge notice valid.
			// 1.judge notice.message whether null.
			if (notice == null
					|| notice.get(Notice.KEY_MESSAGE_CONTENT) == null) {
				logger.error("message is empty.");
			}
			// 2.judge notice.timestamp whether valid.
			if (notice.getTimestamp() <= 0) {
				logger.error("notice.timestamp is invalid.value is:"
						+ notice.getTimestamp());
			}

			// (2).judge notice.message valid.
			JSONObject jsonobj = JSONObject.fromObject(notice
					.get(Notice.KEY_MESSAGE_CONTENT));
			String messagets_str = jsonobj
					.getString(AbstractBaseMessage.FIELD_TIMESTAMP);
			if (messagets_str == null) {
				logger.error("notice.message.timestamp is invalid.value is:"
						+ messagets_str);
			}
			long messagets = Long.valueOf(messagets_str);
			if (messagets <= 0) {
				logger.error("notice.message.timestamp is invalid.value is:"
						+ messagets_str);
			}

			// (3).judge notice.timestamp eq notice.message.timestamp
			if (notice.getTimestamp() != messagets) {
				logger.error(new StringBuilder(
						"notice.message.timestamp is not eq with notice.timestamp. notice.message.timestamp[")
						.append(messagets).append("] notice.timestamp[")
						.append(notice.getTimestamp()).append("]").toString());
			}

			// (4).judge notice.message.msgid is valid.
			Object msgId = jsonobj.get(AbstractBaseMessage.FIELD_MSG_ID);
			if (StringUtil.isEmpty(msgId)) {
				logger.error("notice.message.msgId is empty, its invalid.");
			}

			// (5).judge notice.message.version is valid.
			Object version = jsonobj.get(AbstractBaseMessage.FIELD_VERSION);
			if (StringUtil.isEmpty(version)) {
				logger.error("notice.message.version is empty, its invalid.");
			}

			String timestampstr = jsonobj
					.getString(AbstractBaseMessage.FIELD_TIMESTAMP);

			messageEntity = new MessageEntity();
			messageEntity.setMsgBody((String) notice
					.get(Notice.KEY_MESSAGE_CONTENT));
			messageEntity.setMsgId(jsonobj
					.getString(AbstractBaseMessage.FIELD_MSG_ID));
			messageEntity.setTimestamp(Long.valueOf(timestampstr).longValue());
			messageEntity.setType(jsonobj
					.getString(AbstractBaseMessage.FIELD_TYPE));
			messageEntity.setUserId(notice.getOwnerId());

			messageEntityList.add(messageEntity);

			// ------------ store message-------------//

			if (!messageEntityList.isEmpty()
					&& messageEntityList.size() == tpushClientParam
							.getBatchSubmitCount()) {
				storeMessage(messageEntityList);
				messageEntityList.clear();

				try {
					Thread.sleep(tpushClientParam
							.getBatchSleepBeforeNextBatch());
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		storeMessage(messageEntityList);
	}

	private void storeMessage(List<MessageEntity> messageEntityList) {
		try {
			IMessagePersistenceHandler handler = MessagePersistenceBeanFactory
					.getBean("IMessagePersistenceHandler",
							IMessagePersistenceHandler.class);

			handler.save(messageEntityList);
		} catch (MessagePersistenceHandlerExceptioin e) {
			logger.error(e.getMessage(), e);
		}
	}
}
