package com.example.common_resource.exception.file;

import com.example.common_resource.exception.BaseException;

import java.io.Serial;

/**
 * Exception for File Information
 */
public class FileException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }
}
