package com.tongbupan.tpush.message.persistence.dao.mongo.impl;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.tongbupan.tpush.message.persistence.dao.IMessagePersistenceDao;
import com.tongbupan.tpush.message.persistence.dao.mongo.AbstractMongoMessagePersistenceDao;
import com.tongbupan.tpush.message.persistence.entity.MessageEntity;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceDaoExceptioin;
import com.tongbupan.tpush.util.StringUtil;

public class MongoMessagePersistenceDaoImpl extends
		AbstractMongoMessagePersistenceDao implements IMessagePersistenceDao {

	

	@Override
	public String queryJSONResultByPage(String userId, long startts,
			String preMsgId, int pageCount, boolean isDescSort)
			throws MessagePersistenceDaoExceptioin {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] save(List<MessageEntity> messageEntityList)
			throws MessagePersistenceDaoExceptioin {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public List<String> queryByPage(String userId, long startts, long endts,
	// String preMsgId, int pageCount, boolean isDescSort)
	// throws MessagePersistenceDaoExceptioin {
	//
	// if (StringUtil.isEmpty(userId) || startts < 0 || endts < 0
	// || startts > endts || StringUtil.isEmpty(preMsgId)
	// || pageCount <= 0) {
	// throw new MessagePersistenceDaoExceptioin("invalid empty params.");
	// }
	//
	// DBObject condition = new BasicDBObject();
	// condition.put(TABLE_MESSAGE_RECORD_FIELD_TIMESTAMP, new BasicDBObject(
	// "$gte", startts));
	// condition.put(TABLE_MESSAGE_RECORD_FIELD_TIMESTAMP, new BasicDBObject(
	// "$lte", endts));
	// condition.put(TABLE_MESSAGE_RECORD_FIELD_USER_ID, userId);
	//
	// DBCollection collection = getTableMessageRecord();
	// int sort = isDescSort ? -1 : 1;
	// DBCursor dbCursor = collection
	// .find(condition)
	// .sort(new BasicDBObject(TABLE_MESSAGE_RECORD_FIELD_TIMESTAMP,
	// sort)).limit(pageCount);
	//
	// List<String> list = new ArrayList<String>();
	//
	// DBObject obj = null;
	// String msgBody = null;
	// while (dbCursor.hasNext()) {
	// obj = dbCursor.next();
	// if (preMsgId.trim().equals(
	// obj.get(TABLE_MESSAGE_RECORD_FIELD_MSG_ID))) {
	// list.clear();
	// continue;
	// } else {
	// msgBody = (String) obj.get(TABLE_MESSAGE_RECORD_FIELD_MSG_BODY);
	// if (!StringUtil.isEmpty(msgBody)) {
	// list.add(msgBody);
	// }
	// }
	// }
	//
	// return list;
	// }
}
