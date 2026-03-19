package com.cinema.dto;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.enums.PurchaseActionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseHistoryDto {

    private Long ticketId;
    private PurchaseActionType action;
    private double amount;
    private LocalDateTime purchaseTime;

    public static PurchaseHistoryDto fromEntity(PurchaseHistory h) {
        return new PurchaseHistoryDto(
                h.getTicket().getId(),
                h.getAction(),
                h.getAmount(),
                h.getPurchaseTime()
        );
    }
}
