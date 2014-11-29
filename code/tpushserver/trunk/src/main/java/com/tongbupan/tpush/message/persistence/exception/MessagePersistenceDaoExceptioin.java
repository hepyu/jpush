package com.tongbupan.tpush.message.persistence.exception;

public class MessagePersistenceDaoExceptioin extends Exception {

	private static final long serialVersionUID = 670051680296868309L;

	public MessagePersistenceDaoExceptioin() {
		super();
	}

	public MessagePersistenceDaoExceptioin(String msg) {
		super(msg);
	}

	public MessagePersistenceDaoExceptioin(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
