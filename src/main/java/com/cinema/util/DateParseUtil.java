package com.cinema.util;

import com.cinema.exception.InvalidDateFormatException;

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
            throw new InvalidDateFormatException("start");
        }
    }

    public static LocalDateTime parseEndDate(String end){
        try {
            return LocalDateTime.parse(end)
                    .toLocalDate()
                    .atTime(LocalTime.MAX);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("end");
        }
    }
}
