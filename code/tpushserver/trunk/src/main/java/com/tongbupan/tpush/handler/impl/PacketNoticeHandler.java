package com.tongbupan.tpush.handler.impl;

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import com.emacle.foundation.param.RedisParam;
import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.Notice;
import com.tongbupan.tpush.TPushServerBeanFactory;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.handler.IPacketNoticeHandler;
import com.tongbupan.tpush.message.MessageSender;
import com.tongbupan.tpush.message.menum.PacketType;
import com.tongbupan.tpush.message.persistence.MessagePersistenceBeanFactory;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceHandlerExceptioin;
import com.tongbupan.tpush.message.persistence.handler.IMessagePersistenceHandler;
import com.tongbupan.tpush.message.util.MessageTimestampUtil;
import com.tongbupan.tpush.param.TPushServerParam;

public class PacketNoticeHandler implements IPacketNoticeHandler {

	private static final Logger logger = Logger
			.getLogger(PacketNoticeHandler.class);

	private MessageSender messageSender;

	@Override
	public void handle(TPushConnection connectResource, long prets,
			String userId, String curConnId) {

		TPushServerParam tpushParam = TPushServerBeanFactory.getBean(
				"tpushParam", TPushServerParam.class);
		long topicExpireTime = tpushParam.getMessageExpireTime() * 1000;
		long currentSystemTime = MessageTimestampUtil.getMessageTimestamp();

		try {
			// 1.if prets in expire time, fetch notices from redis.
			if ((currentSystemTime - prets) > 0
					&& (currentSystemTime - prets) < topicExpireTime) {
				sendPacketMessageFromRedis(connectResource, userId, curConnId,
						prets);
			}
			// 2.else if prets invalid, we fetch from mysql
			else if ((currentSystemTime - prets) >= topicExpireTime) {
				sendPacketMessageFromPersistence(connectResource, userId,
						curConnId, prets);
			} else {
				StringBuilder msg = new StringBuilder();
				msg.append("currentSystemTime - prets <0 ,invalid data.")
						.append(" currentSystemTime[")
						.append(currentSystemTime).append("],prets[")
						.append(prets).append("],userId").append(userId)
						.append("]");
				logger.error(msg.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void sendPacketMessageFromPersistence(
			TPushConnection connectResource, String userId, String curConnId,
			long prets) {

		IMessagePersistenceHandler handler = MessagePersistenceBeanFactory
				.getBean("IMessagePersistenceHandler",
						IMessagePersistenceHandler.class);
		TPushServerParam tpushParam = TPushServerBeanFactory.getBean(
				"tpushParam", TPushServerParam.class);

		String str = null;
		String preMsgId = null;
		long lastestts = prets;
		try {
			str = handler.queryJSONResultByPage(userId, lastestts, preMsgId,
					tpushParam.getHistoryMessagePushCount(), true);
			messageSender.sendPacketMessage(connectResource, str,
					PacketType.persistence);
		} catch (MessagePersistenceHandlerExceptioin e) {
			logger.error(e.getMessage(), e);
		}

	}

	private void sendPacketMessageFromRedis(TPushConnection connectResource,
			String userId, String curConnId, long prets) {
		Jedis jedis = null;
		RedisParam redisParam = TPushServerBeanFactory.getBean(
				"clientTPushRedisParam", RedisParam.class);
		TPushServerParam tpushParam = TPushServerBeanFactory.getBean(
				"tpushParam", TPushServerParam.class);

		try {
			jedis = JedisFactory.getInstance(redisParam);

			long len = jedis.llen(userId);
			String noticeStr = null;

			StringBuilder jsonstr = new StringBuilder("[");
			int count = 0;

			for (int index = 0; index < len; index++) {
				try {
					noticeStr = jedis.lindex(userId, index);
					JSONObject obj = JSONObject.fromObject(noticeStr);

					if (noticeStr != null) {
						Long noticets = obj.getLong(Notice.TIMESTAMP);
						if (noticets != null) {
							long ts = noticets.longValue();

							if (ts >= prets) {
								count++;
								jsonstr.append(((JSONObject) obj
										.get(Notice.DATA))
										.getString(Notice.KEY_MESSAGE_CONTENT));
								jsonstr.append(",");
							}
						} else {
							logger.error("noticets error is null." + noticets);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

			if (jsonstr.length() > 1) {
				jsonstr.deleteCharAt(jsonstr.length() - 1);
			}

			jsonstr.append("]");

			// print Ô¤¾¯log
			if (count > tpushParam.getHistoryMessagePushCount()) {
				logger.error(new StringBuilder(
						"notice count in redis is too much, count is:")
						.append(count).append(". userId[").append(userId)
						.toString());
			}

			messageSender.sendPacketMessage(connectResource,
					jsonstr.toString(), PacketType.redis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				JedisFactory.returnResource(redisParam.getHost(),
						redisParam.getPort(), jedis);
			}
		}
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public static void main(String[] args) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb.append(i).append(",");
		}
		System.out.println(sb);

		StringBuilder sb1 = sb.deleteCharAt(sb.length() - 1);

		System.out.println("sb1:" + sb1);

		System.out.println("sb:" + sb);

		System.out.println(sb == sb1);
	}
}
