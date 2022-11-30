package com.example.woomansi.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    public static LocalTime stringToDate(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(s, formatter);
    }

    public static String dateToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String dateToString(int hour, int minute) {
        return dateToString(LocalTime.of(hour, minute));
    }

    public static String formatWithAmPm(int hour, int minute) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a HH:mm");
        return LocalTime.of(hour, minute).format(formatter);
    }
}
