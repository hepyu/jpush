package com.tongbupan.tpush.message.persistence.handler.impl;

import java.util.List;

import com.tongbupan.tpush.message.persistence.dao.IMessagePersistenceDao;
import com.tongbupan.tpush.message.persistence.entity.MessageEntity;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceDaoExceptioin;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceHandlerExceptioin;
import com.tongbupan.tpush.message.persistence.handler.IMessagePersistenceHandler;

public class MessagePersistenceHandlerImpl implements
		IMessagePersistenceHandler {

	private IMessagePersistenceDao messagePersistenceDao;

	@Override
	public int[] save(List<MessageEntity> messageEntityList)
			throws MessagePersistenceHandlerExceptioin {
		int[] count = null;
		try {
			count = messagePersistenceDao.save(messageEntityList);
		} catch (MessagePersistenceDaoExceptioin e) {
			throw new MessagePersistenceHandlerExceptioin(e.getMessage(), e);
		}
		return count;
	}

	@Override
	public String queryJSONResultByPage(String userId, long startts,
			String preId, int pageCount, boolean isDescSort)
			throws MessagePersistenceHandlerExceptioin {
		try {
			return messagePersistenceDao.queryJSONResultByPage(userId, startts,
					preId, pageCount, isDescSort);
		} catch (MessagePersistenceDaoExceptioin e) {
			throw new MessagePersistenceHandlerExceptioin(e.getMessage(), e);
		}
	}

	public IMessagePersistenceDao getMessagePersistenceDao() {
		return messagePersistenceDao;
	}

	public void setMessagePersistenceDao(
			IMessagePersistenceDao messagePersistenceDao) {
		this.messagePersistenceDao = messagePersistenceDao;
	}

}
