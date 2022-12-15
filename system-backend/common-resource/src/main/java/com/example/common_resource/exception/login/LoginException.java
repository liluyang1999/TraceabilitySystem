package com.example.common_resource.exception.login;

import com.example.common_resource.exception.BaseException;

import java.io.Serial;

/**
 * Exception Related to User
 */
public class LoginException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public LoginException(String code, Object[] args) {
        super("login", code, args, null);
    }
}
