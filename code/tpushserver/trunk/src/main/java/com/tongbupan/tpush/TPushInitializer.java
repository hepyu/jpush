package com.tongbupan.tpush;

import org.apache.log4j.Logger;

import com.tongbupan.tpush.communication.config.CommunicationBeanConfig;
import com.tongbupan.tpush.message.MessageContainer;
import com.tongbupan.tpush.message.persistence.MessagePersistenceBeanFactory;
import com.tongbupan.tpush.plugin.perfermancetest.MessageDistributeMachine;

public class TPushInitializer {

	private static final Logger logger = Logger
			.getLogger(TPushInitializer.class);

	private static TPushInitializer initializer;

	private static TPushServer tserver;

	private static final String beatString = "b";

	private TPushInitializer() {

		// 1.init tpush server
		try {
			tserver = TPushServer.getInstance();
			tserver.start();
			logger.info("TPushServer init finished.");
		} catch (Exception e) {
			logger.error("TPushServer init error.", e);
		}

		// 2.init plugIns
		initPlugIns();
	}

	private void initPlugIns() {

		// 2.init MessagePersistence plugin
		try {
			MessagePersistenceBeanFactory.init();
			logger.info("MessagePersistenceInitializer init finished.");
		} catch (Exception e) {
			logger.error("MessagePersistenceInitializer init error.", e);
		}

		CommunicationBeanConfig.init();

		MessageContainer.init();

		MessageDistributeMachine.init();

	}

	public static void init() {
		if (initializer == null) {
			synchronized (TPushInitializer.class) {
				if (initializer == null) {
					initializer = new TPushInitializer();
				}
			}
		}
	}

	public static String getBeatString() {
		return beatString;
	}

}
