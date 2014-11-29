package com.tongbupan.test.websocket.tunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.*;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import com.tongbupan.test.websocket.ClientWebSocket;

public class WebSocketServerMaxConnsTest_SingleThread {

	private static final Logger logger = Logger
			.getLogger(WebSocketServerMaxConnsTest_SingleThread.class);

	// ***********************(1).params init*************************//

	// 1.store the test params
	private static final Properties props;

	// 2.The websocket conn url from TPushServer,example:
	// "ws://192.168.64.93:8080/TPushServer/websockets?userId="
	private static final String URL_CONN;

	// 3.The url of business web,use post request type, example:
	// "http://192.168.64.94:8080/browse/sendmsg"
	// private static final String URL_SENDMSG;

	// 4.the count of the ws conn that the test will create.
	private static final int WEBSOCKET_CONN_COUNT;

	// 5.websocket_client_factory_buffsize
	private static final int WEBSOCKET_CLIENT_FACTORY_BUFFSIZE;

	// 6.websocket_client_max_idle_time
	private static final int WEBSOCKET_CLIENT_MAX_IDLE_TIME;

	// 7.websocket_client_max_connect_time
	private static final int WEBSOCKET_CLIENT_MAX_CONNECT_TIME;

	// 8.websocket_client_stat_result_interval
	private static final int WEBSOCKET_CLIENT_STAT_RESULT_INTERVAL;

	// 9.websocket_client_batch_test_count_conn
	private static final int WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN;

	// 10.batch size
	private static final int WEBSOCKET_CLIENT_BATCH_TEST_SIZE;

	// 11.tpush_server_websocket_onclose_listen_on
	private static final String WEBSOCKET_ONCLOSE_LISTEN_ON;

	// 12.current_test_client_flag
	private static final String CURRENT_TEST_CLIENT_FLAG;

