package com.example.server_framework.server.indicator;

import com.example.common_resource.utils.ArithUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CPU Information
 */
@Data
public class Cpu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int coreNum;

    private double total;

    private double system;

    private double used;

    private double free;

    private double wait;

    private double usage;

    public double getTotal() {
        return ArithUtils.round(ArithUtils.multiply(total, 100), 2);
    }

    public double getSystemRate() {
        return ArithUtils.round(ArithUtils.multiply(system / total, 100), 2);
    }

    public double getUsedRate() {
        return ArithUtils.round(ArithUtils.multiply(used / total, 100), 2);
    }

    public double getFreeRate() {
        return ArithUtils.round(ArithUtils.multiply(free / total, 100), 2);
    }

    public double getWaitRate() {
        return ArithUtils.round(ArithUtils.multiply(wait / total, 100), 2);
    }

    public double getUsage() {
        return ArithUtils.round(getUsedRate() + getSystemRate(), 2);
    }
}
