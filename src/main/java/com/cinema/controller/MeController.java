package com.cinema.controller;

import com.cinema.dto.PurchaseHistoryDto;
import com.cinema.dto.ticket.TicketDto;
import com.cinema.model.enums.ExportFormat;
import com.cinema.model.enums.TicketStatus;
import com.cinema.model.User;
import com.cinema.service.PurchaseHistoryService;
import com.cinema.service.TicketService;
import com.cinema.service.UserService;
import com.cinema.service.export.PurchaseHistoryExportService;
import com.cinema.util.DateParseUtil;
import com.cinema.util.FilenameConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user/me")
public class MeController {

    private final TicketService ticketService;
    private final UserService userService;
    private final PurchaseHistoryService purchaseHistoryService;
    private final PurchaseHistoryExportService purchaseHistoryExportService;

    public MeController(TicketService ticketService,
                        UserService userService,
                        PurchaseHistoryService purchaseHistoryService,
                        PurchaseHistoryExportService purchaseHistoryExportService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.purchaseHistoryService = purchaseHistoryService;
        this.purchaseHistoryExportService = purchaseHistoryExportService;
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

    @GetMapping("/history/export")
    public ResponseEntity<byte[]> exportMyHistory(
                            @RequestParam ExportFormat format,
                            Authentication auth
            ) {
        byte[] file = purchaseHistoryExportService.exportForCurrentUser(format);

        String username = auth.getName();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" +
                                FilenameConstructor.filenameUpToDate(username, LocalDateTime.now(), format))
                .body(file);
    }

    @GetMapping("/history/period")
    public List<PurchaseHistoryDto> myHistoryForPeriod(
            @RequestParam String start,
            @RequestParam String end){

        LocalDateTime startDate = DateParseUtil.parseStartDate(start);
        LocalDateTime endDate = DateParseUtil.parseEndDate(end);

        return purchaseHistoryService
                .getHistoryForPeriod(startDate, endDate)
                .stream()
                .map(PurchaseHistoryDto::fromEntity)
                .toList();
    }

    @GetMapping("/history/export/period")
    public ResponseEntity<byte[]> exportMyHistoryPeriod(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam ExportFormat format,
            Authentication auth) {

        LocalDateTime startDate = DateParseUtil.parseStartDate(start);
        LocalDateTime endDate = DateParseUtil.parseEndDate(end);

        byte[] file = purchaseHistoryExportService
                .exportForCurrentUserPeriod(startDate, endDate, format);

        String username = auth.getName();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" +
                                FilenameConstructor.filename(username, startDate, endDate, format))
                .body(file);
    }
}