	static {
		props = new Properties();
		// 1.read test config.

		File confile = new File(
				"/Users/hpy/WebSocketServerMaxConnsTest_SingleThread.properties");
		System.out.println("a");
		logger.info("init");
		System.out.println("b");

		try {
			if (confile.exists()) {
				logger.info("test config file is:" + confile.getAbsolutePath());
				props.load(new FileInputStream(confile));
			} else {
				String defaultfile = "com/tongbupan/test/websocket/tunit/WebSocketServerMaxConnsTest_SingleThread.properties";
				logger.info("test config file is using defalut file.");
				InputStream in = ClassLoader
						.getSystemResourceAsStream(defaultfile);
				props.load(in);
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		// 2.websocket conn url
		URL_CONN = new StringBuilder(
				props.getProperty("tpush_server_websocket_conn_protocol"))
				.append("://").append(props.getProperty("tpush_server_ip"))
				.append(":").append(props.getProperty("tpush_server_port"))
				.append("/")
				.append(props.getProperty("tpush_server_websocket_conn_url"))
				.toString();
		logger.info("tpush_server_websocket_conn_url = " + URL_CONN.toString());

		// 3.url of business web
		// URL_SENDMSG = new StringBuilder(
		// props.getProperty("business_web_sendmsg_protocol"))
		// .append("://").append(props.getProperty("business_web_ip"))
		// .append(":").append(props.getProperty("business_web_port"))
		// .append("/")
		// .append(props.getProperty("business_web_sendmsg_url"))
		// .toString();
		// logger.info("business_web_sendmsg_url = " + URL_SENDMSG.toString());

		// 4.the count of websocket that the test will create
		WEBSOCKET_CONN_COUNT = new Integer(
				props.getProperty("tpush_server_websocket_conn_count"))
				.intValue();
		logger.info("tpush_server_websocket_conn_count = "
				+ WEBSOCKET_CONN_COUNT);

		// 5.websocket_client_factory_buffsize
		WEBSOCKET_CLIENT_FACTORY_BUFFSIZE = new Integer(
				props.getProperty("websocket_client_factory_buffsize"))
				.intValue();
		logger.info("websocket_client_factory_buffsize = "
				+ WEBSOCKET_CLIENT_FACTORY_BUFFSIZE);

		// 6.websocket_client_max_idle_time
		WEBSOCKET_CLIENT_MAX_IDLE_TIME = new Integer(
				props.getProperty("websocket_client_max_idle_time")).intValue();
		logger.info("websocket_client_max_idle_time = "
				+ WEBSOCKET_CLIENT_MAX_IDLE_TIME);

		// 7.websocket_client_max_connect_time
		WEBSOCKET_CLIENT_MAX_CONNECT_TIME = new Integer(
				props.getProperty("websocket_client_max_connect_time"))
				.intValue();
		logger.info("websocket_client_max_connect_time = "
				+ WEBSOCKET_CLIENT_MAX_CONNECT_TIME);

		// 8.websocket_client_stat_result_interval
		WEBSOCKET_CLIENT_STAT_RESULT_INTERVAL = new Integer(
				props.getProperty("websocket_client_stat_result_interval"))
				.intValue();
		logger.info("websocket_client_stat_result_interval = "
				+ WEBSOCKET_CLIENT_STAT_RESULT_INTERVAL);

		// 9.websocket_client_batch_test_count_conn
		WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN = new Integer(
				props.getProperty("websocket_client_batch_test_count_conn"));
		logger.info("websocket_client_batch_test_count_conn = "
				+ WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN);

		// 10.websocket_client_batch_test_size
		double d = 1.0;
		WEBSOCKET_CLIENT_BATCH_TEST_SIZE = (int) Math
				.ceil((WEBSOCKET_CONN_COUNT / d)
						/ WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN);
		logger.info("websocket_client_batch_test_size = "
				+ WEBSOCKET_CLIENT_BATCH_TEST_SIZE);

		// 11.tpush_server_websocket_onclose_listen_on
		WEBSOCKET_ONCLOSE_LISTEN_ON = props
				.getProperty("tpush_server_websocket_onclose_listen_on");
		logger.info("tpush_server_websocket_onclose_listen_on = "
				+ WEBSOCKET_ONCLOSE_LISTEN_ON);

		// 12.current_test_client_flag
		CURRENT_TEST_CLIENT_FLAG = props
				.getProperty("current_test_client_flag");
		logger.info("current_test_client_flag = " + CURRENT_TEST_CLIENT_FLAG);

		// end init param
		logger.info("*************************end start params*************************");
	}

	// ***********************(2).process function*************************//

	// batch create websocket conn
	private static void batchCreateWebsocketConn(
			WebSocketClientFactory factory, int start, int end, int batchNum) {
		WebSocketClient client = null;
		ClientWebSocket socket = null;
		client = factory.newWebSocketClient();
		for (int x = start; x < end; x++) {
			client.setMaxIdleTime(WEBSOCKET_CLIENT_MAX_IDLE_TIME);
			client.setProtocol("ping." + x);

			socket = new ClientWebSocket();
			socket.setUserId(CURRENT_TEST_CLIENT_FLAG + x);

			while (true) {
				try {
					logger.info("userId:" + socket.getUserId());
					client.open(
							new URI(URL_CONN + CURRENT_TEST_CLIENT_FLAG + x),
							socket, WEBSOCKET_CLIENT_MAX_CONNECT_TIME,
							TimeUnit.HOURS);
					break;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		logger.info(new StringBuilder("batch create websocket conn, from num.")
				.append(start).append(" to num.").append(end)
				.append(", ws_alive_count:")
				.append(ClientWebSocket.getWsLiveCount().longValue()));
	}

	// main process function
	private static void doTest() {

		// 1.init websocket client factory
		WebSocketClientFactory factory = new WebSocketClientFactory();
		factory.setBufferSize(WEBSOCKET_CLIENT_FACTORY_BUFFSIZE);
		try {
			factory.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("begin test");
		// 2.batch create websocket conn and send business request
		for (int batchnum = 0; batchnum < WEBSOCKET_CLIENT_BATCH_TEST_SIZE; batchnum++) {

			int start = 0 + batchnum * WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN;

			int end = (1 + batchnum) * WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN;
			end = end > WEBSOCKET_CONN_COUNT ? WEBSOCKET_CONN_COUNT : end;

			// 2.1.batch create websocket conn
			batchCreateWebsocketConn(factory, start, end, batchnum + 1);
		}

		// 4.stop the client factory
		while (true) {
			logger.info(new StringBuilder().append(", ws_alive_count:").append(
					ClientWebSocket.getWsLiveCount()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// ***********************(3).main function*************************//

	public static void main(String[] args) throws Exception {
		doTest();
	}
}
