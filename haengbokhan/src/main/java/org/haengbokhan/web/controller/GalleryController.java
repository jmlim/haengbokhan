package org.haengbokhan.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Administrator
 *
 */
@Controller
//@SessionAttributes(value = { "user" }, types = { User.class })
public class GalleryController {
	private Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = { "/gallery/gallery" }, method = RequestMethod.GET)
	public String loginPageHandler() {
		return "/pages/gallery/gallery";
	}
}
