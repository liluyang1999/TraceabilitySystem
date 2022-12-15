package com.example.common_resource.text;

import org.apache.commons.lang3.StringUtils;

/**
 * Formatting String
 */
public class StrFormatter {

    public static final String PLACEHOLDER = "{}";
    public static final char BACKSLASH = '\\';
    public static final char DELIM_START = '{';
    public static final char DELIM_END = '}';

    /*
     * format("this is {} for {}", "a", "b") -> this is a for b<br>
     */
    public static String format(final String strPattern, final Object... argArray) {
        if (StringUtils.isEmpty(strPattern) || argArray == null || argArray.length == 0) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();

        StringBuilder builder = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;  // the current position
        int delimIndex;  // index of place holder
        for (Object arg : argArray) {
            delimIndex = strPattern.indexOf(PLACEHOLDER, handledPosition);
            if (delimIndex == -1) {
                if (handledPosition == 0) {
                    return strPattern;
                } else {
                    builder.append(strPattern, handledPosition, strPatternLength);
                    return builder.toString();
                }
            } else {
                builder.append(strPattern, handledPosition, delimIndex);
                builder.append(arg);
                handledPosition = delimIndex + 2;
            }
        }
        builder.append(strPattern, handledPosition, strPattern.length());
        return builder.toString();
    }
}
