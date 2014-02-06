package org.haengbokhan.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	private Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = { "/main" }, method = RequestMethod.GET)
	public String mainPageHandler() {
		return "/pages/main";
	}
}
