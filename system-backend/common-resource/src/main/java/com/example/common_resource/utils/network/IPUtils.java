package com.example.common_resource.utils.network;

import com.example.common_resource.config.SystemConfig;
import com.example.common_resource.utils.MD5Utils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Code Reference: <a href="https://github.com/yangzongzhuan/RuoYi/blob/master/ruoyi-common/src/main/java/com/ruoyi/common/utils/IpUtils.java">...</a>
 * Handling IP address
 */
public class IPUtils {

    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    public static String getIpAddress(HttpServletRequest request) {
        if (request == null)  return "unknown";
        String ipAddr = request.getHeader("x-forwarded-for");
        if (!StringUtils.hasText(ipAddr) || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("X-Forwarded-For");
        }
        if (!StringUtils.hasText(ipAddr) || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ipAddr) || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ipAddr) || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ipAddr) || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ipAddr) ? "127.0.0.1" : EscapeUtil.clean(ipAddr);
    }

    public static String getHostIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            return "unknown";
        }
    }

    // Check if the ip address is internal
    public static boolean isInternalIp(String ipAddr) {
        if ("127.0.0.1".equals(ipAddr)) return true;

        byte[] addr = toNumericFormat(ipAddr);
        if (addr == null || addr.length < 2) return true;
        final byte b0 = addr[0];
        final byte b1 = addr[1];

        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;

        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) return true;
                if (b1 == SECTION_6) return true;
            default: return false;
        }
    }

    // Transform ipv4 address from string to byte[4]
    public static byte[] toNumericFormat(String ipAddr) {
        if (ipAddr.length() == 0) return null;

        byte[] bytes = new byte[4];
        String[] elems = ipAddr.split("\\.", -1);
        try {
            int i;
            long l;
            switch (elems.length) {
                case 1:
                    l = Long.parseLong(elems[0]);
                    if ((l < 0L) || (l > 4294967295L)) return null;
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elems[0]);
                    if ((l < 0L) || (l > 255L)) return null;
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elems[1]);
                    if ((l < 0L) || (l > 16777215L)) return null;
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elems[i]);
                        if ((l < 0L) || (l > 255L)) return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elems[2]);
                    if ((l < 0L) || (l > 65535L)) return null;
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; i++) {
                        l = Integer.parseInt(elems[i]);
                        if ((l < 0L) || (l > 255L)) return null;
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default: return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    // Query IP address
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    public static String getLocationByIP(String ip) {
        // the inner network won't be queried
        if (IPUtils.isInternalIp(ip)) {
            return "Internal IP Address";
        }
        if (SystemConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.sendByGet(IP_URL, "ip=" + ip + "&json=true", "UTF-8");
                if (!StringUtils.hasText(rspStr)) {
                    logger.error("Exception happened when achieving ip address {}", ip);
                    return "unknown";
                }
                Gson gson = new Gson();
                String region = gson.fromJson("pro", String.class);
                String city = gson.fromJson("city", String.class);
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                logger.error("Exception happened when achieving ip address {}", ip);
            }
        }
        return "unknown";
    }
}