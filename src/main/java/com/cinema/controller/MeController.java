package com.cinema.controller;

import com.cinema.dto.PurchaseHistoryDto;
import com.cinema.dto.ticket.TicketDto;
import com.cinema.model.enums.TicketStatus;
import com.cinema.model.User;
import com.cinema.service.PurchaseHistoryService;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/me")
public class MeController {

    private final TicketService ticketService;
    private final UserService userService;
    private final PurchaseHistoryService purchaseHistoryService;

    public MeController(TicketService ticketService,
                        UserService userService,
                        PurchaseHistoryService purchaseHistoryService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.purchaseHistoryService = purchaseHistoryService;
    }

    @GetMapping("/tickets")
    public List<TicketDto> myTickets() {
        return ticketService.getMyTickets()
                .stream()
                .map(TicketDto::fromEntity)
                .toList();
    }

    @GetMapping("/tickets/paid")
    public List<TicketDto> myPaidTickets() {
        return ticketService.getMyPaidTickets()
                .stream()
                .map(TicketDto::fromEntity)
                .toList();
    }

    @GetMapping("/tickets/reserved")
    public List<TicketDto> myReservedTickets() {
        return ticketService.getMyTicketsByStatus(TicketStatus.RESERVED)
                .stream()
                .map(TicketDto::fromEntity)
                .toList();
    }

    @GetMapping("/history")
    public List<PurchaseHistoryDto> myPurchaseHistory() {
        User currentUser = userService.getCurrentUser();
        return purchaseHistoryService.getUserHistory(currentUser)
                .stream()
                .map(PurchaseHistoryDto::fromEntity)
                .toList();

    }

}
