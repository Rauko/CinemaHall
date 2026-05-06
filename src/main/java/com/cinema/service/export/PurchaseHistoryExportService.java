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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryExportService {

    private static final Logger log =
            LoggerFactory.getLogger(PurchaseHistoryExportService.class);

    private final PurchaseHistoryService purchaseHistoryService;
    private final TxtExportService txtExportService;
    private final PdfExportService pdfExportService;
    private final CsvExportService csvExportService;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserService userService;

    public byte[] exportForCurrentUser(ExportFormat format) {
        User user = userService.getCurrentUser();

        log.info("Starting export for current user: userId={}, format={}",
                user.getId(),
                format
        );

        byte[] result = buildExport(user, format);

        log.info("Export completed for current user: userId={}, format={}, fileSizeBytes={}",
                user.getId(),
                format,
                result.length
        );

        return result;
    }

    public byte[] exportForAdmin(User user, ExportFormat format) {
        log.info("Starting admin export: userId={}, format={}",
                user.getId(),
                format
        );

        byte[] result = buildExport(user, format);

        log.info("Admin export completed: userId={}, format={}, fileSizeBytes={}",
                user.getId(),
                format,
                result.length
        );

        return result;
    }

    private byte[] export(List<PurchaseHistoryExportDto> data, ExportFormat format) {

        if (data == null || data.isEmpty()) {

            log.warn("Export requested with empty data: format={}", format);

            data = List.of();
        }

        log.debug("Export parameters: format={}, recordsCount={}",
                format,
                data.size()
        );

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

        log.debug("Building export: userId={}, format={}",
                user.getId(),
                format
        );

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService.getUserHistory(user)
                        .stream()
                        .map(this::map)
                        .toList();

        if (data.isEmpty()) {
            log.warn("No export data found: userId={}, format={}",
                    user.getId(),
                    format
            );
        }

        return export(data,format);
    }

    public byte[] exportAllUsers(ExportFormat format) {

        log.info("Starting export for all users: format={}", format);

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryRepository.findAll()
                        .stream()
                        .map(this::map)
                        .toList();

        if (data.isEmpty()) {
            log.warn("No export data found for all users: format={}", format);
        }

        byte[] result = export(data, format);

        log.info("Export for all users completed: format={}, fileSizeBytes={}",
                format,
                result.length
        );

        return result;
    }

    public byte[] exportForCurrentUserPeriod(
            LocalDateTime start,
            LocalDateTime end,
            ExportFormat format) {
        User user = userService.getCurrentUser();

        log.info("Starting period export for current user: userId={}, format={}",
                user.getId(),
                format
        );

        log.debug("Export period parameters: userId={}, start={}, end={}, format={}",
                user.getId(),
                start,
                end,
                format
        );

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService
                        .getUserHistoryForPeriod(user, start, end )
                        .stream()
                        .map(this::map)
                        .toList();

        if (data.isEmpty()) {
            log.warn("No export data found for period: userId={}, start={}, end={}, format={}",
                    user.getId(),
                    start,
                    end,
                    format
            );
        }

        byte[] result = export(data, format);

        log.info("Period export completed for current user: userId={}, format={}, fileSizeBytes={}",
                user.getId(),
                format,
                result.length
        );

        return result;
    }

    public byte[] exportForAdminPeriod(
            User user,
            LocalDateTime start,
            LocalDateTime end,
            ExportFormat format) {

        log.info("Starting admin period export: userId={}, format={}",
                user.getId(),
                format
        );

        log.debug("Admin export period parameters: userId={}, start={}, end={}, format={}",
                user.getId(),
                start,
                end,
                format
        );

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService
                        .getUserHistoryForPeriod(user, start, end )
                        .stream()
                        .map(this::map)
                        .toList();

        if (data.isEmpty()) {
            log.warn("No admin export data found: userId={}, start={}, end={}, format={}",
                    user.getId(),
                    start,
                    end,
                    format
            );
        }

        byte[] result = export(data, format);

        log.info("Admin period export completed: userId={}, format={}, fileSizeBytes={}",
                user.getId(),
                format,
                result.length
        );

        return result;
    }
}
