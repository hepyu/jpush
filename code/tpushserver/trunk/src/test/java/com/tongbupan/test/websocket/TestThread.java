package com.tongbupan.test.websocket;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class TestThread extends Thread {

	private static final Logger logger = Logger.getLogger(TestThread.class);

	private int size;
	private String flag;
	private String url;

	private static final AtomicLong counter = new AtomicLong(0);

	// 0:running;1:stop
	public static volatile int STATE = 0;
	public static final int STATE_RUNNING = 0;
	public static final int STATE_STOP = 1;

	public static long getHttpRequestNum() {
		return counter.longValue();
	}

	public TestThread(int size, String flag, String url) {
		this.size = size;
		this.flag = flag;
		this.url = url;
	}

	public void run() {
		HttpClient hc = null;
		PostMethod postMethod = null;
		NameValuePair[] data = null;

		logger.info("begin test");

		while (true) {
			if (TestThread.STATE == STATE_STOP) {
				break;
			}

			int index = (int) (Math.random() * size);
			hc = new HttpClient();

			// hc向业务web发起业务请求,use post type
			postMethod = new PostMethod(url);
			data = new NameValuePair[1];
			data[0] = new NameValuePair("userId", flag + index);
			postMethod.setRequestBody(data);
			// logger.info("threadtest send http request");

			try {
				// hc.setConnectionTimeout(1000 * 1000 * 1000);
				// hc.setTimeout(1000 * 1000 * 1000);
				hc.executeMethod(postMethod);
				counter.addAndGet(1);
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
		}
	}
}
