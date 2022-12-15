package com.example.common_resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {

    private static String name;

    private static String version;

    private static String copyright;

    private static Boolean addressEnabled;

    private static String baseFilePath;

    @Value("${system.name}")
    public void setName(String name) {
        SystemConfig.name = name;
    }

    @Value("${system.version}")
    public void setVersion(String version) {
        SystemConfig.version = version;
    }

    @Value("${system.copyright}")
    public void setCopyright(String copyright) {
        SystemConfig.copyright = copyright;
    }

    @Value("${system.addressEnabled}")
    public static void setAddressEnabled(Boolean addressEnabled) {
        SystemConfig.addressEnabled = addressEnabled;
    }

    @Value("${system.baseFilePath}")
    public static void setBaseFilePath(String baseFilePath) {
        SystemConfig.baseFilePath = baseFilePath;
    }

    public static String getName() {
        return name;
    }

    public static String getVersion() {
        return version;
    }

    public static String getCopyright() {
        return copyright;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public static String getBaseFilePath() {
        return baseFilePath;
    }
}