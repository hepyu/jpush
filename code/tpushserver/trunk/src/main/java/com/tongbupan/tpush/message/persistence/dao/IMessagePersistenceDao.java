package com.tongbupan.tpush.message.persistence.dao;

import java.util.List;

import com.tongbupan.tpush.message.persistence.entity.MessageEntity;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceDaoExceptioin;

public interface IMessagePersistenceDao {

	public int[] save(List<MessageEntity> messageEntityList)
			throws MessagePersistenceDaoExceptioin;

	public String queryJSONResultByPage(String userId, long startts,
			String preMsgId, int pageCount, boolean isDescSort)
			throws MessagePersistenceDaoExceptioin;

}
