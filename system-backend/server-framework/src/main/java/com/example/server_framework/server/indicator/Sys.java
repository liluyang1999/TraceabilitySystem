package com.example.server_framework.server.indicator;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Server System Information
 */
@Data
public class Sys implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String computerName;

    private String computerIp;

    private String userDir;

    private String osName;

    private String osArch;
}
