package com.example.common_resource.utils.network;

import org.springframework.util.StringUtils;

/**
 * Code Reference: <a href="https://github.com/yangzongzhuan/RuoYi/blob/master/ruoyi-common/src/main/java/com/ruoyi/common/utils/html/EscapeUtil.java">...</a>
 * Transitive and Anti-transitive
 */
public class EscapeUtil {

    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

    private static final char[][] TEXT = new char[64][];

    static {
        for (int i = 0; i < 64; i++) {
            TEXT[i] = new char[] { (char) i };
        }
        // special HTML characters
        TEXT['\''] = "&#039;".toCharArray(); // 单引号
        TEXT['"'] = "&#34;".toCharArray(); // 单引号
        TEXT['&'] = "&#38;".toCharArray(); // &符
        TEXT['<'] = "&#60;".toCharArray(); // 小于号
        TEXT['>'] = "&#62;".toCharArray(); // 大于号
    }

    // Transform HTML characters into secure character
    public static String escape(String text) {
        return encode(text);
    }


    // Restore the transformed characters
    public static String unescape(String content) {
        return decode(content);
    }

    // Clean all the labels but keep the inside content
    public static String clean(String content) {
        return new HTMLFilter().filter(content);
    }

    // Encoding HTML
    private static String encode(String text) {
        if (text == null || text.length() == 0) return null;

        int len = text.length();
        StringBuilder buffer = new StringBuilder(len + (len >> 2));

        char c;
        for (int i = 0; i < len; i++) {
            c = text.charAt(i);
            if (c < 64) {
                buffer.append(TEXT[c]);
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    // Decode HTML
    public static String decode(String content) {
        if (!StringUtils.hasText(content)) return content;

        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < content.length()) {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                } else {
                    tmp.append(content.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
}
