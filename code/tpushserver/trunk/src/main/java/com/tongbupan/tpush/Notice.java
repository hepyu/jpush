package com.tongbupan.tpush;

public abstract class Notice {

	public static final String KEY_MESSAGE_CONTENT = "content";

	public static final String DATA = "data";

	public static final String TIMESTAMP = "timestamp";

	public abstract String toString();

	public abstract String get(String key);

	public abstract long getTimestamp();

	public abstract String getTopic();

	public abstract String getOwnerId();

}
