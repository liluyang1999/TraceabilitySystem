package com.example.common_resource.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Utility of servlet
 */
public class ServletUtils {

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static Integer conParamToInt(String name) {
        return Integer.parseInt(getRequest().getParameter(name));
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static void renderString(HttpServletResponse response, String jsonStr) {
        try {
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
