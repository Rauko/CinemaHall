package com.cinema.dto;

import com.cinema.model.PurchaseHistory;
import com.cinema.model.enums.PurchaseActionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseHistoryDto {

    private Long ticketId;
    private PurchaseActionType action;
    private double amount;
    private LocalDateTime createAt;

    public static PurchaseHistoryDto fromEntity(PurchaseHistory h) {
        return new PurchaseHistoryDto(
                h.getTicket().getId(),
                h.getAction(),
                h.getAmount(),
                h.getMadeAt()
        );
    }
}
