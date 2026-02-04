package com.cinema.service;

import com.cinema.model.*;
import com.cinema.repository.ScreeningRepository;
import com.cinema.repository.SeatRepository;
import com.cinema.repository.TicketRepository;
import com.cinema.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;

    @Value("${ticket.reservation-expiration-minutes}")
    private int expirationTimeMinutes;


    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         ScreeningRepository screeningRepository,
                         SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
    }

    // Current user

    private User getCurrentUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Getters

    public List<Ticket> getMyTickets(){
        return ticketRepository.findByUser(getCurrentUser());
    }

    public List<Ticket> getMyPaidTickets(){
        return ticketRepository.findByUserAndStatus(
                getCurrentUser(),
                TicketStatus.PAID
        );
    }

    public List<Ticket> getTicketsByUserAndStatus(TicketStatus status){
        return ticketRepository.findByUserAndStatus(
                getCurrentUser(),
                status
        );
    }

    // Create Ticket

    public Ticket createTicket(Long screeningId,
                               Long seatId,
                               ImaxGlassesOption glassesOption){
        User user = getCurrentUser();

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.getHall().getId().equals(screening.getHall().getId())) {
            throw new RuntimeException("Seat does not belong to screening hall");
        }

        boolean taken = ticketRepository.existsByScreeningIdAndSeatIdAndStatus(
                screeningId,
                seatId,
                List.of(TicketStatus.PAID, TicketStatus.RESERVED));


        if (taken) {
            throw new RuntimeException("Seat already taken");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setScreening(screening);
        ticket.setSeat(seat);

        double price = screening.getBasePrice();

        // IMAX
        if(screening.isImax()){
            price *= 1.5;

            if (glassesOption == ImaxGlassesOption.RENT){
                price += 50;
            } else if(glassesOption == ImaxGlassesOption.BUY) {
                price += 150;
            }
        }

        // VIP
        if (seat.getType().equals(SeatType.VIP)) {
            price += 0.5 * screening.getBasePrice();
        }

        ticket.setPrice(price);

        ticket.setStatus(TicketStatus.RESERVED);
        ticket.setReservedAt(java.time.LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    // Cancel Reservation

    public Ticket cancelReservation(Long ticketId){

        User user = getCurrentUser();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can cancel only YOUR OWN ticket");
        }

        if (ticket.getStatus() != TicketStatus.RESERVED) {
            throw new RuntimeException("Only RESERVED tickets can be cancelled");
        }

        ticket.setStatus(TicketStatus.CANCELLED);

        return ticketRepository.save(ticket);
    }

    // Auto expire

    @Scheduled(fixedRate = 60000) //once a minute
    @Transactional
    public void expireOldReservations(){
        LocalDateTime expirationTime =
                LocalDateTime.now().minusMinutes(expirationTimeMinutes);

        List<Ticket> oldTickets =
                ticketRepository.findByStatusAndReservedAtBefore(
                        TicketStatus.EXPIRED,
                        expirationTime
                );

        for (Ticket ticket : oldTickets) {
            ticket.setStatus(TicketStatus.EXPIRED);
        }

        ticketRepository.saveAll(oldTickets);
    }

    // Pay

    @Transactional
    public Ticket payForTicket(Long ticketId){
        User user = getCurrentUser();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can pay for YOUR OWN ticket only");
        }

        if (ticket.getStatus() != TicketStatus.RESERVED) {
            throw new RuntimeException("Only RESERVED tickets can be paid");
        }

        //expired payment protection
        LocalDateTime expirationTime =
                ticket.getReservedAt().plusMinutes(expirationTimeMinutes);

        if (expirationTime.isBefore(LocalDateTime.now())) {
            ticket.setStatus(TicketStatus.EXPIRED);
            ticketRepository.save(ticket);
            throw new RuntimeException("Ticket reservation expired");
        }

        //payment must be here

        ticket.setStatus(TicketStatus.PAID);
        ticket.setPurchaseTime(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}
