package com.tongbupan.tpush.communication.config;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tongbupan.tpush.communication.config.bean.TopicBean;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.communication.topic.center.TopicCenter;

public class CommunicationBeanConfig {

	private static final Logger logger = Logger
			.getLogger(CommunicationBeanConfig.class);

	private static ApplicationContext communicationContext;

	private CommunicationBeanConfig() {
		String webPath = CommunicationBeanConfig.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		logger.info(webPath);

		int index = webPath.indexOf("/WEB-INF/");
		if (index < 0) {
			index = webPath.indexOf("/lib/");
		}
		if (index < 0) {
			index = webPath.length() - 1;
		}

		StringBuilder path = new StringBuilder("file:").append(
				webPath.substring(0, index)).append(
				"/conf/communication-config.xml");
		logger.info("path of communication-config-plugIn.xml is:"
				+ path.toString());

		communicationContext = new FileSystemXmlApplicationContext(
				path.toString());
	}

	private static CommunicationBeanConfig beanConfig;

	public static void init() {
		if (beanConfig == null) {
			synchronized (CommunicationBeanConfig.class) {
				if (beanConfig == null) {
					beanConfig = new CommunicationBeanConfig();
				}
			}
		}
	}

	public static TPushConnection getConnectionBean() {
		return communicationContext
				.getBean("Connection", TPushConnection.class);
	}

	public static TopicBean getTopicBean() {
		return communicationContext.getBean("TopicBean", TopicBean.class);
	}

	public static TopicCenter getTopicCenterBean() {
		return communicationContext.getBean("TopicCenter", TopicCenter.class);
	}

}
