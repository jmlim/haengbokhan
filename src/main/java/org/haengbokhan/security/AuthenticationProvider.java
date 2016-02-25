package org.haengbokhan.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationProvider extends UsernamePasswordAuthenticationToken {

	public AuthenticationProvider(Object principal, Object credentials) {
		super(principal, credentials);
		// TODO Auto-generated constructor stub
	}

}
