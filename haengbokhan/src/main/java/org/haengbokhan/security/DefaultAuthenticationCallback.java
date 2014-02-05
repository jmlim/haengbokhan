package org.haengbokhan.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service("authenticationCallback")
public class DefaultAuthenticationCallback implements AuthenticationCallback {

	@Override
	public void onSuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult) {
		// TODO Auto-generated method stub
		System.out.println("success");
	}

	@Override
	public void onUnsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed) {
		// TODO Auto-generated method stub
		System.out.println("unsuccess");
		
	}

}
