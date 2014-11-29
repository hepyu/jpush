package com.tongbupan.com.logfecher.tpush;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import com.emacle.foundation.param.RedisParam;
import com.emacle.foundation.redis.JedisFactory;
import com.tongbupan.tpush.Notice;
import com.tongbupan.tpush.exception.TPushClientException;
import com.tongbupan.tpush.partitioner.DefaultPartitioner;
import com.tongbupan.tpush.partitioner.TopicPartitioner;

public class TPushClient {

	private static final Logger logger = Logger.getLogger(TPushClient.class);

	private static Properties properties;

	public static void init() {
		if (properties != null) {
			return;
		} else {
			synchronized (TPushClient.class) {
				if (properties != null) {
					return;
				} else {

					String webPath = TPushClient.class.getProtectionDomain()
							.getCodeSource().getLocation().getPath();
					logger.info(webPath);

					int index = webPath.indexOf("/WEB-INF/");
					if (index < 0) {
						index = webPath.indexOf("/lib/");
					}
					if (index < 0) {
						index = webPath.length() - 1;
					}

					// 1.init client-config model
					StringBuilder path = new StringBuilder().append(
							webPath.substring(0, index)).append(
							"/conf/process.properties");
					logger.info("path of process.properties is:"
							+ path.toString());

					properties = new Properties();
					FileInputStream fis;
					try {
						fis = new FileInputStream(path.toString());
						properties.load(fis);

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	public static void publish(Notice notice) throws TPushClientException {

		Jedis jedis = null;
		RedisParam redisParam = new RedisParam();
		redisParam.setHost(properties.getProperty("tpush.redis.host"));
		redisParam.setMaxActive(Integer.parseInt(properties
				.getProperty("tpush.redis.maxActive")));
		redisParam.setMaxIdle(Integer.parseInt(properties
				.getProperty("tpush.redis.maxIdle")));
		redisParam.setMaxWait(Integer.parseInt(properties
				.getProperty("tpush.redis.maxWait")));
		redisParam.setPort(Integer.parseInt(properties
				.getProperty("tpush.redis.port")));

		try {
			jedis = JedisFactory.getInstance(redisParam);
			publish2channel(jedis, notice.getTopic(), notice.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new TPushClientException(e.getMessage(), e);
		} finally {
			JedisFactory.returnResource(redisParam.getHost(),
					redisParam.getPort(), jedis);
		}

	}

	public static void publish2channel(Jedis jedis, String topic, String msg) {
		TopicPartitioner topicPartitioner = new DefaultPartitioner();
		topicPartitioner.setChannelNum(Integer.parseInt(properties
				.getProperty("tpush.channelNum")));
		String channel = topicPartitioner.getChannel(topic);
		jedis.publish(channel, msg);
	}
}
