package com.tongbupan.tpush.communication.cenum;

public enum ResultCode {

	OK(0, "process success"),

	CONNECTION_IS_NULL(100, "connection obj is null.");

	private ResultCode(int code, String define) {
		this.code = code;
		this.define = define;
	}

	private int code;

	private String define;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDefine() {
		return define;
	}

	public void setDefine(String define) {
		this.define = define;
	}

	public static void main(String[] args) {
		for (ResultCode name : ResultCode.values()) {
			System.out.println(name + ":" + name.getCode() + ":"
					+ name.getDefine());
		}
	}

}
