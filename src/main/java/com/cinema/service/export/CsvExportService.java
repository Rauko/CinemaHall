package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CsvExportService {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] export(List<PurchaseHistoryExportDto> data) {

        StringBuilder sb = new StringBuilder();

        //Header
        sb.append("Movie,Hall,Seat,Price,Time,Date\n");

        for (PurchaseHistoryExportDto dto : data) {
            LocalDateTime dt = dto.purchaseTime();

            sb.append(escape(dto.movieTitle())).append(",")
              .append(escape(dto.hallName())).append(",")
              .append(escape(dto.seat())).append(",")
              .append(escape(String.valueOf(dto.price()))).append(",")
              .append(dt.format(TIME_FORMAT)).append(",")
              .append(dt.format(DATE_FORMAT))
              .append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String s) {
        if (s==null) {
            return "";
        }

        boolean sontainsSpecial =
                s.contains(",") ||
                s.contains("\"") ||
                s.contains("\r") ||
                s.contains("\n");

        if (sontainsSpecial) {
            s = s.replace("\"","\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
