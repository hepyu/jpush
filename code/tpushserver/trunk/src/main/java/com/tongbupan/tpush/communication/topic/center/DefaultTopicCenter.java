package com.tongbupan.tpush.communication.topic.center;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tongbupan.tpush.communication.config.bean.MapParamBean;
import com.tongbupan.tpush.communication.config.bean.TopicBean;
import com.tongbupan.tpush.communication.connection.TPushConnection;
import com.tongbupan.tpush.message.definition.ProtocolMessage;

/**
 * Suppose this class is singleton
 * 
 * @author tianchao
 * 
 */
public class DefaultTopicCenter implements TopicCenter {

	private static final Logger logger = Logger
			.getLogger(DefaultTopicCenter.class);

	// topicKey->connection set
	private ConcurrentHashMap<String, Set<TPushConnection>> topicsNamespace;

	private TopicBean topicBean;

	private MapParamBean mapParamBean_for_topicsNamespace;

	private List<String> defaultTopicList;

	public DefaultTopicCenter() {
	}

	@Override
	public void init() {
		if (topicsNamespace == null) {
			synchronized (this) {
				if (topicsNamespace == null) {
					// 1.init the topicList
					defaultTopicList = topicBean.getDefaultTopicList();
					defaultTopicList.add(ProtocolMessage.class.getSimpleName());// protocol
																				// message
																				// type
					// 2.init topicsNamespace map
					topicsNamespace = new ConcurrentHashMap<String, Set<TPushConnection>>(
							mapParamBean_for_topicsNamespace.getInitCapacity(),
							mapParamBean_for_topicsNamespace.getLoadFactor(),
							mapParamBean_for_topicsNamespace
									.getConcurrencyLevel());
				}
			}
		}
	}

	@Override
	public List<String> getDefaultTopics(String userId) {
		// TODO different users with different topics
		return defaultTopicList;
	}

	public String getTopicKey(String userid, String topic) {
		String key = new StringBuilder().append(userid).append(":")
				.append(topic).toString();
		return key;
	}

	private void registerTopic(final TPushConnection connection, String topicKey) {
		synchronized (topicsNamespace) {
			Set<TPushConnection> cset = topicsNamespace.get(topicKey);
			if (cset == null) {
				cset = new HashSet<TPushConnection>();
				topicsNamespace.put(topicKey, cset);
			}
			if (cset.add(connection)) {
				// TODO not already contain this connection
			}
		}
	}

	/**
	 * @topic userId:messageType
	 */
	@Override
	public void registerTopics(final TPushConnection connection) {
		String[] topics = connection.getTopics();

		if (topics == null || topics.length == 0) { // set default topics
			topics = new String[defaultTopicList.size()];
			topics = defaultTopicList.toArray(topics);
			connection.setTopics(topics);
		}

		String topicKey = null;
		for (String topic : topics) {
			topicKey = getTopicKey(connection.getUserId(), topic);
			registerTopic(connection, topicKey);
		}
	}

	@Override
	public Set<TPushConnection> getConnections(String userId, String topic) {
		String topicKey = getTopicKey(userId, topic);
		Set<TPushConnection> connections = null;
		connections = topicsNamespace.get(topicKey); // Get without additional
														// lock
		// TODO if have influence of memory consumption
		// TODO
		if (connections != null) {
			return new HashSet<TPushConnection>(connections);
		} else {
			return null;
		}
	}

	@Override
	public void remove(TPushConnection connection) {
		// TODO
		remove(connection.getConnectionId(), connection.getUserId());
	}

	@Override
	public void remove(String connectionId, String userId) {
		if (connectionId == null || userId == null) {
			return;
		}
		synchronized (topicsNamespace) {
			List<String> topicList = this.getValidTopics();
			String topicKey = null;
			Set<TPushConnection> connectionSet = null;
			TPushConnection connection = null;
			for (String topic : topicList) {
				topicKey = this.getTopicKey(userId, topic);
				connectionSet = null;
				if ((connectionSet = topicsNamespace.get(topicKey)) != null) {
					Iterator<TPushConnection> it = connectionSet.iterator();
					while (it.hasNext()) {
						connection = it.next();
						if (connectionId.equals(connection.getConnectionId())) {
							it.remove();
						}
					}
				}
			}
		}
	}

	public String[] getTopicKeys(TPushConnection connection) {
		String[] topics = connection.getTopics();
		String userid = connection.getUserId();
		String[] topicKeys = new String[topics.length];
		String topicKey = null;
		int len = topics.length;
		for (int i = 0; i < len; i++) {
			topicKey = getTopicKey(userid, topics[i]);
			topicKeys[i] = topicKey;
		}
		return topicKeys;
	}

	@Override
	public List<String> getValidTopics() {
		// TODO suppose the valid topics is the same as default topics for now
		return this.defaultTopicList;
	}

	public TopicBean getTopicBean() {
		return topicBean;
	}

	public void setTopicBean(TopicBean topicBean) {
		this.topicBean = topicBean;
	}

	public MapParamBean getMapParamBean_for_topicsNamespace() {
		return mapParamBean_for_topicsNamespace;
	}

	public void setMapParamBean_for_topicsNamespace(
			MapParamBean mapParamBean_for_topicsNamespace) {
		this.mapParamBean_for_topicsNamespace = mapParamBean_for_topicsNamespace;
	}

	public static void main(String[] args) {
		List<String> defaultTopicList = new ArrayList<String>();
		defaultTopicList.add("1");
		defaultTopicList.add("2");
		defaultTopicList.add("3");

		String[] topics = new String[defaultTopicList.size()];
		topics = defaultTopicList.toArray(topics);

		for (String topic : topics) {
			System.out.println(topic);
		}
	}

}
