package com.cinema.service.export;

import com.cinema.dto.export.PurchaseHistoryExportDto;
import com.cinema.model.PurchaseHistory;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.model.enums.ExportFormat;
import com.cinema.service.PurchaseHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseHistoryExportService {

    private final PurchaseHistoryService purchaseHistoryService;
    private final TxtExportService txtExportService;
    private final PdfExportService pdfExportService;

    public PurchaseHistoryExportService(
            PurchaseHistoryService purchaseHistoryService,
            TxtExportService txtExportService,
            PdfExportService pdfExportService
    ) {
        this.purchaseHistoryService = purchaseHistoryService;
        this.txtExportService = txtExportService;
        this.pdfExportService = pdfExportService;
    }

    public byte[] exportForUser(User user, ExportFormat format) {

        //TODO: will need to rewrite for auth later

        List<PurchaseHistoryExportDto> data =
                purchaseHistoryService.getUserHistory(user)
                        .stream()
                        .map(this::map)
                        .toList();

        return export(data,format);
    }

    private byte[] exportForAdmin(User user, ExportFormat format) {

        //TODO: after exportForUser will be changed take that method here

        return exportForUser(user, format);
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
}
