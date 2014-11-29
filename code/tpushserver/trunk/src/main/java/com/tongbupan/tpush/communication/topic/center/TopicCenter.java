package com.tongbupan.tpush.communication.topic.center;

import java.util.List;
import java.util.Set;

import com.tongbupan.tpush.communication.connection.TPushConnection;

/**
 * 1.topic-connections
 * 
 * @author hpy
 * 
 */
public interface TopicCenter {

	public void init();

	public void registerTopics(TPushConnection connection);

	// topic(map) -> key--userId:messageType, value-list<connection>
	// topic -- messagetype
	// IM(map) --> key--> groupId:im, value-
	public Set<TPushConnection> getConnections(String userId, String topic);

	public List<String> getDefaultTopics(String userId);

	public List<String> getValidTopics();

	public void remove(String connectionId, String userId);

	public void remove(TPushConnection connection);

}
