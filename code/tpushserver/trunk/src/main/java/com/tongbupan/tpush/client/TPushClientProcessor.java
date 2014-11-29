package com.tongbupan.tpush.client;

import java.util.List;
import org.apache.log4j.Logger;
import com.tongbupan.tpush.Notice;
import com.tongbupan.tpush.client.core.PushExecutor;
import com.tongbupan.tpush.client.core.PushTask;

public class TPushClientProcessor {

	private static final Logger logger = Logger
			.getLogger(TPushClientProcessor.class);

	private static TPushClientProcessor tpushClient;

	public static TPushClientProcessor getInstance() {
		if (tpushClient == null) {
			synchronized (TPushClientProcessor.class) {
				if (tpushClient == null) {
					tpushClient = new TPushClientProcessor();
				}
			}
		}
		return tpushClient;
	}

	public void publish(List<Notice> noticeList, boolean needPersistence) {
		PushTask task = new PushTask(noticeList, needPersistence);
		PushExecutor.getInstance().execute(task);
	}
}
