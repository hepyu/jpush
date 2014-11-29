package com.tongbupan.tpush.communication.connection;

import javax.servlet.http.HttpServletRequest;
import com.tongbupan.tpush.communication.cenum.Transport;

public interface TPushConnection {

	public static String TOPIC_KEY = "topic";

	public String getUserId();

	public String getConnectionId();

	public String[] getTopics();

	public void setTopics(String[] topics);

	public String getUserAgent();

	public Transport transport();

	public void sendMessage(String message);

	public void processAfterOpen();

	public void close();

	public boolean isOpen();

	public void configRequest(HttpServletRequest req);

}
