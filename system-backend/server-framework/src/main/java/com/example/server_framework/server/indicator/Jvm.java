package com.example.server_framework.server.indicator;

import com.example.common_resource.utils.ArithUtils;
import com.example.common_resource.utils.DateUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * JVM Information
 */
@Data
public class Jvm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private double total;

    private double max;

    private double free;

    private double usage;

    private String version;

    private String homePath;

    public double getTotal() {
        return ArithUtils.divide(total, (1024 * 1024), 2);
    }

    public double getUsed() {
        return ArithUtils.divide(total - free, (1024 * 1024), 2);
    }

    public double getFree() {
        return ArithUtils.divide(free, (1024 * 1024), 2);
    }

    public double getMax() {
        return ArithUtils.divide(max, (1024 * 1024), 2);
    }

    public double getUsage() {
        return ArithUtils.multiply(ArithUtils.divide(total - free, total, 4), 100);
    }

    public String getStartDateTime() {
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getServerStartDate());
    }

    public String getRunTime() {
        return DateUtils.getDateDiff(new Date(), DateUtils.getServerStartDate());
    }

    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }
}
