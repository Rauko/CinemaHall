package com.cinema.service;

import com.cinema.model.Ticket;
import com.cinema.model.enums.TicketStatus;
import com.cinema.repository.TicketRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketCleanupService {

    private static final Logger log =
            LoggerFactory.getLogger(TicketCleanupService.class);


    private final TicketRepository ticketRepository;
    private final long expirationMinutes;

    public TicketCleanupService(TicketRepository ticketRepository,
                                @Value("${ticket.reservation-expiration-minutes:10}") long  expirationMinutes) {
        this.ticketRepository = ticketRepository;
        this.expirationMinutes = expirationMinutes;
    }

    @Scheduled(fixedRate = 60000) //every60 seconds
    public void cancelExpiredReservations() {

        log.info("Starting reservation cleanup");

        LocalDateTime now = LocalDateTime.now();
        List<Ticket> tickets = ticketRepository.findAll();

        int processed = 0;
        int expired = 0;

        for (Ticket ticket : tickets) {

            processed++;

            if (ticket.getStatus() == TicketStatus.RESERVED) {

                if (ticket.getReservedAt() == null) {

                    log.warn("Inconsistent ticket found: ticketId={}, status=RESERVED but reservedAt is null",
                            ticket.getId()
                    );

                    continue;
                }

                LocalDateTime expireAt =
                        ticket.getReservedAt()
                                .plusMinutes(expirationMinutes);

                if (now.isAfter(expireAt)) {

                    ticket.setStatus(TicketStatus.EXPIRED);
                    ticketRepository.save(ticket);

                    expired++;

                    log.info("Reservation expired: ticketId={}, expireAt={}",
                            ticket.getId(),
                            expireAt
                    );
                }
            }
        }

        log.info("Reservation cleanup completed: processedTickets={}, expiredTickets={}",
                processed,
                expired
        );
    }

    @Scheduled(cron = "0 0 * * * *") //every hour
    public void deleteExpiredTickets() {
        log.info("Starting expired ticket deletion");

        List<Ticket> expiredTickets =
                ticketRepository.findByStatus(TicketStatus.EXPIRED);

        if (expiredTickets.isEmpty()) {

            log.info("No expired tickets found for deletion");

            return;
        }

        int count = expiredTickets.size();

        ticketRepository.deleteAllByStatus(TicketStatus.EXPIRED);

        log.info("Expired tickets deleted: deletedCount={}", count);
    }
}
