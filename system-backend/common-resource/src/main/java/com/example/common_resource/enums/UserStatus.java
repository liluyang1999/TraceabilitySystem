package com.example.common_resource.enums;

public enum UserStatus {

    ENABLED(0, "User is normally enabled"),

    DISABLED(1, "User is temporarily disabled");

    private final Integer code;

    private final String info;

    UserStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getInfo() {
        return this.info;
    }
}
