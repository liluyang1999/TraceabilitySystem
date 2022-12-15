package com.example.common_resource.enums;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {

    GET, POST, PUT, HEAD, PATCH, DELETE, OPTIONS, TRACE;

    private static final Map<String, HttpMethod> TypeMappings = new HashMap<>();

    static {
        for (HttpMethod httpMethod : values()) {
            TypeMappings.put(httpMethod.name(), httpMethod);
        }
    }

    public static HttpMethod getHttpType(@Nullable String type) {
        return ((type != null) ? TypeMappings.get(type) : null);
    }
}
