package com.tongbupan.tpush.client.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tongbupan.tpush.client.TPushClientBeanFactory;
import com.tongbupan.tpush.client.param.TPushClientParam;

public class PushExecutor {

	private ExecutorService executorService;

	private static PushExecutor pushProcessor;

	private PushExecutor() {
		TPushClientParam tpushClientParam = TPushClientBeanFactory
				.getInstance().getBean(TPushClientParam.class.getSimpleName(),
						TPushClientParam.class);
		executorService = Executors.newFixedThreadPool(tpushClientParam
				.getThreadCount());
	}

	public static PushExecutor getInstance() {
		if (pushProcessor == null) {
			synchronized (PushExecutor.class) {
				if (pushProcessor == null) {
					pushProcessor = new PushExecutor();
				}
			}
		}
		return pushProcessor;
	}

	public void execute(Runnable task) {
		executorService.execute(task);
	}

}
