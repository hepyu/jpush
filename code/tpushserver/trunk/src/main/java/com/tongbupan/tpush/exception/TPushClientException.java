package com.tongbupan.tpush.exception;

public class TPushClientException extends Exception {

	private static final long serialVersionUID = -8031067099108552445L;

	public TPushClientException() {
		super();
	}

	public TPushClientException(String msg) {
		super(msg);
	}

	public TPushClientException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
