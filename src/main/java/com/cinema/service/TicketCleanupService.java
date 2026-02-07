package com.cinema.service;

import com.cinema.model.Ticket;
import com.cinema.model.enums.TicketStatus;
import com.cinema.repository.TicketRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketCleanupService {
    private final TicketRepository ticketRepository;
    private final long expirationMinutes;

    public TicketCleanupService(TicketRepository ticketRepository,
                                @Value("${ticket.reservation-expiration-minutes:10}") long  expirationMinutes) {
        this.ticketRepository = ticketRepository;
        this.expirationMinutes = expirationMinutes;
    }

    @Scheduled(fixedRate = 60000) //every60 seconds
    public void cancelExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Ticket> tickets = ticketRepository.findAll();

        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == TicketStatus.RESERVED) {
                LocalDateTime expireAt = ticket.getReservedAt().plusMinutes(expirationMinutes);
                if(now.isBefore(expireAt)) {
                    ticket.setStatus(TicketStatus.EXPIRED);
                    ticketRepository.save(ticket);
                    System.out.println("Reservation expired for ticket #" + ticket.getId());
                }
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *") //every hour
    public void deleteExpiredTickets() {
        List<Ticket> expiredTickets = ticketRepository.findByStatus(TicketStatus.EXPIRED);
        if(!expiredTickets.isEmpty()) {
            ticketRepository.deleteAllByStatus(TicketStatus.EXPIRED);
            System.out.println("Deleted " + expiredTickets.size() + " expired tickets");
        }
    }
}
