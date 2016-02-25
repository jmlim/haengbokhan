package org.haengbokhan.web.controller;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.haengbokhan.model.User;
import org.haengbokhan.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Administrator
 * 
 */
@Controller
// @SessionAttributes(value = { "user" }, types = { User.class })
public class SignController {

	private Log log = LogFactory.getLog(getClass());

	@Autowired(required = false)
	private UserDetailsManager detailsManager;

	@Autowired
	private UserManager ownerManager;

	@RequestMapping(value = { "/sign/signin" }, method = RequestMethod.GET)
	public String signinPageHandler(Model model) {
		return "/pages/sign/signin";
	}

	@RequestMapping(value = { "/sign/signup" }, method = RequestMethod.GET)
	public String signupPageHandler(Model model) {
		return "/pages/sign/signup";
	}

	/*
	 * @RequestMapping(value = { "/ldap/authentication" }) public String
	 * ldapLoginHandler(@ModelAttribute("user") User user) { SecurityContext
	 * securityContext = SecurityContextHolder.getContext(); Authentication
	 * authentication = securityContext.getAuthentication(); if (authentication
	 * != null) { LdapUserDetails ldapUserDetails = (LdapUserDetails)
	 * authentication .getPrincipal(); Collection<? extends GrantedAuthority>
	 * userAuthorities = ldapUserDetails .getAuthorities(); Iterator<? extends
	 * GrantedAuthority> its = userAuthorities .iterator();
	 * 
	 * List<String> roles = new ArrayList<String>(); while (its.hasNext()) {
	 * GrantedAuthority it = its.next(); log.info("유저 ROLE : " +
	 * it.getAuthority()); roles.add(it.getAuthority()); }
	 * 
	 * InetOrgPerson inetOrgPerson = (InetOrgPerson) detailsManager
	 * .loadUserByUsername(ldapUserDetails.getUsername());
	 * user.setUid(inetOrgPerson.getUid());
	 * user.setName(inetOrgPerson.getCn()[0]);
	 * user.setEmail(inetOrgPerson.getMail());
	 * 
	 * if (inetOrgPerson.getUid().equals("voyaging")) { roles.add("ROLE_ADMIN");
	 * }
	 * 
	 * String userRole = null; if (!roles.isEmpty()) { userRole =
	 * StringUtils.join(roles.toArray(), ","); }
	 * 
	 * // user.setRole(userRole);
	 * 
	 * if (!userExsit(inetOrgPerson.getUid())) { ownerManager.createUser(user);
	 * } else { User dbUser = ownerManager.getUser(user.getUid()); if (dbUser !=
	 * null) { user.setId(dbUser.getId()); } } }
	 * 
	 * return "redirect:/"; }
	 */
	/*
	 * private Boolean userExsit(String uid) { User user =
	 * ownerManager.getUser(uid); return user != null; }
	 */

	@RequestMapping(value = "/sign/createUser", method = RequestMethod.POST)
	public String createUser(@ModelAttribute("User") User user) {
		if (user == null) {
			throw new NullArgumentException("user");
		}

		ownerManager.createUser(user);

		return "redirect:/signin";
	}

	@RequestMapping(value = { "/sign/authentication" })
	public String signinHandler(@ModelAttribute("user") User user) {
		/*
		 * user.setUid("guest"); user.setName("게스트");
		 * user.setEmail("voyaging@eyeq.co.kr");
		 * user.setRole("ROLE_ADMIN,ROLE_USER"); ownerManager.createUser(user);
		 */
		System.out.println(user);
		return "redirect:/";
	}
}
