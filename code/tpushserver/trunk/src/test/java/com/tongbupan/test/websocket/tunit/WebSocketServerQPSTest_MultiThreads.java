package com.tongbupan.test.websocket.tunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import com.tongbupan.test.websocket.ClientWebSocket;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class WebSocketServerQPSTest_MultiThreads {

	private static final Logger logger = Logger
			.getLogger(WebSocketServerQPSTest_MultiThreads.class);

	// ***********************(1).params init*************************//

	// 1.store the test params
	private static final Properties props;

	// 2.The websocket conn url from TPushServer,example:
	// "ws://192.168.64.93:8080/TPushServer/websockets?userId="
	private static final String URL_CONN;

	// 3.The url of business web,use post request type, example:
	// "http://192.168.64.94:8080/browse/sendmsg"
	private static final String URL_SENDMSG;

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

	// 13.test_thread_count
	private static final int TEST_THREAD_COUNT;

	// 14.test_run_time , unit by minutes
	private static final int TEST_RUN_TIME;

	// 15.tpush_server_test_client_url
	private static final String TPUSH_SERVER_TEST_CLIENT_URL;

	static {
		props = new Properties();

		// 1.read test config.
		// after deploying in the server,we should config the file in the path
		// below ,in order to config test param.
		File confile = new File(
				"/usr/tongbupan_test_config/WebSocketServerQPSTest.properties");

		try {
			if (confile.exists()) {
				logger.info("test config file is:" + confile.getAbsolutePath());
				props.load(new FileInputStream(confile));
			} else {
				String defaultfile = "com/tongbupan/test/websocket/tunit/WebSocketServerQPSTest.properties";
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
		URL_SENDMSG = new StringBuilder(
				props.getProperty("business_web_sendmsg_protocol"))
				.append("://").append(props.getProperty("business_web_ip"))
				.append(":").append(props.getProperty("business_web_port"))
				.append("/")
				.append(props.getProperty("business_web_sendmsg_url"))
				.toString();
		logger.info("business_web_sendmsg_url = " + URL_SENDMSG.toString());

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

		// 13.test_thread_count
		TEST_THREAD_COUNT = new Integer(props.getProperty("test_thread_count"));
		logger.info("test_thread_count = " + TEST_THREAD_COUNT);

		// 14.test_run_time
		TEST_RUN_TIME = new Integer(props.getProperty("test_run_time"));
		logger.info("test_run_time = " + TEST_RUN_TIME);

		// 15.tpush_server_test_client_url
		TPUSH_SERVER_TEST_CLIENT_URL = props
				.getProperty("tpush_server_test_client_url");
		logger.info("tpush_server_test_client_url = "
				+ TPUSH_SERVER_TEST_CLIENT_URL);

		// end init param
		logger.info("*************************end start params*************************");

	}

	protected static final List<ClientWebSocket> clients = new ArrayList<ClientWebSocket>();

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
			try {
				client.open(new URI(URL_CONN + CURRENT_TEST_CLIENT_FLAG + x),
						socket, WEBSOCKET_CLIENT_MAX_CONNECT_TIME,
						TimeUnit.HOURS);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			clients.add(socket);
		}

		logger.info(CURRENT_TEST_CLIENT_FLAG
				+ "batch create websocket conn, from num." + start + " to num."
				+ end);
	}

	// batch validate websocket conn whether alive
	private static void batchValidateWebsocketConn(int uplimit, int batchNum) {
		while (true) {
			try {
				Thread.sleep(WEBSOCKET_CLIENT_STAT_RESULT_INTERVAL * 1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}

			int aliveCount = ClientWebSocket.getWsLiveCount().intValue();
			logger.info(CURRENT_TEST_CLIENT_FLAG + "ws_alive_count is : "
					+ aliveCount + " ; current uplimit is : " + uplimit
					+ " ; batchNum is : " + batchNum);
			if (aliveCount == uplimit) {
				break;
			}
		}
	}

	private static final WebSocketClientFactory factory = new WebSocketClientFactory();

	// main process function
	private static void doCreateConn() {

		// 1.init websocket client factory
		factory.setBufferSize(WEBSOCKET_CLIENT_FACTORY_BUFFSIZE);
		try {
			factory.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// 2.batch create websocket conn and send business request
		for (int batchnum = 0; batchnum < WEBSOCKET_CLIENT_BATCH_TEST_SIZE; batchnum++) {

			int start = 0 + batchnum * WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN;

			int end = (1 + batchnum) * WEBSOCKET_CLIENT_BATCH_TEST_COUNT_CONN;
			end = end > WEBSOCKET_CONN_COUNT ? WEBSOCKET_CONN_COUNT : end;

			// 2.1.batch create websocket conn
			batchCreateWebsocketConn(factory, start, end, batchnum + 1);

			// 2.2.batch send msg to test conn alive
			// batchTestConnAlive(start, end);

			// 2.3.batch validate websocket conn whether alive
			batchValidateWebsocketConn(end, batchnum + 1);
		}

		// reset to zero, in order to add by 1 step
		// ClientWebSocket.getAliveCount().getAndSet(0);

	}

	// ***********************(3).close resources*************************//

	// private static void closeResources() {
	//
	// for (ClientWebSocket socket : clients) {
	// socket.getConnection().close();
	// }
	//
	// logger.info(CURRENT_TEST_CLIENT_FLAG
	// + "after close all ws conn, now alive count is:"
	// + ClientWebSocket.getAliveCount());
	//
	// // stop the client factory
	// try {
	// factory.stop();
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// }
	//
	// logger.info("end qps test");
	// }

	public static void startTest() {
		HttpClient hc = new HttpClient();
		PostMethod postMethod = new PostMethod(TPUSH_SERVER_TEST_CLIENT_URL);
		try {
			// hc.setConnectionTimeout(1000 * 1000 * 1000);
			// hc.setTimeout(1000 * 1000 * 1000);
			hc.executeMethod(postMethod);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			// notice
			postMethod.releaseConnection();
		}
	}

	// ***********************(4).main function*************************//

	public static void main(String[] args) throws Exception {

		// PropertyConfigurator.configure("/tpush/test/log4j.properties");
		//PropertyConfigurator.configure("/Users/hpy/Documents/workspace/webpush-spring/src/test/resources/log4j.properties");

		// 1.建立连接并测试是否成功建立
		doCreateConn();
		logger.info("alive ws conn count:"
				+ ClientWebSocket.getWsLiveCount().longValue());
		logger.info("receive message count:"
				+ ClientWebSocket.getReceiveMessageCount());

		// 2.向tpushserver_client发送启动信号
		startTest();

		// 3.log
		logger.info("test client begin");
		Thread.sleep(5 * 60 * 1000);
		while (true) {

			logger.info("alive ws conn count:"
					+ ClientWebSocket.getWsLiveCount().longValue());
			logger.info("receive message count:"
					+ ClientWebSocket.getReceiveMessageCount());

			Thread.sleep(1000);
		}
	}
}
