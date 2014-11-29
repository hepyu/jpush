package com.tongbupan.tpush.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import com.tongbupan.tpush.util.CounterUtil;

public class CapabilitySituationServlet extends HttpServlet {

	private static final long serialVersionUID = -8662193978984809224L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		CapabilitySituation msg = new CapabilitySituation();
		msg.setAliveWSConnCounter(CounterUtil.getAlivewsconncounter()
				.longValue());
		msg.setBroadcastCounter(CounterUtil.getBroadcastcounter().longValue());
		msg.setBroadcastResourceCounter(CounterUtil
				.getBroadcastresourcecounter().longValue());
		msg.setHeartBeatCounter(CounterUtil.getHeartbeatcounter().longValue());
		msg.setRedisNoticeCounter(CounterUtil.getRedisnoticecounter()
				.longValue());

		String str = objectMapper.writeValueAsString(msg);
		resp.getWriter().print(str);
		resp.flushBuffer();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		CapabilitySituation msg = new CapabilitySituation();
		try {
			System.out.println(objectMapper.writeValueAsString(msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.PROPERTY, property = "@class")
class CapabilitySituation {

	/**
	 * notice broadcast times
	 */
	private long broadcastCounter;

	/**
	 * count of resource that broadcasted to
	 */
	private long broadcastResourceCounter;

	/**
	 * recive notice times from redis
	 */
	private long redisNoticeCounter;

	/**
	 * alive count of websocket conn
	 */
	private long aliveWSConnCounter;

	/**
	 * count of heart beat
	 */
	private long heartBeatCounter;

	public long getBroadcastCounter() {
		return broadcastCounter;
	}

	public void setBroadcastCounter(long broadcastCounter) {
		this.broadcastCounter = broadcastCounter;
	}

	public long getBroadcastResourceCounter() {
		return broadcastResourceCounter;
	}

	public void setBroadcastResourceCounter(long broadcastResourceCounter) {
		this.broadcastResourceCounter = broadcastResourceCounter;
	}

	public long getRedisNoticeCounter() {
		return redisNoticeCounter;
	}

	public void setRedisNoticeCounter(long redisNoticeCounter) {
		this.redisNoticeCounter = redisNoticeCounter;
	}

	public long getAliveWSConnCounter() {
		return aliveWSConnCounter;
	}

	public void setAliveWSConnCounter(long aliveWSConnCounter) {
		this.aliveWSConnCounter = aliveWSConnCounter;
	}

	public long getHeartBeatCounter() {
		return heartBeatCounter;
	}

	public void setHeartBeatCounter(long heartBeatCounter) {
		this.heartBeatCounter = heartBeatCounter;
	}

}