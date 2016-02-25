package org.haengbokhan.web.validator;

import org.haengbokhan.model.ImageReply;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class ImageReplyValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		if (ImageReply.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils
				.rejectIfEmptyOrWhitespace(errors, "content", "required");
	}

}
