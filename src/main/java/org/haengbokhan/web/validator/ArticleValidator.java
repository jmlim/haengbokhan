package org.haengbokhan.web.validator;

import org.haengbokhan.model.Article;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class ArticleValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		if (Article.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required");
		ValidationUtils
				.rejectIfEmptyOrWhitespace(errors, "content", "required");
	}

}
