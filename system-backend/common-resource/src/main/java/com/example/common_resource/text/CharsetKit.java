package com.example.common_resource.text;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utility of Charset
 */
public class CharsetKit {

    public static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;

    public static final Charset CHARSET_UTF_16 = StandardCharsets.UTF_16;

    public static final Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;

    public static final String UTF_8 = "UTF-8";

    public static final String UTF_16 = "UTF-16";

    public static final String ISO_8859_1 = "ISO-8859-1";

    public static String systemCharset() {
        return Charset.defaultCharset().name();
    }

    public static Charset getCharset(String charset) {
        return StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset);
    }

    public static String convertCharset(String source, String srcCharset, String destCharset) {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    private static String convert(String source, Charset srcCharset, Charset destCharset) {
        if (srcCharset == null) {
            srcCharset = StandardCharsets.UTF_8;
        }
        if (destCharset == null) {
            destCharset = StandardCharsets.UTF_8;
        }
        if (StringUtils.isEmpty(source) || srcCharset.equals(destCharset)) {
            return source;
        }
        return new String(source.getBytes(srcCharset), destCharset);
    }
}
