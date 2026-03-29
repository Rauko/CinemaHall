package com.cinema.dto.purchase.mapper;

import com.cinema.dto.purchase.PurchaseHistoryDto;
import com.cinema.model.PurchaseHistory;

public class PurchaseHistoryMapper {
    public static PurchaseHistoryDto toDto(PurchaseHistory h) {
        if(h == null) return null;

        return new PurchaseHistoryDto(
                h.getTicket().getId(),
                h.getAction(),
                h.getAmount(),
                h.getPurchaseTime()
        );
    }
}
