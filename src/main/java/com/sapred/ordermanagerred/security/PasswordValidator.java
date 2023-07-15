package com.sapred.ordermanagerred.security;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?!.*\\s).{8,20}$";
    private RegexValidator validator;

    public PasswordValidator() {
        validator = new RegexValidator(PASSWORD_REGEX);
    }

    public boolean isValid(String password) {
        return validator.isValid(password);
    }
}
