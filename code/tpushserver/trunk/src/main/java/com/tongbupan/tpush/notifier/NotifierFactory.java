package com.tongbupan.tpush.notifier;

import java.util.concurrent.BlockingQueue;

public abstract class NotifierFactory {

	public abstract Notifier getNotifier(String channel,
			BlockingQueue<String> queue);
}
