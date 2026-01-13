package com.cinema.service;

import com.cinema.model.Ticket;
import com.cinema.model.TicketStatus;
import com.cinema.model.User;
import com.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getTicketsByUser(User user){
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getPaidTicketsByUser(User user){
        return ticketRepository.findByUserAndStatus(user, TicketStatus.PAID);
    }
}
