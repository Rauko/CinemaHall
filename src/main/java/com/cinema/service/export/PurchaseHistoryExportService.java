package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.cinema.model.PurchaseHistory;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.repository.PurchaseHistoryRepository;
import com.cinema.service.PurchaseHistoryService;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryExportService {

    private final PurchaseHistoryService purchaseHistoryService;
    private final TxtExportService txtExportService;
    private final PdfExportService pdfExportService;
    private final CsvExportService csvExportService;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserService userService;

    public byte[] exportForCurrentUser(ExportFormat format) {
        return buildExport(userService.getCurrentUser(), format);
    }

    public byte[] exportForAdmin(User user, ExportFormat format) {
        return buildExport(user, format);
    }

    private byte[] export(List<PurchaseHistoryExportDto> data, ExportFormat format) {
        return switch (format) {
            case TXT -> txtExportService.export(data);
            case PDF -> pdfExportService.export(data);
            case CSV -> csvExportService.export(data);
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

    public byte[] exportForCurrentUserPeriod(
            LocalDateTime start,
            LocalDateTime end,
            ExportFormat format) {
        User user = userService.getCurrentUser();

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService
                        .getUserHistoryForPeriod(user, start, end )
                        .stream()
                        .map(this::map)
                        .toList();

        return export(data,format);
    }

    public byte[] exportForAdminPeriod(
            User user,
            LocalDateTime start,
            LocalDateTime end,
            ExportFormat format) {

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService
                        .getUserHistoryForPeriod(user, start, end )
                        .stream()
                        .map(this::map)
                        .toList();

        return export(data,format);
    }
}
