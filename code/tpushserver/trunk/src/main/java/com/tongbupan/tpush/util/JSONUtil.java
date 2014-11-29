package com.tongbupan.tpush.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JSONUtil {

	private static ObjectMapper objectMapper;

	public static ObjectMapper getObjectMapperInstance() {
		if (objectMapper == null) {
			synchronized (JSONUtil.class) {
				if (objectMapper == null) {
					objectMapper = new ObjectMapper();
				}
			}
		}
		return objectMapper;
	}
}
