package com.example.common_resource.exception.login;

import java.io.Serial;

/**
 * Exception for Error Captcha
 */
public class CaptchaNotMatchException extends LoginException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CaptchaNotMatchException() {
        super("login.captcha.unmatched", null);
    }
}
