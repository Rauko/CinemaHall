package com.cinema.dto.export;

import java.time.LocalDateTime;

public record PurchaseHistoryExportDto (
        String movieTitle,
        String hallName,
        String seat,
        double price,
        LocalDateTime purchaseTime
) {}
