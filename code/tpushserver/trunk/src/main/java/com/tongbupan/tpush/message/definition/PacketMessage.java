package com.tongbupan.tpush.message.definition;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.tongbupan.tpush.message.menum.PacketType;
import com.tongbupan.tpush.util.JSONUtil;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "@class")
public class PacketMessage extends ProtocolMessage {

	public static final String FIELD_DATA = "data";

	public static final String FIELD_CLASS = "@class";

	/**
	 * json style
	 */
	private String data;

	private PacketType packetType;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public PacketType getPacketType() {
		return packetType;
	}

	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}

	public static void main(String[] args) throws Exception {
		PacketMessage msg = new PacketMessage();
		msg.setPacketType(PacketType.persistence);

		StringBuilder sb = new StringBuilder("[");
		sb.append(JSONUtil.getObjectMapperInstance().writeValueAsString(
				new NoticeMessage()));
		sb.append(",");
		sb.append(JSONUtil.getObjectMapperInstance().writeValueAsString(
				new NoticeMessage()));
		sb.append("]");
		System.out.println(sb.toString());

		JSONObject jsonObj = JSONObject.fromObject(msg);
		jsonObj.put("data", sb.toString());

		System.out.println(jsonObj.toString());

		List<NoticeMessage> list = new ArrayList<NoticeMessage>();
		list.add(new NoticeMessage());
		list.add(new NoticeMessage());
		System.out.println(JSONUtil.getObjectMapperInstance()
				.writeValueAsString(list));
	}
}
