package com.example.common_resource.exception.login;

import java.io.Serial;

/**
 * Exception for User's Incorrect Password
 */
public class LoginPasswordNotMatchException extends LoginException {

    @Serial
    private static final long serialVersionUID = 1L;

    public LoginPasswordNotMatchException() {
        super("login.password.unmatched", null);
    }
}