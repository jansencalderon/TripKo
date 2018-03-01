package com.tripko.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

    private static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    private static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String toReadable(String dateToConvert) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            DateFormat outputFormat = new SimpleDateFormat("E, MMM dd", Locale.US);
            Date date = inputFormat.parse(dateToConvert);
            return outputFormat.format(date);
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toReadableComplete(String dateToConvert) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.US);
            DateFormat outputFormat = new SimpleDateFormat("E, MMM dd' at 'h:mm a", Locale.US);
            Date date = inputFormat.parse(dateToConvert);
            return outputFormat.format(date);
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toDuration(Date date) {
        try {
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(date); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 8); // adds 8 hour
            // returns new date object, one hour in the future
            Date mDate = cal.getTime();
            Date currentDate = new Date();
            long duration = currentDate.getTime() - mDate.getTime();

            StringBuilder res = new StringBuilder();
            for (int i = 0; i < times.size(); i++) {
                Long current = times.get(i);
                long temp = duration / current;
                if (temp > 0) {
                    res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                    break;
                }
            }
            if ("".equals(res.toString()))
                return "just a moment ago";
            else
                return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "unable to parse data";
        }
    }

    public static String getLongDateTimeString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(date);
    }


    public static String TO_AM_PM(String dateToConvert) {

        DateFormat f1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String convertedDate = "";
        try {
            Date date = f1.parse(dateToConvert);
            SimpleDateFormat am_pm = new SimpleDateFormat("h:mm a", Locale.US);
            convertedDate = am_pm.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    public static Date String_To_Time(String dateToConvert) {

        DateFormat f1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date convertedDate = null;
        try {
            convertedDate = f1.parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    public static String dateToday() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
    }

    public static String dateTodayToast() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = sdfs.format(calendar.getTime()) + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date converted = null;
        try {
            converted = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(converted);
    }

    public static Date getDateToday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = sdfs.format(calendar.getTime()) + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        ;
        Date converted = null;
        try {
            converted = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted;
    }

    public static Date getDateTodayEnd() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = sdfs.format(calendar.getTime()) + " 23:59:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date converted = null;
        try {
            converted = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted;
    }


}