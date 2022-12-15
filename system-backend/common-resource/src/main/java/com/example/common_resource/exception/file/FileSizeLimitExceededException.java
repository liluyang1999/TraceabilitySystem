package com.example.common_resource.exception.file;

import java.io.Serial;

/**
 * Exception for File Excessive Size
 */
public class FileSizeLimitExceededException extends FileException {

    @Serial
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultSize) {
        super("upload.file.size", new Object[] { defaultSize });
    }
}
