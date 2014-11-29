package com.tongbupan.tpush.communication.connection;

public abstract class TPushWebSocketConnection extends TPushDefaultConnection {

	public abstract void processAfterOpen();

	public abstract void setTopics(String[] topics);
}
