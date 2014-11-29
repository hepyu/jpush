package com.tongbupan.tpush.exception;

public class TPushServerException extends Exception {

	private static final long serialVersionUID = 5634634734209715765L;

	public TPushServerException() {
		super();
	}

	public TPushServerException(String msg) {
		super(msg);
	}

	public TPushServerException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
