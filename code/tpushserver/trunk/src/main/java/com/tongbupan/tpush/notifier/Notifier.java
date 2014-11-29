package com.tongbupan.tpush.notifier;

import java.util.concurrent.BlockingQueue;

public interface Notifier {

	public void addQueue(BlockingQueue<String> queue);

	public void setChannel(String channel);

	public void listen();

	public void close();
}
