package com.tongbupan.tpush.message.persistence.handler;

import java.util.List;

import com.tongbupan.tpush.message.persistence.entity.MessageEntity;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceHandlerExceptioin;

public interface IMessagePersistenceHandler {

	public int[] save(List<MessageEntity> messageEntityList)
			throws MessagePersistenceHandlerExceptioin;

	public String queryJSONResultByPage(String userId, long startts,
			String preId, int pageCount, boolean isDescSort)
			throws MessagePersistenceHandlerExceptioin;
}
