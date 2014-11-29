package com.tongbupan.tpush;

import java.util.concurrent.BlockingQueue;

import com.tongbupan.tpush.pusher.Pusher;
import com.tongbupan.tpush.util.NoticeMaster;

public class PusherWorker implements Runnable{
	private BlockingQueue<String> noticeQueue;
	private Pusher pusher;
	
	public PusherWorker(Pusher pusher,BlockingQueue<String> noticeQueue){
		this.noticeQueue = noticeQueue;
		this.pusher = pusher;
	}
	
	public void addNoticeQueue(BlockingQueue<String> noticeQueue){
		this.noticeQueue = noticeQueue;
	}
	
	public void setMessengerPubSub(Pusher pusher){
		this.pusher = pusher;
	}
	
	public Notice extract(String n){
		
//		Class<Message> messageType = n.getMessageType();
//		Message msg = MessageFactory.getMessage(messageType);
//		msg.loadFromNotice(n);
//		return msg;
		return NoticeMaster.parse(n);
	}
	
	public void run(){
		String noticeString;
		Notice notice;
		for(;;){
			try {
				noticeString = noticeQueue.take();
				notice = extract(noticeString);
				pusher.push(notice.getTopic(), notice);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
