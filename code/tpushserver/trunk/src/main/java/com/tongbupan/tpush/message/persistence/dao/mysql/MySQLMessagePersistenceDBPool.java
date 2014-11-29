/**
 * Banckle - http://banckle.com
 * Copyright (c) 2001-2011 Aspose Pty Ltd. All rights Reserved
 */

/**
 * 
 */
package com.tongbupan.tpush.message.persistence.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.tongbupan.tpush.message.persistence.MessagePersistenceBeanFactory;

/**
 * Provide the connection data source.
 * 
 * @author Shengkai Kuang
 * 
 */
public class MySQLMessagePersistenceDBPool {
	private DataSource ds;

	/**
	 * @return the ds
	 */
	public DataSource getDs() {
		return ds;
	}

	/**
	 * @param ds
	 *            the ds to set
	 */
	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * singleton object
	 */
	private static MySQLMessagePersistenceDBPool cm;

	/**
	 * @return a database connection
	 * @throws SQLException
	 */
	static public Connection getConnection() throws SQLException {
		if (cm != null) {
			return cm.ds.getConnection();
		} else {
			synchronized (MySQLMessagePersistenceDBPool.class) {
				if (cm != null) {
					return cm.ds.getConnection();
				} else {
					cm = MessagePersistenceBeanFactory.getBean(
							"MySQLMessagePersistenceDBPool",
							MySQLMessagePersistenceDBPool.class);
					return cm.ds.getConnection();
				}
			}
		}
	}
}
