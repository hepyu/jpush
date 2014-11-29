package com.tongbupan.tpush.client;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tongbupan.tpush.message.persistence.MessagePersistenceBeanFactory;

public class TPushClientBeanFactory {

	private static final Logger logger = Logger
			.getLogger(TPushClientBeanFactory.class);

	private ApplicationContext tpushClientContext;

	private static TPushClientBeanFactory tpushClientBeanFactory;

	private TPushClientBeanFactory() {
		String webPath = TPushClientProcessor.class.getProtectionDomain()
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
		StringBuilder path = new StringBuilder("file:").append(
				webPath.substring(0, index)).append(
				"/conf/tpush-client-params-config.xml");
		logger.info("path of tpush-client-params-config.xml is:"
				+ path.toString());
		tpushClientContext = new FileSystemXmlApplicationContext(
				path.toString());
		// 2.init message-persistence model
		MessagePersistenceBeanFactory.init();
		logger.info("MessagePersistenceInitializer init finished.");
	}

	public static TPushClientBeanFactory getInstance() {
		if (tpushClientBeanFactory == null) {
			synchronized (TPushClientProcessor.class) {
				if (tpushClientBeanFactory == null) {
					tpushClientBeanFactory = new TPushClientBeanFactory();
				}
			}
		}
		return tpushClientBeanFactory;
	}

	public <T> T getBean(String beanName, Class<T> cla) {
		return tpushClientContext.getBean(beanName, cla);
	}
}
