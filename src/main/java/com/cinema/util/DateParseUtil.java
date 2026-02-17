package com.cinema.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class DateParseUtil {
    public static LocalDateTime parseStartDate(String start){
        try {
           return LocalDateTime.parse(start)
                    .toLocalDate()
                    .atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid start date format");
        }
    }

    public static LocalDateTime parseEndDate(String start){
        try {
            return LocalDateTime.parse(start)
                    .toLocalDate()
                    .atTime(LocalTime.MAX);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid end date format");
        }
    }
}
