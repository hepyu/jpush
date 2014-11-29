package com.tongbupan.tpush.message.persistence.exception;

public class MessagePersistenceHandlerExceptioin extends Exception {

	private static final long serialVersionUID = -5041653576311833380L;

	public MessagePersistenceHandlerExceptioin() {
		super();
	}

	public MessagePersistenceHandlerExceptioin(String msg) {
		super(msg);
	}

	public MessagePersistenceHandlerExceptioin(String message, Throwable cause) {
		super(message, cause);
	}
}
