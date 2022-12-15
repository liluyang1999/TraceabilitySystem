package com.example.common_resource.exception.login;

import java.io.Serial;

/**
 * Exception for Expired Captcha
 */
public class CaptchaExpiredException extends LoginException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CaptchaExpiredException() {
        super("login.captcha.expired", null);
    }
}
