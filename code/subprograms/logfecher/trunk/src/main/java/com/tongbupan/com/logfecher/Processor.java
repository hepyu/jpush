package com.tongbupan.com.logfecher;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import com.tongbupan.com.logfecher.message.RequestLogMessage;
import com.tongbupan.com.logfecher.tpush.TPushClient;
import com.tongbupan.tpush.DefaultNotice;
import com.tongbupan.tpush.Notice;

public class Processor {

	private static final Logger logger = Logger.getLogger(Processor.class);

	static {
		TPushClient.init();
	}

	private Notice buildMessage() throws Exception {

		String webPath = TPushClient.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		logger.info(webPath);

		int index = webPath.indexOf("/WEB-INF/");
		if (index < 0) {
			index = webPath.indexOf("/lib/");
		}
		if (index < 0) {
			index = webPath.length() - 1;
		}

		// 1.init client-config model
		StringBuilder path = new StringBuilder().append(
				webPath.substring(0, index)).append("/conf/process.properties");

		long ts = System.currentTimeMillis();

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(path.toString());
		prop.load(fis);
		RequestLogMessage message = new RequestLogMessage();
		message.setWhichlog(prop.getProperty("whichlog"));
		message.setFilename(prop.getProperty("filename"));
		message.setHostsn(prop.getProperty("hostsn"));
		message.setUsername(prop.getProperty("username"));
		message.setAskACK(true);
		message.setMsgId(UUID.randomUUID().toString());
		message.setTimestamp(ts + "");

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> data = new HashMap<String, String>();
		data.put("content", objectMapper.writeValueAsString(message));

		logger.info(objectMapper.writeValueAsString(message));

		DefaultNotice notice = new DefaultNotice();
		notice.setData(data);
		notice.setOwnerId(prop.getProperty("username"));
		notice.setTimestamp(ts);
		notice.setTopic(prop.getProperty("username"));

		return notice;
	}

	private void send(Notice notice) throws Exception {
		TPushClient.publish(notice);
	}

	public static void main(String[] args) {
		try {
			Processor processor = new Processor();

			Notice notice = processor.buildMessage();
			processor.send(notice);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
