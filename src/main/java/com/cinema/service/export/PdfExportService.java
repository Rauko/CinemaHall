package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.cinema.exception.ExportFailedException;
import com.cinema.model.enums.ExportFormat;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfExportService {

    private static final Logger log = LoggerFactory.getLogger(PdfExportService.class);

    public byte[] export(List<PurchaseHistoryExportDto> data) {

        if (data == null) {
            log.warn("PDF export called with null data");
            data = List.of();
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, out);
                document.open();

                log.info("Starting PDF export: recordsCount={}", data.size());

                document.add(new Paragraph("Purchase History"));
                document.add(new Paragraph("================"));

                for (PurchaseHistoryExportDto d : data) {
                    document.add(new Paragraph(
                            String.format(
                                    "Movie: %s\nHall: %s\nSeat: %s\nPrice: %s\nTime: %s\n",
                                    d.movieTitle(),
                                    d.hallName(),
                                    d.seat(),
                                    d.price(),
                                    d.purchaseTime()
                            )
                    ));
                    document.add(new Paragraph("----------------"));
                }

                log.info("PDF export successful: recordsCount={}", data.size());

            } finally {
                document.close(); // Document не AutoCloseable ❗
            }

            return out.toByteArray();

        } catch (Exception e) {

            log.error("PDF export failed: format={}, recordsCount={}, message={}",
                    ExportFormat.PDF,
                    data.size(),
                    e.getMessage(),
                    e
            );

            throw new ExportFailedException(ExportFormat.PDF);
        }
    }
}
