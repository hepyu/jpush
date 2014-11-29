package com.tongbupan.tpush.pusher;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import com.emacle.foundation.param.RedisParam;
import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.Notice;
import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.message.MessageSender;
import com.tongbupan.tpush.message.definition.ACKMessage;
import com.tongbupan.tpush.message.definition.AbstractBaseMessage;
import com.tongbupan.tpush.param.TPushServerParam;
import com.tongbupan.tpush.util.CounterUtil;

public class DefaultPusher implements Pusher {

	public static final Logger logger = Logger.getLogger(DefaultPusher.class);

	private MessageSender messageSender;

	public void push(String topic, Notice notice) {

		CounterUtil.getRedisnoticecounter().incrementAndGet();

		String jsonstr = notice.get("content");
		Jedis jedis = null;
		RedisParam clientRedisParam = TPushServerBeanFactory.getBean(
				"clientTPushRedisParam", RedisParam.class);

		try {
			jedis = JedisFactory.getInstance(clientRedisParam);

			JSONObject jsonobj = JSONObject.fromObject(jsonstr);
			String realTopic = jsonobj
					.getString(AbstractBaseMessage.FIELD_TYPE);

			messageSender.sendBusinessMessage(notice.getOwnerId(), realTopic,
					jsonstr);

			processAfterSendMessage(jedis, notice.getOwnerId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				JedisFactory.returnResource(clientRedisParam.getHost(),
						clientRedisParam.getPort(), jedis);
			}
		}
	}

	private void processAfterSendMessage(Jedis jedis, String topicId) {
		// 1.update expire time
		jedis.expire(
				topicId,
				TPushServerBeanFactory.getBean("tpushParam",
						TPushServerParam.class).getTopicExpireTime());
	}

	public static void main(String args[]) {
		JSONObject jsonObj = JSONObject.fromObject(new ACKMessage());

		for (int i = 0; i < 5; i++) {
			jsonObj.put(AbstractBaseMessage.FIELD_CUR_CONN_ID, i);
			System.out.println(jsonObj.toString());
		}
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

}