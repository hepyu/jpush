package com.tongbupan.tpush;

import java.util.concurrent.BlockingQueue;
import com.tongbupan.tpush.notifier.Notifier;
import com.tongbupan.tpush.notifier.NotifierFactory;

public class NotifierWorker implements Runnable {

	private BlockingQueue<String> noticeQueue;
	private Notifier notifier;
	private String channel;

	public NotifierWorker(String channel, BlockingQueue<String> noticeQueue) {
		this.channel = channel;
		this.noticeQueue = noticeQueue;
	}

	/**
	 * 
	 * @param noticeQueue
	 */
	public void addNoticeQueue(BlockingQueue<String> noticeQueue) {
		this.noticeQueue = noticeQueue;
	}

	/**
	 * 
	 * @param notifier
	 */
	public void addNotifier(Notifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * accept the notifier and add notice to the queue
	 */
	public void run() {
		notifier = TPushServerBeanFactory.getBean("NotifierFactory",
				NotifierFactory.class).getNotifier(channel, noticeQueue);
		notifier.listen();
	}

}
