package org.haengbokhan.security;

import java.util.ArrayList;
import java.util.List;

import org.haengbokhan.model.User;
import org.haengbokhan.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS)
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserManager userManager;

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		// TODO Auto-generated method stub

		try {
			User user = userManager.getUser(userName);
			return new UserPrincipal(user, getAuthorities());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authList = getGrantedAuthorities();
		return authList;
	}

	public List<String> getRoles() {
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		roles.add("ROLE_ADMIN");
		return roles;
	}

	public List<GrantedAuthority> getGrantedAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		return authorities;
	}

}
