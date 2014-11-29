package com.tongbupan.tpush.message.definition;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.tongbupan.tpush.tenum.FSFileTypeEnum;
import com.tongbupan.tpush.tenum.FSOperTypeEnum;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FSMessage extends BusinessMessage {

	// 文件类型：是文件还是目录
	private FSFileTypeEnum fileType;

	// 操作类型:add update delete
	private FSOperTypeEnum operType;

	// 用户名（email）
	private String username;

	// 文件路径
	private String path;

	// 是否需要向appserver发起请求
	private boolean isFetch;

	// appserver url
	private String url;

	public FSFileTypeEnum getFileType() {
		return fileType;
	}

	public void setFileType(FSFileTypeEnum fileType) {
		this.fileType = fileType;
	}

	public FSOperTypeEnum getOperType() {
		return operType;
	}

	public void setOperType(FSOperTypeEnum operType) {
		this.operType = operType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isFetch() {
		return isFetch;
	}

	public void setFetch(boolean isFetch) {
		this.isFetch = isFetch;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		FSMessage msg = new FSMessage();
		msg.setFileType(FSFileTypeEnum.directory);
		msg.setOperType(FSOperTypeEnum.add);
		msg.setUsername("a@qq.com");
		msg.setPath("/mybooks");
		try {
			System.out.println(objectMapper.writeValueAsString(msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
