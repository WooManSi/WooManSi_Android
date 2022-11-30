package com.example.woomansi.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    public static LocalDateTime stringToDate(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(s, formatter);
        return LocalDate.now().atTime(time);
    }

    public static String dateToString(LocalTime time) {
        return dateToString(time.getHour(), time.getMinute());
    }

    public static String dateToString(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }
}
