package com.tongbupan.tpush.notifier;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.emacle.foundation.param.RedisParam;
import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.TPushServerBeanFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisPubSubNotifier implements Notifier {

	private Jedis client;
	private String channel;
	private RedisPubSubLisenter lisenter;
	private BlockingQueue<String> queue;

	private static RedisParam redisParam;

	static {
		redisParam = TPushServerBeanFactory.getBean("clientTPushRedisParam",
				RedisParam.class);
	}

	public void createClient() throws IOException {
		client = JedisFactory.getInstance(redisParam);
	}

	public void addQueue(BlockingQueue<String> queue) {
		this.queue = queue;
		this.lisenter = new RedisPubSubLisenter();
		this.lisenter.addQueue(queue);

	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void listen() {
		while (true)
			try {
				createClient();
				client.subscribe(lisenter, channel);
			} catch (Exception e) {
				JedisFactory.returnBrokenResource(redisParam.getHost(),
						redisParam.getPort(), client);
			}
	}

	public void close() {
		JedisFactory.returnResource(redisParam.getHost(), redisParam.getPort(),
				client);
	}

	public static class RedisPubSubLisenter extends JedisPubSub {
		private BlockingQueue<String> queue;

		public void addQueue(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		@Override
		public void onMessage(String channel, String message) {
			// TODO Auto-generated method stub
			queue.add(message);
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			// TODO Auto-generated method stub

		}

	}

}
