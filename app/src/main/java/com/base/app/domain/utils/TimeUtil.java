package com.base.app.domain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cahaya on 11/21/16.
 */

public class TimeUtil {
    public static final String TAG = "TimeUtil";


    public TimeUtil() {

    }

    public static String formatDate(String unFormattedDate) {

        if (unFormattedDate != null) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(unFormattedDate);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd/yyyy");

            return simpleDateFormat1.format(date);

        }
        return "";

    }

    public static String formatTime(int hourOfDay, int minute, int second) {

        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;

        return hourString + ":" + minuteString + ":" + secondString;
    }

    public static String getCurrentDate() {

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);

        String monthString = month < 10 ? "0" + month : "" + month;
        String dayString = day < 10 ? "0" + day : "" + day;
        String date = monthString + "/" + dayString + "/" + year;

        return date;
    }

    public static String getCurrentTime() {

        Calendar now = Calendar.getInstance();

        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);

        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0"+second : ""+second;

        String time = hourString + ":" + minuteString + ":" + secondString;

        return time;
    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        try {
            cal.setTime(s.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.add(Calendar.DATE, days);
        SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy");
        String output = s1.format(cal.getTime());

        return output;
    }

}