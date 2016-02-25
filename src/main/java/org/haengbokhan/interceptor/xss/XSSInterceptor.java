package org.haengbokhan.interceptor.xss;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.haengbokhan.utils.PatternUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class XSSInterceptor extends HandlerInterceptorAdapter {

	private boolean matchesXSSAttack(String value) {
		Pattern[] patterns = PatternUtils.xssPattern;
		if (value != null) {

			for (Pattern scriptPattern : patterns) {
				if (scriptPattern.matcher(value).matches()) {
					return scriptPattern.matcher(value).matches();
				}
			}
		}
		return false;
	}

	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Map<String, String[]> params = request.getParameterMap();

		String inValidString = null;
		String name = null;
		loop: for (String[] values : params.values()) {
			for (String value : values) {
				if (matchesXSSAttack(value)) {
					inValidString = value;
					break loop;
				}
			}
		}
		if (inValidString != null) {
			String message = name + " 항목에 유효하지 않은 단어(" + inValidString
					+ ")가 포함되어 크로스 서버 스크립팅(XSS)검사에 실패하였습니다.";
			Exception e = new RuntimeException(message);
			request.setAttribute("error", e);
			request.setAttribute("uri", request.getRequestURI());
			request.setAttribute("message", message);
			request.setAttribute("exception_type", "RuntimeException");
			throw e;
		}

		return super.preHandle(request, response, handler);
	}

	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

}
