package com.example.common_resource.exception.file;

import java.io.Serial;

/**
 * Exception for File Name's Excessive Length
 */
public class FileNameLengthLimitExceededException extends FileException {

    @Serial
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultLength) {
        super("upload.file.length", new Object[] { defaultLength });
    }
}
