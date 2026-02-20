package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TxtExportService {
    public byte[] export(List<PurchaseHistoryExportDto> data) {
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

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
