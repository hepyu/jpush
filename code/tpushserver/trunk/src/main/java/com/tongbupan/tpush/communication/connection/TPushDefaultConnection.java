/**
 * 
 */
package com.tongbupan.tpush.communication.connection;

import java.util.UUID;

/**
 * @author hpy
 * 
 */
public abstract class TPushDefaultConnection implements TPushConnection {

	private String connectionId;

	public TPushDefaultConnection() {
		connectionId = UUID.randomUUID().toString();
	}

	@Override
	public boolean equals(Object obj) {
		return this.getConnectionId().equals(
				((TPushConnection) obj).getConnectionId());
	}

	public String getConnectionId() {
		return connectionId;
	}

}
