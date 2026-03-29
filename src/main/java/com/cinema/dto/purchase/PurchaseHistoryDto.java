package com.cinema.dto.purchase;

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
}
