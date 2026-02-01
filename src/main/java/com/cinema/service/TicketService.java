package com.cinema.service;

import com.cinema.model.*;
import com.cinema.repository.ScreeningRepository;
import com.cinema.repository.SeatRepository;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;

    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         ScreeningRepository screeningRepository,
                         SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
    }

    public List<Ticket> getTicketsByUser(User user){
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getPaidTicketsByUser(User user){
        return ticketRepository.findByUserAndStatus(user, TicketStatus.PAID);
    }

    public List<Ticket> getTicketsByUserAndStatus(User user, TicketStatus status){
        return ticketRepository.findByUserAndStatus(user, status);
    }

    public Ticket createTicket(Long userId,
                               Long screeningId,
                               Long seatId,
                               ImaxGlassesOption glassesOption){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.getHall().getId().equals(screening.getHall().getId())) {
            throw new RuntimeException("Seat does not belong to screening hall");
        }

        if (ticketRepository.existsByScreeningIdAndSeatId(screeningId, seatId)) {
            throw new RuntimeException("Ticket already taken");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setScreening(screening);
        ticket.setSeat(seat);

        double price = screening.getBasePrice();

        if(screening.isImax()){
            price *= 1.5;

            if (glassesOption == ImaxGlassesOption.RENT){
                price += 50;
            } else if(glassesOption == ImaxGlassesOption.BUY) {
                price += 150;
            }
        }

        if (seat.getType().equals(SeatType.VIP)) {
            price += 0.5 * screening.getBasePrice();
        }

        ticket.setPrice(price);

        ticket.setStatus(TicketStatus.RESERVED);
        ticket.setReservedAt(java.time.LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}
