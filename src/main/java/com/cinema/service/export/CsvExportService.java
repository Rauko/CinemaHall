package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.cinema.exception.ExportFailedException;
import com.cinema.model.enums.ExportFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CsvExportService {

    private static final Logger log = LoggerFactory.getLogger(CsvExportService.class);

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] export(List<PurchaseHistoryExportDto> data) {

        if (data == null) {
            log.warn("CSV export called with null data");
            data = List.of();
        }

        try {
            log.info("Starting CSV export: recordsCount={}", data.size());

            StringBuilder sb = new StringBuilder();

            // Header
            sb.append("Movie,Hall,Seat,Price,Time,Date\n");

            for (PurchaseHistoryExportDto dto : data) {

                if (dto == null) {
                    log.warn("Skipping null record in CSV export");
                    continue;
                }

                LocalDateTime dt = dto.purchaseTime();

                sb.append(escape(dto.movieTitle())).append(",")
                        .append(escape(dto.hallName())).append(",")
                        .append(escape(dto.seat())).append(",")
                        .append(escape(String.valueOf(dto.price()))).append(",");

                if (dt != null) {
                    sb.append(dt.format(TIME_FORMAT)).append(",")
                            .append(dt.format(DATE_FORMAT));
                } else {
                    log.warn("Missing purchaseTime for record: movie={}", dto.movieTitle());
                    sb.append(","); // пустые значения
                }

                sb.append("\n");
            }

            log.info("CSV export successful: recordsCount={}", data.size());

            return sb.toString().getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {

            log.error("CSV export failed: format={}, recordsCount={}, message={}",
                    ExportFormat.CSV,
                    data.size(),
                    e.getMessage(),
                    e
            );

            throw new ExportFailedException(ExportFormat.CSV);
        }
    }

    private String escape(String s) {
        if (s == null) {
            return "";
        }

        boolean containsSpecial =
                s.contains(",") ||
                        s.contains("\"") ||
                        s.contains("\r") ||
                        s.contains("\n");

        if (containsSpecial) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}
