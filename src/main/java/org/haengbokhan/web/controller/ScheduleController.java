package org.haengbokhan.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import org.haengbokhan.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * @author Administrator
 *
 */
@Controller
public class ScheduleController {

	/**
	 * @param model
	 * @param httpSession
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/schedule")
	public String schedulePageHandler(Model model, HttpSession httpSession)
			throws UnsupportedEncodingException {
		User user = (User) httpSession.getAttribute("user");
		String state = (String) httpSession.getAttribute("oAuthState");
		String token = (String) httpSession.getAttribute("accessToken");
		model.addAttribute("currentUser", user);
		model.addAttribute("oAuthState", state);
		model.addAttribute("accessToken", token);
		return "/pages/schedule";
	}
}
