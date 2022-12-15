package com.example.server_framework.server.indicator;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Server System Files Information
 */
@Data
public class SysFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String dirName;

    private String sysTypeName;

    private String typeName;

    private String total;

    private String free;

    private String used;

    private Double usage;
}
