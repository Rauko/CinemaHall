package com.cinema.service;

import com.cinema.dto.PaymentRequest;
import com.cinema.exception.PaymentException;
import com.cinema.exception.ReservationExpiredException;
import com.cinema.model.*;
import com.cinema.model.enums.*;
import com.cinema.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;
    private final PaymentService paymentService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final UserService userService;

    @Value("${ticket.reservation-expiration-minutes}")
    private int expirationTimeMinutes;

    // Getters

    public List<Ticket> getMyTickets(){
        return ticketRepository.findByUser(userService.getCurrentUser());
    }

    public List<Ticket> getMyPaidTickets(){
        return ticketRepository.findByUserAndStatus(
                userService.getCurrentUser(),
                TicketStatus.PAID
        );
    }

    public List<Ticket> getMyTicketsByStatus(TicketStatus status){
        return ticketRepository.findByUserAndStatus(
                userService.getCurrentUser(),
                status
        );
    }

    // Admin Getters

    public List<Ticket> getTicketsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ticketRepository.findByUser(user);
    }

    public List<Ticket> getTicketsByUserIdAndStatus(Long userId, TicketStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ticketRepository.findByUserAndStatus(user, status);
    }

    // Create Ticket

    public Ticket createTicket(Long screeningId,
                               Long seatId,
                               ImaxGlassesOption glassesOption){
        User user = userService.getCurrentUser();

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (!seat.getHall().getId().equals(screening.getHall().getId())) {
            throw new RuntimeException("Seat does not belong to screening hall");
        }

        boolean taken = ticketRepository.existsByScreeningIdAndSeatIdAndStatusIn(
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

        User user = userService.getCurrentUser();

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

    @Transactional // if exception - rollback
    public Ticket payForTicket(Long ticketId, PaymentRequest paymentRequest){
        User user = userService.getCurrentUser();

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can pay for YOUR OWN ticket only");
        }

        if (ticket.getStatus() != TicketStatus.RESERVED) {
            throw new RuntimeException("Only RESERVED tickets can be paid");
        }

        PurchaseHistory history = new PurchaseHistory();
        history.setUser(user);
        history.setAmount(ticket.getPrice());
        history.setAction(PurchaseActionType.PURCHASE);
        history.setPurchaseTime(LocalDateTime.now());
        history.setPaymentStatus(PaymentStatus.PENDING);

        purchaseHistoryRepository.save(history);


        //expired payment protection
        LocalDateTime expirationTime =
                ticket.getReservedAt().plusMinutes(expirationTimeMinutes);

        if (expirationTime.isBefore(LocalDateTime.now())) {
            ticket.setStatus(TicketStatus.EXPIRED);
            ticketRepository.save(ticket);

            history.setPaymentStatus(PaymentStatus.EXPIRED);
            purchaseHistoryRepository.save(history);

            throw new ReservationExpiredException("Ticket reservation expired");
        }

        //payment itself - if failed - EXCEPTION and rollback
        PaymentMethod paymentMethod =
                paymentMethodRepository
                        .findByUserAndIsDefaultTrue(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No default payment method found for user"));

        try {
            paymentService.processPayment(
                    paymentMethod,
                    ticket.getPrice()
            );
        } catch (PaymentException ex) {
            history.setPaymentStatus(PaymentStatus.FAILED);
            purchaseHistoryRepository.save(history);
            throw ex;
        }

        history.setPaymentStatus(PaymentStatus.SUCCESS);
        purchaseHistoryRepository.save(history);

        ticket.setStatus(TicketStatus.PAID);
        ticket.setPurchaseTime(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}
