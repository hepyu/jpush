package com.tongbupan.com.logfecher.message;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "@class")
public class RequestLogMessage extends AbstractDetailMessage {

	private String filename;
	private String whichlog;
	private String hostsn;
	private String username;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getWhichlog() {
		return whichlog;
	}

	public void setWhichlog(String whichlog) {
		this.whichlog = whichlog;
	}

	public String getHostsn() {
		return hostsn;
	}

	public void setHostsn(String hostsn) {
		this.hostsn = hostsn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
