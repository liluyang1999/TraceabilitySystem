package com.example.server_framework.server;

import com.example.common_resource.utils.ArithUtils;
import com.example.common_resource.utils.ServletUtils;
import com.example.common_resource.utils.network.IPUtils;
import com.example.server_framework.server.indicator.*;
import lombok.Data;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Code Reference: <a href="https://github.com/yangzongzhuan/RuoYi/blob/master/ruoyi-framework/src/main/java/com/ruoyi/framework/web/domain/Server.java">...</a>
 * Server Information
 */
@Component
@Data
public class ServerInfo {

    private static final int WAIT_SECOND = 1000;

    private Cpu cpu = new Cpu();

    private Mem mem = new Mem();

    private Jvm jvm = new Jvm();

    private Sys sys = new Sys();

    private List<SysFile> sysFiles = new LinkedList<>();

    public void load() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardLayer = systemInfo.getHardware();
        setCpuInfo(hardLayer.getProcessor());
        setMemInfo(hardLayer.getMemory());
        setJvmInfo();
        setSysInfo();
        setSysFiles(systemInfo.getOperatingSystem());
    }

    public String getPathDomain() {
        HttpServletRequest request = ServletUtils.getRequest();
        return getPathDomain(request);
    }

    private static String getPathDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length())
                .append(contextPath).toString();
    }

    private void setCpuInfo(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();

        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];

        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCoreNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSystem(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setFree(memory.getAvailable());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
    }

    private void setJvmInfo() {
        Properties props = System.getProperties();
        jvm.setHomePath(props.getProperty("java.home"));
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
    }

    private void setSysInfo() {
        Properties properties = System.getProperties();
        sys.setComputerIp(IPUtils.getHostIpAddress());
        sys.setComputerName(IPUtils.getHostName());
        sys.setUserDir(properties.getProperty("login.dir"));
        sys.setOsArch(properties.getProperty("os.arch"));
        sys.setOsName(properties.getProperty("os.name"));
    }

    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(ArithUtils.multiply(ArithUtils.divide(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    private String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
