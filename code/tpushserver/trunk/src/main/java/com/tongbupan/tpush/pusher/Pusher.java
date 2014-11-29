package com.tongbupan.tpush.pusher;

import com.tongbupan.tpush.Notice;


public interface Pusher {

	/**
	 * 
	 * @param topic
	 * @param msg
	 */
	public void push(String topic,Notice notice);
}
