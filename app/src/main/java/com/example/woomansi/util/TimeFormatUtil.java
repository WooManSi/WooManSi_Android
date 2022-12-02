package com.example.woomansi.util;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeFormatUtil {

    public static LocalTime stringToTime(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(s, formatter);
    }

    public static String timeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String timeToString(int hour, int minute) {
        return timeToString(LocalTime.of(hour, minute));
    }

    public static String formatWithAmPm(int hour, int minute) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a HH:mm");
        return LocalTime.of(hour, minute).format(formatter);
    }
}
