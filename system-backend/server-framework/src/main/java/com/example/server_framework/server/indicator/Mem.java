package com.example.server_framework.server.indicator;

import com.example.common_resource.utils.ArithUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Memory Information
 */
@Data
public class Mem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private double total;

    private double used;

    private double free;

    private double usage;

    public double getTotal() {
        return ArithUtils.divide(total, (1024 * 1024 * 1024), 2);
    }

    public double getUsed() {
        return ArithUtils.divide(used, (1024 * 1024 * 1024), 2);
    }

    public double getFree() {
        return ArithUtils.divide(free, (1024 * 1024 * 1024), 2);
    }

    public double getUsage() {
        return ArithUtils.multiply(ArithUtils.divide(used, total, 4), 100);
    }
}
