package com.tongbupan.tpush.conn.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClientMessageController {

	private static final Logger logger = Logger
			.getLogger(ClientMessageController.class);

	@RequestMapping(value = "/clientwserrormsg", method = RequestMethod.POST)
	@ResponseBody
	public void clientWSErrorMessage(@RequestBody String message) {
		try {
			message = URLDecoder.decode(message, "UTF-8");
			logger.info(message);
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
	}

}
