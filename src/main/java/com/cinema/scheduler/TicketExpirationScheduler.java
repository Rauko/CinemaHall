package com.cinema.scheduler;

import com.cinema.service.TicketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketExpirationScheduler {

    private final TicketService ticketService;

    public TicketExpirationScheduler(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Scheduled(fixedRate = 60000) //every min
    public void expireReservations() {
        ticketService.expireOldReservations();
    }
}
