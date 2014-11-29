package com.tongbupan.tpush.message.persistence.dao.mongo;

import com.mongodb.DB;

public class MongoMessagePersistenceDBPool {

	/**
	 * mongo db
	 */
	private static DB mongoDB;

	public static DB getMongoDB() {
		return mongoDB;
	}

	public static void setMongoDB(DB mongoDB) {
		MongoMessagePersistenceDBPool.mongoDB = mongoDB;
	}
}
