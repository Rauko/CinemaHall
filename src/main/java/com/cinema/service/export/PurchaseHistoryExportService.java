package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.cinema.model.PurchaseHistory;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.repository.PurchaseHistoryRepository;
import com.cinema.repository.UserRepository;
import com.cinema.service.PurchaseHistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseHistoryExportService {

    private final PurchaseHistoryService purchaseHistoryService;
    private final TxtExportService txtExportService;
    private final PdfExportService pdfExportService;
    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public PurchaseHistoryExportService(
            PurchaseHistoryService purchaseHistoryService,
            TxtExportService txtExportService,
            PdfExportService pdfExportService,
            UserRepository userRepository,
            PurchaseHistoryRepository purchaseHistoryRepository
    ) {
        this.purchaseHistoryService = purchaseHistoryService;
        this.txtExportService = txtExportService;
        this.pdfExportService = pdfExportService;
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    public byte[] exportForCurrentUser(ExportFormat format) {
        return buildExport(getCurrentUser(), format);
    }

    public byte[] exportForAdmin(User user, ExportFormat format) {
        return buildExport(user, format);
    }

    private byte[] export(List<PurchaseHistoryExportDto> data, ExportFormat format) {
        return switch (format) {
            case TXT -> txtExportService.export(data);
            case PDF -> pdfExportService.export(data);
        };
    }

    private PurchaseHistoryExportDto map(PurchaseHistory history) {

        Ticket ticket = history.getTicket();

        return new PurchaseHistoryExportDto(
                ticket.getScreening().getMovie().getTitle(),
                ticket.getScreening().getHallName(),
                "Row " + ticket.getSeat().getRowNumber() +
                        ", Seat " + ticket.getSeat().getSeatNumber(),
                history.getAmount(),
                history.getPurchaseTime()
        );
    }

    private User getCurrentUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private byte[] buildExport(User user, ExportFormat format) {
        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService.getUserHistory(user)
                        .stream()
                        .map(this::map)
                        .toList();

        return export(data,format);
    }

    public byte[] exportAllUsers(ExportFormat format) {

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryRepository.findAll()
                        .stream()
                        .map(this::map)
                        .toList();

        return export(data,format);

    }
}
