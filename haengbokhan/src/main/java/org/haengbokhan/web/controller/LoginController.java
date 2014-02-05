package org.haengbokhan.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.haengbokhan.model.User;
import org.haengbokhan.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author Hana Lee
 * @since 0.0.2 2013. 1. 21. 오전 7:16:27
 * @revision $LastChangedRevision: 6066 $
 * @date $LastChangedDate: 2013-02-16 06:02:23 +0900 (토, 16 2월 2013) $
 * @by $LastChangedBy: voyaging $
 */
@Controller
@SessionAttributes(value = { "user" }, types = { User.class })
public class LoginController {

	private Log log = LogFactory.getLog(getClass());

	@Autowired(required = false)
	private UserDetailsManager detailsManager;

	@Autowired
	private UserManager ownerManager;

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String loginPageHandler(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "/pages/login";
	}
	
	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String signupPageHandler(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "/pages/signup";
	}
	

	@RequestMapping(value = { "/ldap/authentication" })
	public String ldapLoginHandler(@ModelAttribute("user") User user) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			LdapUserDetails ldapUserDetails = (LdapUserDetails) authentication
					.getPrincipal();
			Collection<? extends GrantedAuthority> userAuthorities = ldapUserDetails
					.getAuthorities();
			Iterator<? extends GrantedAuthority> its = userAuthorities
					.iterator();

			List<String> roles = new ArrayList<String>();
			while (its.hasNext()) {
				GrantedAuthority it = its.next();
				log.info("유저 ROLE : " + it.getAuthority());
				roles.add(it.getAuthority());
			}

			InetOrgPerson inetOrgPerson = (InetOrgPerson) detailsManager
					.loadUserByUsername(ldapUserDetails.getUsername());
			user.setUid(inetOrgPerson.getUid());
			user.setName(inetOrgPerson.getCn()[0]);
			user.setEmail(inetOrgPerson.getMail());

			if (inetOrgPerson.getUid().equals("voyaging")) {
				roles.add("ROLE_ADMIN");
			}

			String userRole = null;
			if (!roles.isEmpty()) {
				userRole = StringUtils.join(roles.toArray(), ",");
			}

			user.setRole(userRole);

			if (!userExsit(inetOrgPerson.getUid())) {
				ownerManager.createUser(user);
			} else {
				User dbUser = ownerManager.getUser(user.getUid());
				if (dbUser != null) {
					user.setId(dbUser.getId());
				}
			}
		}

		return "redirect:/";
	}

	private Boolean userExsit(String uid) {
		User user = ownerManager.getUser(uid);
		return user != null;
	}

	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public String createUser(@ModelAttribute("User") User user) {
		if (user == null) {
			throw new NullArgumentException("user");
		}

		user.setRole("ROLE_ADMIN,ROLE_USER");
		ownerManager.createUser(user);

		return "redirect:/login";
	}

	@RequestMapping(value = { "/authentication" })
	public String loginHandler(@ModelAttribute("user") User user) {
	/*	user.setUid("guest");
		user.setName("게스트");
		user.setEmail("voyaging@eyeq.co.kr");
		user.setRole("ROLE_ADMIN,ROLE_USER");
		ownerManager.createUser(user);*/
		System.out.println(user);
		return "redirect:/";
	}
}
