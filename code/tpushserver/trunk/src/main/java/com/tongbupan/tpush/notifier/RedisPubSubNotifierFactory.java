package com.tongbupan.tpush.notifier;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;
import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.param.TPushServerRedisParam;

public class RedisPubSubNotifierFactory extends NotifierFactory {

	private static final Logger logger = Logger
			.getLogger(RedisPubSubNotifierFactory.class);

	private TPushServerRedisParam serverPubsubRedisParam;

	public void init() {
		if (serverPubsubRedisParam.isOpenPush()) {
			JedisFactory.getJedisPool(serverPubsubRedisParam.getHost(),
					serverPubsubRedisParam.getPort(),
					serverPubsubRedisParam.getPushMaxActive(),
					serverPubsubRedisParam.getPushMaxWait(),
					serverPubsubRedisParam.getPushMaxIdle());
			logger.info("serverPubsubRedisParam open notify.");
			logger.info("tpush.redis.host:" + serverPubsubRedisParam.getHost());
			logger.info("tpush.redis.port:" + serverPubsubRedisParam.getPort());
			logger.info("tpush.redis.maxActive:"
					+ serverPubsubRedisParam.getPushMaxActive());
			logger.info("tpush.redis.maxWait:"
					+ serverPubsubRedisParam.getPushMaxWait());
			logger.info("tpush.redis.maxIdle:"
					+ serverPubsubRedisParam.getPushMaxIdle());
		}
	}

	@Override
	public Notifier getNotifier(String channel, BlockingQueue<String> queue) {
		RedisPubSubNotifier notifier = new RedisPubSubNotifier();
		notifier.setChannel(channel);
		notifier.addQueue(queue);
		return notifier;
	}

	public TPushServerRedisParam getServerPubsubRedisParam() {
		return serverPubsubRedisParam;
	}

	public void setServerPubsubRedisParam(
			TPushServerRedisParam serverPubsubRedisParam) {
		this.serverPubsubRedisParam = serverPubsubRedisParam;
	}

}
