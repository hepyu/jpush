package com.tongbupan.tpush.message.persistence.dao.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.tongbupan.tpush.message.persistence.dao.IMessagePersistenceDao;

public abstract class AbstractMongoMessagePersistenceDao implements
		IMessagePersistenceDao {

	// --------------------(1).table message_record define----------------------

	// ----(1).1.table message_record define----

	/**
	 * table message_record
	 */
	public static final String TABLE_MESSAGE_RECORD = "message_record";

	// ----(1).2.field define of table message_record

	public static final String TABLE_MESSAGE_RECORD_FIELD_USER_ID = "user_id";

	public static final String TABLE_MESSAGE_RECORD_FIELD_TIMESTAMP = "timestamp";

	public static final String TABLE_MESSAGE_RECORD_FIELD_MSG_BODY = "msg_body";

	public static final String TABLE_MESSAGE_RECORD_FIELD_MSG_ID = "msg_id";

	public static final String TABLE_MESSAGE_RECORD_FIELD_TYPE = "msg_type";

	public DBCollection getTableMessageRecord() {
		DB mongodb = MongoMessagePersistenceDBPool.getMongoDB();
		DBCollection collection = mongodb.getCollection(TABLE_MESSAGE_RECORD);
		return collection;
	}
}
