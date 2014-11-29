package com.tongbupan.tpush.message.definition;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "@class")
public class NotifyMessage extends BusinessMessage {

	private String notifyData;

	public String getNotifyData() {
		return notifyData;
	}

	public void setNotifyData(String notifyData) {
		this.notifyData = notifyData;
	}

	public static void main(String[] args) throws Exception {
		ObjectMapper omapper = new ObjectMapper();

		NotifyMessage notifyMessage = new NotifyMessage();
		notifyMessage.setNotifyData("hello");

		String str = omapper.writeValueAsString(notifyMessage);
		System.out.println(str);
	}

}
