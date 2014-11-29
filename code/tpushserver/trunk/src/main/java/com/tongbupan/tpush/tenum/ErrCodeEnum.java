package com.tongbupan.tpush.tenum;

public enum ErrCodeEnum {

	// 1.user no login
	ERR_CODE_NOLOGIN(100);

	private final int code;

	private ErrCodeEnum(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}

}
