package com.airtribe.meditrack.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private void DateUtil() {}

    public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter CSV_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static String format(LocalDateTime dt) {
        return dt != null ? dt.format(DISPLAY_FORMAT) : "N/A";
    }

    public static String formatForCsv(LocalDateTime dt) {
        return dt != null ? dt.format(CSV_FORMAT) : "";
    }

    public static LocalDateTime parse(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr.trim(), DISPLAY_FORMAT);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(dateTimeStr.trim(), CSV_FORMAT);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format. Use: yyyy-MM-dd HH:mm");
            }
        }
    }

    public static boolean isFuture(LocalDateTime dt) {
        return dt != null && dt.isAfter(LocalDateTime.now());
    }

    public static boolean isPast(LocalDateTime dt) {
        return dt != null && dt.isBefore(LocalDateTime.now());
    }
}
