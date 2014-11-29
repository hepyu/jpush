package org.eclipse.jetty.server.ssl;

import java.io.IOException;
import java.net.Socket;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class TPushSslSelectChannelConnector extends SslSelectChannelConnector {

	public TPushSslSelectChannelConnector() {
		super();
	}

	public TPushSslSelectChannelConnector(SslContextFactory sslContextFactory) {
		super(sslContextFactory);
	}

	@Override
	protected void configure(Socket socket) throws IOException {
		super.configure(socket);
		socket.setKeepAlive(true);
	}
}
