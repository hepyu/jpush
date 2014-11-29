package com.tongbupan.tpush;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.param.TPushServerParam;
import com.tongbupan.tpush.param.TPushServerRedisParam;
import com.tongbupan.tpush.exception.TPushClientException;
import com.tongbupan.tpush.notifier.NotifierFactory;
import com.tongbupan.tpush.notifier.RedisPubSubNotifierFactory;
import com.tongbupan.tpush.partitioner.TopicPartitioner;
import com.tongbupan.tpush.pusher.Pusher;

public class TPushServer {

	private static final Logger logger = Logger.getLogger(TPushServer.class);

	private BlockingQueue<String> inNoticeQueue = new LinkedBlockingQueue<String>();

	private int pusherWorkerNum;

	@Resource
	private NotifierFactory notifierFactory;

	private TopicPartitioner partitioner;

	private ExecutorService notifierPool;

	private ExecutorService pusherPool;

	private TPushServer() {
		// init server pubsub redis param
		TPushServerRedisParam serverPubsubRedisParam = TPushServerBeanFactory
				.getBean("serverPubsubRedisParam", TPushServerRedisParam.class);
		if (serverPubsubRedisParam.isOpenPush()) {
			JedisFactory.getJedisPool(serverPubsubRedisParam.getHost(),
					serverPubsubRedisParam.getPort(),
					serverPubsubRedisParam.getPushMaxActive(),
					serverPubsubRedisParam.getPushMaxWait(),
					serverPubsubRedisParam.getPushMaxIdle());
			logger.info("serverPubsubRedisParam open push.");
			logger.info("tpush.redis.host:" + serverPubsubRedisParam.getHost());
			logger.info("tpush.redis.port:" + serverPubsubRedisParam.getPort());
			logger.info("tpush.redis.maxActive:"
					+ serverPubsubRedisParam.getPushMaxActive());
			logger.info("tpush.redis.maxWait:"
					+ serverPubsubRedisParam.getPushMaxWait());
			logger.info("tpush.redis.maxIdle:"
					+ serverPubsubRedisParam.getPushMaxIdle());
		}

		TPushServerParam tpushServerParam = TPushServerBeanFactory.getBean(
				"tpushParam", TPushServerParam.class);
		pusherWorkerNum = tpushServerParam.getPusherWorkerNum();
		notifierFactory = new RedisPubSubNotifierFactory();
		partitioner = TPushServerBeanFactory.getBean("TopicPartitioner",
				TopicPartitioner.class);

		notifierPool = Executors
				.newFixedThreadPool(partitioner.getChannelNum() * 2 - 1);
		pusherPool = Executors.newFixedThreadPool(pusherWorkerNum);
	}

	private void setupNotifier() {
		String[] channels = partitioner.getChannels();
		for (String channel : channels) {
			logger.info("channel:" + channel);
			NotifierWorker inqueueWorker = new NotifierWorker(channel,
					inNoticeQueue);
			notifierPool.execute(inqueueWorker);
		}
	}

	private void setupPusher() {
		for (int i = 0; i < pusherWorkerNum; i++) {
			Pusher pusher = TPushServerBeanFactory.getBean("Pusher",
					Pusher.class);
			PusherWorker pusherWorker = new PusherWorker(pusher, inNoticeQueue);
			pusherPool.execute(pusherWorker);
		}

	}

	public void start() {
		this.setupNotifier();
		this.setupPusher();
	}

	private static TPushServer tpushServer;

	public static TPushServer getInstance() {
		if (tpushServer == null) {
			synchronized (TPushServer.class) {
				if (tpushServer == null) {
					tpushServer = new TPushServer();
				}
			}
		}
		return tpushServer;
	}

	public static void main(String[] args) throws TPushClientException {
		// TPushServer server = new TPushServer();
		// server.start();
		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// DefaultNotice notice = new DefaultNotice();
		// notice.setTopic("test@emacle.com");
		// notice.setOwnerId("test@emacle.com");
		// TPushClient.publish(notice, false);
		// try {
		// Thread.currentThread().join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

}
