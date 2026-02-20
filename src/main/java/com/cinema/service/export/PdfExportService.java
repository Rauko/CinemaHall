package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfExportService {
    public byte[] export(List<PurchaseHistoryExportDto> data) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

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
        } catch (Exception e) {
            throw new RuntimeException("PDF export failed", e);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }
}
