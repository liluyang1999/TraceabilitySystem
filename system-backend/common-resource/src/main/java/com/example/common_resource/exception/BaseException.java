package com.example.common_resource.exception;

import com.example.common_resource.utils.SpringUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;

/**
 * Base Exception
 */
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private String module;
    private String code;
    private Object[] args;
    private String message;

    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.hasText(code)) {
            message = SpringUtils.message(code, args);
        }
        if (message != null) {
            this.message = message;
        }
        return message;
    }

    public BaseException(String module, String code, Object[] args, String message) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.message = message;
    }

    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public BaseException(String module, String message) {
        this(module, null, null, message);
    }

    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public BaseException(String message) {
        this(null, null, null, message);
    }

    public String getDefaultMessage()
    {
        return this.message;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
