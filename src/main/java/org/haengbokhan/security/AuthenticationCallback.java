package org.haengbokhan.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationCallback {

	void onSuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult);

	void onUnsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed);
}
