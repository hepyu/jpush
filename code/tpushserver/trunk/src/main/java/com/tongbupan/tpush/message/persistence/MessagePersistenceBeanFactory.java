package com.tongbupan.tpush.message.persistence;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tongbupan.tpush.TPushServerBeanFactory;

public class MessagePersistenceBeanFactory {

	private static final Logger logger = Logger
			.getLogger(MessagePersistenceBeanFactory.class);

	private static MessagePersistenceBeanFactory initializer;

	private static ApplicationContext persistenceContext;

	private MessagePersistenceBeanFactory() {

		String webPath = TPushServerBeanFactory.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		logger.info(webPath);

		int index = webPath.indexOf("/WEB-INF/");
		if (index < 0) {
			index = webPath.indexOf("/lib/");
		}
		if (index < 0) {
			index = webPath.length() - 1;
		}

		// 1.init mongo config
		StringBuilder path = new StringBuilder("file:").append(
				webPath.substring(0, index)).append(
				"/conf/tpush-message-persistence-config.xml");
		logger.info("path of stat-config.xml is:" + path.toString());

		persistenceContext = new FileSystemXmlApplicationContext(
				path.toString());

	}

	public static void init() {
		if (initializer != null) {
			return;
		} else {
			synchronized (MessagePersistenceBeanFactory.class) {
				if (initializer != null) {
					return;
				} else {
					initializer = new MessagePersistenceBeanFactory();
				}
			}
		}
	}

	public static <T> T getBean(String beanName, Class<T> cla) {
		return persistenceContext.getBean(beanName, cla);
	}
}
