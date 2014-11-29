/**
 * 
 */
package com.tongbupan.tpush.message.definition;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.tongbupan.tpush.tenum.MessageTypeEnum;

/**
 * 
 * @author hpy
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ACKMessage extends AbstractBaseMessage {

	// 触发ack消息的原消息类型
	private MessageTypeEnum type;

	public MessageTypeEnum getType() {
		return type;
	}

	public void setType(MessageTypeEnum type) {
		this.type = type;
	}

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		ACKMessage msg = new ACKMessage();
		msg.setType(MessageTypeEnum.WebSocketStatMessage);
		try {
			System.out.println(objectMapper.writeValueAsString(msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
