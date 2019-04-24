package com.base.app.domain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatterDate {
    public final static SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy",
            new Locale("in"));
    public final static SimpleDateFormat formatWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat formatWithHour = new SimpleDateFormat("yyyy MMMM dd - HH:mm");
    public final static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat formatDateReverse = new SimpleDateFormat("dd-MM-yyyy");
    public final static SimpleDateFormat formatDateSlash = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    public static final String changeDate(String sDate) {
        String result = "";
        if (sDate != null) {
            try {
                Date date = formatDateSlash.parse(sDate);
                result = formatWithHour.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static final String changeDateMonthText(String sDate) {
        String result = "";
        if (sDate != null) {
            try {
                Date date = formatDateReverse.parse(sDate);
                result = format.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static final long dateToLong(String sDate) {
        try {
            Date d = formatDate.parse(sDate);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static final long dateTimeToLong(String sDate) {
        try {
            Date d = formatDateSlash.parse(sDate);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static final String formatDate(long date) {
        return format.format(new Date(date));
    }

    public static final String formatDate(long date, String format) {
        SimpleDateFormat formatDate = new SimpleDateFormat(format,
                new Locale("in"));
        return formatDate.format(new Date(date));
    }

    public static String parseTimeNotification(String date) {
        long time = dateTimeToLong(date);
        long divTime = System.currentTimeMillis() - time;

        long diffSeconds = divTime / 1000 % 60;
        long diffMinutes = divTime / (60 * 1000) % 60;
        long diffHours = divTime / (60 * 60 * 1000) % 24;
        long diffDays = divTime / (24 * 60 * 60 * 1000);

        if (diffDays == 1) {
            return "Yesterday";
        } else if (diffMinutes == 5 || diffDays < 1) {
            return "Today";
        } else if (diffMinutes < 5) {
            return "Just Now";
        } else {
            return date;
        }
    }
}
