package com.tongbupan.tpush.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.tongbupan.tpush.DefaultNotice;
import com.tongbupan.tpush.Notice;

public class NoticeMaster {
	static ObjectMapper mapper = new ObjectMapper(); 
	public static Notice parse(String noticeInString){
		DefaultNotice notice = null;
		try {
			notice = mapper.readValue(noticeInString, DefaultNotice.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notice;
		
	}
	
	
	public static String parse(Notice notice){
		String result = null;
		try {
			result = mapper.writeValueAsString(notice);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
}
