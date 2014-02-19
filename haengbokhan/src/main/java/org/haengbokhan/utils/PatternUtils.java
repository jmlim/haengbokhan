package org.haengbokhan.utils;

import java.util.regex.Pattern;

/**
 * @author Administrator
 * 
 */
public class PatternUtils {

	public static final String EMAIL = "^[a-zA-Z0-9]+([\\.a-zA-Z0-9]+){0,}@[a-zA-Z0-9]+(\\.[a-zA-Z]+){1,2}";

	public static final Pattern[] xssPattern = new Pattern[] {
			// Script fragments
			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			// src='...'
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL),
			// lonely script tags
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// eval(...)
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// expression(...)
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL),
			// javascript:...
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			// vbscript:...
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			// onload(...)=...
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL) };
}
