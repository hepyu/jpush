package com.tongbupan.tpush.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import com.emacle.foundation.model.UserInfo;
import com.emacle.foundation.util.VerifyUtil;
import com.tongbupan.tpush.communication.config.CommunicationBeanConfig;
import com.tongbupan.tpush.servlet.component.TPushJetty8WebSocketConnection;

public class TPushWebsocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = 1928465910605308605L;

	private static final Logger logger = Logger
			.getLogger(TPushWebsocketServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Check if this connection is valid
	 */
	@Override
	public boolean checkOrigin(HttpServletRequest req, String origin) {
		try {
			UserInfo userInfo = VerifyUtil.verifyUser(req);
			if (userInfo != null) {
				req.setAttribute("userId", userInfo.getUserName());
				return true;
			} else {
				logger.info("user verify failed");
			}
		} catch (Exception e) {
			logger.info("exception wheen user verify.", e);
		}
		return false;
	}

	/**
	 * build a connection
	 */
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest req, String protocol) {

		TPushJetty8WebSocketConnection tpushConnection = (TPushJetty8WebSocketConnection) CommunicationBeanConfig
				.getConnectionBean();
		tpushConnection.configRequest(req);

		return tpushConnection;
	}
}
