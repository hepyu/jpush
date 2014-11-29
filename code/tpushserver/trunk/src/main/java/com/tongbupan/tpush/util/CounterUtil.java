package com.tongbupan.tpush.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * system counter
 * 
 * @author hpy
 * 
 */
public class CounterUtil {

	/**
	 * notice broadcast times
	 */
	private final static AtomicLong broadcastCounter;

	/**
	 * count of resource that broadcasted to
	 */
	private final static AtomicLong broadcastResourceCounter;

	/**
	 * recive notice times from redis
	 */
	private final static AtomicLong redisNoticeCounter;

	/**
	 * alive count of websocket conn
	 */
	private final static AtomicLong aliveWSConnCounter;

	/**
	 * count of heart beat
	 */
	private final static AtomicLong heartBeatCounter;

	static {
		broadcastCounter = new AtomicLong(0);
		broadcastResourceCounter = new AtomicLong(0);
		redisNoticeCounter = new AtomicLong(0);
		aliveWSConnCounter = new AtomicLong(0);
		heartBeatCounter = new AtomicLong(0);
	}

	public static AtomicLong getBroadcastcounter() {
		return broadcastCounter;
	}

	public static AtomicLong getBroadcastresourcecounter() {
		return broadcastResourceCounter;
	}

	public static AtomicLong getRedisnoticecounter() {
		return redisNoticeCounter;
	}

	public static AtomicLong getAlivewsconncounter() {
		return aliveWSConnCounter;
	}

	public static AtomicLong getHeartbeatcounter() {
		return heartBeatCounter;
	}
}
