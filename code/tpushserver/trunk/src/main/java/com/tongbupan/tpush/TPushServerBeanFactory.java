package com.tongbupan.tpush;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.emacle.foundation.config.GlobalConfigManager;
import com.tongbupan.tpush.param.TPushServerParam;

public class TPushServerBeanFactory {

	private static final Logger logger = Logger
			.getLogger(TPushServerBeanFactory.class);

	private static ApplicationContext tpushContext;

	private TPushServerBeanFactory() {
		String webPath = TPushServerBeanFactory.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		logger.info(webPath);

		GlobalConfigManager instance = GlobalConfigManager.getInstance();

		int index = webPath.indexOf("/WEB-INF/");
		if (index < 0) {
			index = webPath.indexOf("/lib/");
		}
		if (index < 0) {
			index = webPath.length() - 1;
		}

		// 1.init verify redis
		StringBuilder path = new StringBuilder().append(
				webPath.substring(0, index)).append(
				"/conf/storage-global.properties");
		instance.init(path.toString());
		logger.info("verify redis host:" + instance.getRedisHost());
		logger.info("verify redis port:" + instance.getRedisPort());
		logger.info("path of storage-global.properties is:" + path.toString());

		// 2.init spring xml
		path = new StringBuilder("file:").append(webPath.substring(0, index))
				.append("/conf/tpush-*-config.xml");
		logger.info("path of *-config.xml is:" + path.toString());
		tpushContext = new FileSystemXmlApplicationContext(path.toString());

		TPushServerParam tpushServerParam = tpushContext.getBean("tpushParam",
				TPushServerParam.class);
		logger.info("tpushParam.topicExpireTime:"
				+ tpushServerParam.getTopicExpireTime());
		logger.info("tpushParam.historyMessagePushCount:"
				+ tpushServerParam.getHistoryMessagePushCount());
	}

	private static TPushServerBeanFactory factory;

	private static void init() {
		if (factory == null) {
			synchronized (TPushServerBeanFactory.class) {
				if (factory == null) {
					factory = new TPushServerBeanFactory();
				}
			}
		}
	}

	public static <T> T getBean(String beanName, Class<T> cla) {
		init();
		return tpushContext.getBean(beanName, cla);
	}

	public static void main(String[] args) {
	}

}
