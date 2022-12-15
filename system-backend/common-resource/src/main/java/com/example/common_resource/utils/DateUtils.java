package com.example.common_resource.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

/**
 * Utility of Date
 */
public class DateUtils {

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String YYYY_MM_DD1 = "yyyy-MM-dd";

    public static String YYYY_MM_DD2 = "yyyy/MM/dd";

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
    };

    public static String getCurrentDateTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static Date parseDateStr(String dateStr) {
        if (dateStr == null) return null;
        try {
            return parseDate(dateStr, parsePatterns);
        }
        catch (ParseException e) {
            return null;
        }
    }

    private static String dateTimeNow(final String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String getCurrentDate() {
        return dateTimeNow(YYYY_MM_DD1);
    }

    public static String getPathDate() {
        return dateTimeNow(YYYY_MM_DD2);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    public static String getDateDiff(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        return day + "days" + hour + "hours" + min + "minutes";
    }
}
