package com.tongbupan.tpush.message.persistence.dao.mysql.impl;

import com.tongbupan.tpush.message.persistence.dao.IMessagePersistenceDao;
import com.tongbupan.tpush.message.persistence.dao.mysql.AbstractMySQLMessagePersistenceDao;
import com.tongbupan.tpush.message.persistence.dao.mysql.MySQLMessagePersistenceDBPool;
import com.tongbupan.tpush.message.persistence.entity.MessageEntity;
import com.tongbupan.tpush.message.persistence.exception.MessagePersistenceDaoExceptioin;
import com.tongbupan.tpush.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLMessagePersistenceDaoImpl extends
		AbstractMySQLMessagePersistenceDao implements IMessagePersistenceDao {

	private static final Logger logger = Logger
			.getLogger(MySQLMessagePersistenceDaoImpl.class);

	private final String SQL_SAVE_MESSAGE = "INSERT INTO message_record(msg_id,msg_timestamp,user_id,msg_type,msg_body) VALUES(?,?,?,?,?)";

	@Override
	public int[] save(List<MessageEntity> messageEntityList)
			throws MessagePersistenceDaoExceptioin {

		if (messageEntityList == null || messageEntityList.isEmpty()) {
			return null;
		}

		PreparedStatement ps = null;
		Connection conn = null;
		SQLException e = null;

		Iterator<MessageEntity> iterator = messageEntityList.iterator();
		int[] result = null;
		try {
			conn = MySQLMessagePersistenceDBPool.getConnection();
			conn.setAutoCommit(true);
			ps = conn.prepareStatement(SQL_SAVE_MESSAGE);
			MessageEntity entity = null;
			while (iterator.hasNext()) {
				entity = iterator.next();
				ps.setString(1, entity.getMsgId());
				ps.setLong(2, entity.getTimestamp());
				ps.setString(3, entity.getUserId());
				ps.setString(4, entity.getType());
				ps.setString(5, entity.getMsgBody());
				ps.addBatch();
			}

			result = ps.executeBatch();
		} catch (SQLException sqe) {
			e = sqe;
			logger.error(sqe.getMessage(), sqe);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException sqe) {
					e = sqe;
					logger.error(sqe.getMessage(), sqe);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqe) {
					e = sqe;
					logger.error(sqe.getMessage(), sqe);
				}
			}

			if (e != null) {
				throw new MessagePersistenceDaoExceptioin(e.getMessage(), e);
			}
		}
		return result;
	}

	@Override
	public String queryJSONResultByPage(String userId, long startts,
			String preMsgId, int pageCount, boolean isDescSort)
			throws MessagePersistenceDaoExceptioin {

		if (StringUtil.isEmpty(userId) || startts < 0 || pageCount <= 0) {
			throw new MessagePersistenceDaoExceptioin("invalid empty params.");
		}

		StringBuilder sqlQueryMessage = new StringBuilder(
				"SELECT msg_id,msg_timestamp,user_id,msg_type,msg_body FROM message_record WHERE user_id=? AND msg_timestamp>=? AND msg_type='NotifyMessage' ORDER BY msg_timestamp ");
		if (isDescSort) {
			sqlQueryMessage.append(" DESC ");
		} else {
			sqlQueryMessage.append(" ASC ");
		}
		sqlQueryMessage.append(" LIMIT ? ");

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		StringBuilder list = new StringBuilder("[");
		try {
			conn = MySQLMessagePersistenceDBPool.getConnection();
			ps = conn.prepareStatement(sqlQueryMessage.toString());
			ps.setString(1, userId);
			ps.setLong(2, startts);
			ps.setInt(3, pageCount);

			rs = ps.executeQuery();
			while (rs.next()) {
				list.append(rs.getString("msg_body"));
				if (!rs.isLast()) {
					list.append(",");
				}
			}
			list.append("]");
		} catch (Exception sqe) {
			logger.error(sqe.getMessage(), sqe);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException sqe) {
					logger.error(sqe.getMessage(), sqe);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqe) {
					logger.error(sqe.getMessage(), sqe);
				}
			}
		}

		return list.toString();
	}
}
