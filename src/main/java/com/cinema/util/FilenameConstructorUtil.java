package com.cinema.util;

import com.cinema.model.enums.ExportFormat;

import java.time.LocalDateTime;

public class FilenameConstructorUtil {
    public static String filename(
            String username,
            LocalDateTime start,
            LocalDateTime end,
            ExportFormat format) {

        String timestamp = start.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + "_to_" + end.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return String.format(
                "history-%s-%s.%s",
                username,
                timestamp,
                format.name().toLowerCase());
    }
    public static String filenameUpToNow(
            String username,
            ExportFormat format) {

        String timestamp = LocalDateTime.now()
                                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        return String.format(
                "history_%s_%s.%s",
                username,
                timestamp,
                format.name().toLowerCase());
    }

    public static String filenameUpToDate(
            String username,
            LocalDateTime end,
            ExportFormat format) {

        String timestamp = end.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"));

        return String.format(
                "history-%s-%s.%s",
                username,
                timestamp,
                format.name().toLowerCase());
    }
}
