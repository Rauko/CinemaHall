package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.cinema.exception.ExportFailedException;
import com.cinema.model.enums.ExportFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TxtExportService {

    private static final Logger log = LoggerFactory.getLogger(TxtExportService.class);

    public byte[] export(List<PurchaseHistoryExportDto> data) {

        try {
            log.info("Starting TXT export: recordsCount={}", data != null ? data.size() : 0);

            if (data == null) {
                data = List.of();
            }

            StringBuilder sb = new StringBuilder();

            sb.append("Purchase history\n");
            sb.append("================\n\n");

            for (PurchaseHistoryExportDto d : data) {
                sb.append("Movie: ").append(d.movieTitle()).append("\n");
                sb.append("Hall: ").append(d.hallName()).append("\n");
                sb.append("Seat: ").append(d.seat()).append("\n");
                sb.append("Price: ").append(d.price()).append("\n");
                sb.append("Time: ").append(d.purchaseTime()).append("\n");
                sb.append("----------------\n");
            }

            log.info("TXT export successful: recordsCount={}", data.size());

            return sb.toString().getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {

            log.error("TXT export failed: format={}, recordsCount={}, message={}",
                    ExportFormat.TXT,
                    data != null ? data.size() : 0,
                    e.getMessage(),
                    e
            );

            throw new ExportFailedException(ExportFormat.TXT);
        }
    }
}
